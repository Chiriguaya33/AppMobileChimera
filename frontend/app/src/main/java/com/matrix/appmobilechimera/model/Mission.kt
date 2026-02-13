package com.matrix.appmobilechimera.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mission(
    val id: Int,
    val title: String,         // Ej: "Laboratorio 1"
    val courseName: String,    // Ej: "Seguridad Informática"
    val type: String,          // Ej: "Vencimiento de Tarea"
    val dueDate: String,       // Ej: "jueves, 19 de febrero"
    val dueTime: String,       // Ej: "00:00"
    val progress: Int,         // Simulado para el diseño visual
    val xpReward: Int          // Valor estético para el diseño gamificado
) : Parcelable