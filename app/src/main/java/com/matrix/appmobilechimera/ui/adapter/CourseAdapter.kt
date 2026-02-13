package com.matrix.appmobilechimera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Course

// Esta clase recibe la lista de cursos que vienen de Python
class CourseAdapter(
    private val courses: List<Course>,
    private val onItemClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    // CLASE INTERNA (ViewHolder):
    // Su único trabajo es "recordar" las referencias a los TextViews
    // para que Android no tenga que buscarlos (findViewById) una y otra vez.
    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCourseName: TextView = view.findViewById(R.id.tvCourseName)
        val tvProfessor: TextView = view.findViewById(R.id.tvProfessor)
        val chipId: TextView = view.findViewById(R.id.chipId)
        // val progressBar: ProgressBar = view.findViewById(R.id.progressBar) // Opcional
    }

    // 1. CREAR EL MOLDE
    // Este método se ejecuta solo unas pocas veces (solo las necesarias para llenar la pantalla inicial)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    // 2. LLENAR DATOS (BINDING)
    // Este método se ejecuta MUCHAS veces (cada vez que aparece una tarjeta en pantalla al hacer scroll)
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.tvCourseName.text = course.fullname
        holder.tvProfessor.text = course.summary ?: "Materia de Ingeniería"
        holder.chipId.text = "#${course.id}"

        // ESCUCHADOR DE CLIC:
        holder.itemView.setOnClickListener {
            onItemClick(course) // Le avisamos a la Activity qué curso se tocó
        }
    }

    // 3. CONTAR ELEMENTOS
    // Le dice al RecyclerView cuántos elementos hay en total
    override fun getItemCount() = courses.size
}