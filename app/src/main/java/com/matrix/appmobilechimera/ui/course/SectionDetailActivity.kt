package com.matrix.appmobilechimera.ui.course

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Module
import com.matrix.appmobilechimera.model.Section
import com.matrix.appmobilechimera.ui.adapter.ModuleAdapter
import com.matrix.appmobilechimera.ui.mission.MissionSubmissionActivity

class SectionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        // 1. Recuperar datos de la sección
        val section = intent.getParcelableExtra<Section>("SECTION_DATA")

        // 2. Vincular y mostrar el título
        findViewById<TextView>(R.id.tvSectionTitle).text = section?.name ?: "Sin nombre"

        // 3. Configurar el texto explicativo (HTML de Moodle)
        val tvDescription = findViewById<TextView>(R.id.tvSectionDescription)

        if (!section?.summary.isNullOrEmpty()) {
            // Convierte etiquetas <p>, <b>, etc., en texto con formato para Android
            tvDescription.text = Html.fromHtml(section?.summary, Html.FROM_HTML_MODE_COMPACT)
            tvDescription.visibility = View.VISIBLE
        } else {
            // Oculta el campo si no hay descripción para no dejar espacios vacíos (C3)
            tvDescription.visibility = View.GONE
        }

        // 4. Configurar lista de módulos
        val rvModules = findViewById<RecyclerView>(R.id.rvModules)
        rvModules.layoutManager = LinearLayoutManager(this)

        if (section != null) {
            rvModules.adapter = ModuleAdapter(section.modules) { module ->
                verificarTipoModulo(module)
            }
        }
    }

    private fun verificarTipoModulo(module: Module) {
        // Solo permitimos el envío si es una tarea de Moodle ('assign')
        if (module.modname == "assign") {
            abrirPantallaEnvio(module)
        }
    }

    private fun abrirPantallaEnvio(module: Module) {
        val intent = Intent(this, MissionSubmissionActivity::class.java)
        // Pasamos el módulo completo para que la pantalla de envío tenga el ID y nombre
        intent.putExtra("MODULE_DATA", module)
        startActivity(intent)
    }
}