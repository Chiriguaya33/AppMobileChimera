package com.matrix.appmobilechimera.ui.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Mission

class MissionAdapter(
    private val missions: List<Mission>,
    private val onItemClick: (Mission) -> Unit
) : RecyclerView.Adapter<MissionAdapter.MissionViewHolder>() {

    class MissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvMissionTitle)
        val tvDetails: TextView = view.findViewById(R.id.tvMissionDetails)
        val xpBadge: TextView = view.findViewById(R.id.xpBadge)
        val tvDueDate: TextView = view.findViewById(R.id.tvDueDate)
        val pbProgress: ProgressBar = view.findViewById(R.id.missionProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission, parent, false)
        return MissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = missions[position]

        // 1. Datos básicos
        holder.tvTitle.text = mission.title
        holder.tvDetails.text = "${mission.courseName} • ${mission.type}"
        holder.xpBadge.text = "+${mission.xpReward} XP"
        holder.pbProgress.progress = mission.progress

        // 2. Lógica de Pulido: Feedback de Estado (C3)
        // Usamos ContextCompat para obtener colores de forma segura según la versión de Android
        val context = holder.itemView.context

        if (mission.status == "Entregado") {
            holder.tvDueDate.text = "✓ Tarea Entregada"
            holder.tvDueDate.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))
            holder.tvDueDate.setTypeface(null, Typeface.BOLD)
            holder.pbProgress.progress = 100 // Si está entregada, la barra debe estar llena
        } else {
            holder.tvDueDate.text = "Vence: ${mission.dueDate} a las ${mission.dueTime}"
            holder.tvDueDate.setTextColor(ContextCompat.getColor(context, R.color.chimera_orange))
            holder.tvDueDate.setTypeface(null, Typeface.NORMAL)
        }

        // 3. Gestión de Clics
        holder.itemView.setOnClickListener {
            onItemClick(mission)
        }
    }

    override fun getItemCount() = missions.size
}