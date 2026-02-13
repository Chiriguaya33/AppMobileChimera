package com.matrix.appmobilechimera.model

// Module.kt
data class Module(
    val id: Int,
    val name: String,
    val modname: String, // 'assign', 'forum', 'resource', etc.
    val instance: Int
)