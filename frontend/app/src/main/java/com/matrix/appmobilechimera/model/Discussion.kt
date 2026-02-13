package com.matrix.appmobilechimera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Discussion(
    @SerializedName("discussion") val id: Int, // ID del hilo
    @SerializedName("name") val title: String, // Título del tema
    @SerializedName("message") val message: String, // Mensaje inicial
    @SerializedName("userfullname") val author: String, // Quién lo creó
    @SerializedName("subject") val subject: String // Asunto
) : Parcelable