package com.matrix.appmobilechimera.ui.course

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

    // 1. DECLARACIÓN: Esto quita el rojo de rvSections
    private lateinit var rvSections: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        // 2. VINCULACIÓN: Usamos los IDs exactos de tu XML
        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvCode = findViewById<TextView>(R.id.tvDetailCode)
        rvSections = findViewById(R.id.rvSections)

        rvSections.layoutManager = LinearLayoutManager(this)

        val course = intent.getParcelableExtra<Course>("COURSE_DATA")

        if (course != null) {
            tvName.text = course.fullname
            tvCode.text = "Código: ${course.shortname}"

            // 3. LLAMADA: Ahora la función ya existe
            cargarContenido(course.id)
        }
    }

    // 4. DEFINICIÓN: Esta es la función que te faltaba implementar
    private fun cargarContenido(courseId: Int) {
        RetrofitClient.instance.getCourseContents(courseId).enqueue(object : Callback<List<Section>> {
            override fun onResponse(call: Call<List<Section>>, response: Response<List<Section>>) {
                if (response.isSuccessful) {
                    val sections = response.body() ?: emptyList()
                    // Aquí es donde ocurre la magia y se llena la lista
                    rvSections.adapter = SectionAdapter(sections)
                }
            }

            override fun onFailure(call: Call<List<Section>>, t: Throwable) {
                Toast.makeText(this@CourseDetailActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}