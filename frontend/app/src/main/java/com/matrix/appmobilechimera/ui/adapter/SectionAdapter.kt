package com.matrix.appmobilechimera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Section

class SectionAdapter(private val sections: List<Section>) :
    RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSectionName: TextView = view.findViewById(R.id.tvSectionName)
        val rvModules: RecyclerView = view.findViewById(R.id.rvModules)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.tvSectionName.text = section.name

        // Configuramos el RecyclerView anidado
        holder.rvModules.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvModules.adapter = ModuleAdapter(section.modules)
    }

    override fun getItemCount() = sections.size
}