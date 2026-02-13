package com.matrix.appmobilechimera.model

// Section.kt
data class Section(
    val id: Int,
    val name: String,      // Ej: "Unidad 1: Introducción"
    val modules: List<Module> // La lista de actividades dentro de la sección
)