package com.matrix.appmobilechimera.data.api

import com.matrix.appmobilechimera.model.User
import com.matrix.appmobilechimera.model.Course
import com.matrix.appmobilechimera.model.Mission
import com.matrix.appmobilechimera.model.Section
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ChimeraApi {

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<User>

    @GET("cursos/{id}")
    fun getCursos(@Path("id") userId: Int): Call<List<Course>>

    @GET("misiones/{id}")
    fun getMisiones(@Path("id") userId: Int): Call<List<Mission>>

    @GET("cursos/{curso_id}/secciones")
    fun getCourseContents(@Path("curso_id") cursoId: Int): Call<List<Section>>

    // --- NUEVO MÉTODO PARA ENVÍO DE TAREAS ---
    @Multipart
    @POST("/enviar_mision")
    fun enviarMision(
        @Part("user_id") userId: RequestBody,
        @Part("mission_id") missionId: RequestBody,
        @Part("comentario") comentario: RequestBody,
        @Part archivo: MultipartBody.Part? // El archivo es opcional por si solo envían texto
    ): Call<Unit> // Unit porque solo esperamos una confirmación (200 OK)
}

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null
)