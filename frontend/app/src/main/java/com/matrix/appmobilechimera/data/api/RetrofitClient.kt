package com.matrix.appmobilechimera.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ⚠ CAMBIA ESTO POR TU IP REAL (Ej: 192.168.1.15)
    // Mantén el puerto 8000 y el http:// al inicio
    private const val BASE_URL = "http://192.168.100.15:8000/"

    val instance: ChimeraApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ChimeraApi::class.java)
    }
}