package com.matrix.appmobilechimera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Module(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("modname") val modname: String, // 'assign', 'forum', 'resource', etc.
    @SerializedName("instance") val instance: Int   // Este es el ID real de la tarea en Moodle
) : Parcelable