package com.matrix.appmobilechimera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Section(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("summary") val summary: String? = "", // Nuevo campo
    @SerializedName("modules") val modules: List<Module> = emptyList()
) : Parcelable