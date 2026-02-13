package com.matrix.appmobilechimera.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mission(
    val id: Int,
    val title: String,
    val courseName: String,
    val type: String, // Tarea, Foro, etc.
    val dueDate: String,
    val dueTime: String,
    val progress: Int,
    val xpReward: Int,
    val status: String = "Pendiente" // Nuevo: "Pendiente" o "Entregado"
) : Parcelable