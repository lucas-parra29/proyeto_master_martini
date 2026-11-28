package com.example.proyectomartini.data.remote

import com.google.gson.annotations.SerializedName

data class RegistroRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String,
    val perfil: PerfilRemoto? = null
)

data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    val rol: String = "CLIENTE"
)