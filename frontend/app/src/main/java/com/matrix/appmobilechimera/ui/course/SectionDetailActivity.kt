package com.matrix.appmobilechimera.ui.course

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.Module
import com.matrix.appmobilechimera.model.Section
import com.matrix.appmobilechimera.ui.adapter.ModuleAdapter
import com.matrix.appmobilechimera.ui.mission.MissionSubmissionActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SectionDetailActivity : AppCompatActivity() {

    private var section: Section? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        section = intent.getParcelableExtra<Section>("SECTION_DATA")

        findViewById<TextView>(R.id.tvSectionTitle).text = section?.name ?: "Sin nombre"

        val tvDescription = findViewById<TextView>(R.id.tvSectionDescription)
        if (!section?.summary.isNullOrEmpty()) {
            tvDescription.text = Html.fromHtml(section?.summary, Html.FROM_HTML_MODE_COMPACT)
            tvDescription.visibility = View.VISIBLE
        } else {
            tvDescription.visibility = View.GONE
        }

        val rvModules = findViewById<RecyclerView>(R.id.rvModules)
        rvModules.layoutManager = LinearLayoutManager(this)

        section?.let { currentSection ->
            rvModules.adapter = ModuleAdapter(currentSection.modules) { module ->
                verificarTipoModulo(module)
            }
        }
    }

    private fun verificarTipoModulo(module: Module) {
        when (module.modname) {
            "assign" -> abrirPantallaEnvio(module)
            "forum" -> abrirForo(section)
            // NUEVO: Detecta que es una sub-sección y pide su contenido
            "sub-section" -> cargarYEntrarASemana(module.instance)
        }
    }

    private fun cargarYEntrarASemana(sectionId: Int) {
        Toast.makeText(this, "Cargando contenido de la semana...", Toast.LENGTH_SHORT).show()

        // Consultamos el servidor para obtener los módulos internos de la semana
        RetrofitClient.instance.getSectionDetail(sectionId).enqueue(object : Callback<Section> {
            override fun onResponse(call: Call<Section>, response: Response<Section>) {
                if (response.isSuccessful && response.body() != null) {
                    // Navegación Recursiva: Abrimos esta misma actividad con los nuevos datos
                    val intent = Intent(this@SectionDetailActivity, SectionDetailActivity::class.java)
                    intent.putExtra("SECTION_DATA", response.body())
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Section>, t: Throwable) {
                Toast.makeText(this@SectionDetailActivity, "Error al conectar con la semana", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun abrirForo(section: Section?) {
        val intent = Intent(this, ForumActivity::class.java)
        intent.putExtra("CURSO_ID", section?.id ?: -1)
        intent.putExtra("CURSO_NAME", section?.name ?: "Curso")
        startActivity(intent)
    }

    private fun abrirPantallaEnvio(module: Module) {
        val intent = Intent(this, MissionSubmissionActivity::class.java)
        intent.putExtra("MODULE_DATA", module)
        startActivity(intent)
    }
}