package com.matrix.appmobilechimera.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Mission

// 1. MODIFICACIÓN: Agregamos el callback onItemClick al constructor
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

        holder.tvTitle.text = mission.title
        holder.tvDetails.text = "${mission.courseName} • ${mission.type}"
        holder.xpBadge.text = "+${mission.xpReward} XP"
        holder.tvDueDate.text = "Vence: ${mission.dueDate}"
        holder.pbProgress.progress = mission.progress

        // 2. MODIFICACIÓN: Escuchamos el clic en toda la tarjeta (itemView)
        holder.itemView.setOnClickListener {
            onItemClick(mission) // Ejecutamos la función que viene del Fragment
        }
    }

    override fun getItemCount() = missions.size
}