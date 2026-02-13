package com.matrix.appmobilechimera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Module

class ModuleAdapter(private val modules: List<Module>) :
    RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>() {

    class ModuleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvModuleName)
        val ivIcon: ImageView = view.findViewById(R.id.ivModuleIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val module = modules[position]
        holder.tvName.text = module.name

        // Lógica para cambiar el icono según el tipo de Moodle (modname)
        when (module.modname) {
            "assign" -> holder.ivIcon.setImageResource(R.drawable.ic_missions) // Tarea
            "resource" -> holder.ivIcon.setImageResource(R.drawable.ic_profile) // PDF/Archivo (usa tus iconos)
            "forum" -> holder.ivIcon.setImageResource(R.drawable.ic_home) // Foro
        }
    }

    override fun getItemCount() = modules.size
}