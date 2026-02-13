package com.matrix.appmobilechimera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Section

class SectionAdapter(
    private val sections: List<Section>,
    private val onItemClick: (Section) -> Unit // Callback para la navegación jerárquica
) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    // ViewHolder: Referencias a las vistas del nuevo item_section.xml
    class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSectionName: TextView = view.findViewById(R.id.tvSectionName)
        val tvSectionSummary: TextView = view.findViewById(R.id.tvSectionSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        // Inflamos el diseño simplificado que creamos previamente
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]

        // 1. Asignamos el nombre de la Unidad
        holder.tvSectionName.text = section.name

        // 2. Cálculo dinámico del subtítulo (Estructura de Datos)
        // Mostramos cuántos elementos contiene la lista de módulos de esta sección
        val count = section.modules.size
        holder.tvSectionSummary.text = "$count actividades y recursos"

        // 3. Gestión de Eventos (Callback)
        // Al tocar la tarjeta, enviamos la sección completa a la actividad superior
        holder.itemView.setOnClickListener {
            onItemClick(section)
        }
    }

    override fun getItemCount() = sections.size
}