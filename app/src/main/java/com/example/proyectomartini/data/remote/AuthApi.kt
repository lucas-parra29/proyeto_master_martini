package com.example.proyectomartini.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/register")
    suspend fun registrar(@Body body: RegistroRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun iniciarSesion(@Body body: LoginRequest): AuthResponse
}
