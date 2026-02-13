package com.matrix.appmobilechimera.model

import com.google.gson.annotations.SerializedName

// Esta clase es el espejo de lo que tu Python devuelve en /login
data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String
)