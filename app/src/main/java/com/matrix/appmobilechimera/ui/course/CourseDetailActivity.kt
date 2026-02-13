package com.matrix.appmobilechimera.ui.course

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.Course
import com.matrix.appmobilechimera.model.Section
import com.matrix.appmobilechimera.ui.adapter.SectionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseDetailActivity : AppCompatActivity() {

    private lateinit var rvSections: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvCode = findViewById<TextView>(R.id.tvDetailCode)
        rvSections = findViewById(R.id.rvSections)
        rvSections.layoutManager = LinearLayoutManager(this)

        val course = intent.getParcelableExtra<Course>("COURSE_DATA")

        if (course != null) {
            tvName.text = course.fullname
            tvCode.text = "Código: ${course.shortname}"
            cargarContenido(course.id)
        }
    }

    private fun cargarContenido(courseId: Int) {
        RetrofitClient.instance.getCourseContents(courseId).enqueue(object : Callback<List<Section>> {
            override fun onResponse(call: Call<List<Section>>, response: Response<List<Section>>) {
                if (response.isSuccessful) {
                    val sections = response.body() ?: emptyList()
                    // MODIFICACIÓN: Pasamos una función lambda para manejar el clic en la sección
                    rvSections.adapter = SectionAdapter(sections) { section ->
                        irADetalleSeccion(section)
                    }
                }
            }

            override fun onFailure(call: Call<List<Section>>, t: Throwable) {
                Toast.makeText(this@CourseDetailActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // NUEVO MÉTODO: Abre la nueva actividad de detalle de sección
    private fun irADetalleSeccion(section: Section) {
        val intent = Intent(this, SectionDetailActivity::class.java)
        intent.putExtra("SECTION_DATA", section) // Gracias a Parcelable pasamos toda la unidad
        startActivity(intent)
    }
}