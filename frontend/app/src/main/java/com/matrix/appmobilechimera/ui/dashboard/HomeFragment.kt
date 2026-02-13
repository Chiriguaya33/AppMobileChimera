package com.matrix.appmobilechimera.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.Course
import com.matrix.appmobilechimera.ui.adapter.CourseAdapter
import com.matrix.appmobilechimera.ui.course.CourseDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    // Declaramos como propiedades de clase para evitar errores de Scope
    private lateinit var rvCourses: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicialización vinculada al XML
        rvCourses = view.findViewById(R.id.rvCourses)
        progressBar = view.findViewById(R.id.progressBar)
        rvCourses.layoutManager = LinearLayoutManager(requireContext())

        // Recuperamos la sesión guardada en LoginActivity
        val sharedPref = requireContext().getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        val fullname = sharedPref.getString("FULLNAME", "Estudiante")
        val userId = sharedPref.getInt("USER_ID", -1)

        view.findViewById<TextView>(R.id.tvWelcome).text = "Hola, $fullname"

        // Cargamos los datos desde Python enviando el ID real
        cargarCursos(userId)

        return view
    }

    private fun cargarCursos(userId: Int) {
        if (userId == -1) return

        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getCursos(userId).enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val cursos = response.body() ?: emptyList()
                    rvCourses.adapter = CourseAdapter(cursos) { curso ->
                        irAlDetalle(curso)
                    }
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Uso de requireContext() para evitar errores si el fragment se cierra
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun irAlDetalle(course: Course) {
        val intent = Intent(requireContext(), CourseDetailActivity::class.java)
        // IMPORTANTE: Requiere que Course sea Parcelable
        intent.putExtra("COURSE_DATA", course)
        startActivity(intent)
    }
}