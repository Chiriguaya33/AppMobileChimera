package com.matrix.appmobilechimera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    @SerializedName("id") val id: Int,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("shortname") val shortname: String,
    @SerializedName("summary") val summary: String? = null
) : Parcelable
