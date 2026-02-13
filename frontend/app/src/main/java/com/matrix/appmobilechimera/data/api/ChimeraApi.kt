package com.matrix.appmobilechimera.data.api

import com.matrix.appmobilechimera.model.User
import com.matrix.appmobilechimera.model.Course
import com.matrix.appmobilechimera.model.Mission // IMPORTANTE: Importa tu modelo Mission
import com.matrix.appmobilechimera.model.Section
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChimeraApi {

    // 1. LOGIN: Recibe un Objeto User { id, fullname, ... }
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<User>

    // 2. CURSOS: Recibe una LISTA de Cursos [ {...}, {...} ]
    @GET("cursos/{id}")
    fun getCursos(@Path("id") userId: Int): Call<List<Course>>

    // 3. MISIONES: Recibe una LISTA de Misiones [ {...}, {...} ]
    // Esta es la que alimentará tu MissionsFragment
    @GET("misiones/{id}")
    fun getMisiones(@Path("id") userId: Int): Call<List<Mission>>

    @GET("cursos/{curso_id}/secciones")
    fun getCourseContents(@Path("curso_id") cursoId: Int): Call<List<Section>>
}

// Clase auxiliar para el cuerpo del POST
data class LoginRequest(val email: String)