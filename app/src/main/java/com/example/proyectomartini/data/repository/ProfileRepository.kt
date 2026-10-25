package com.example.proyectomartini.data.repository

import android.content.Context

data class UserProfile(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val tipoNegocio: String = "",
    val intereses: String = "",
    val foto: String = ""
)

class ProfileRepository(context: Context) {

    private val preferences = context.getSharedPreferences(
        "perfil_master_martini",
        Context.MODE_PRIVATE
    )

    fun obtenerPerfil(): UserProfile {
        return UserProfile(
            nombre = preferences.getString("nombre", "") ?: "",
            correo = preferences.getString("correo", "") ?: "",
            telefono = preferences.getString("telefono", "") ?: "",
            tipoNegocio = preferences.getString("tipoNegocio", "") ?: "",
            intereses = preferences.getString("intereses", "") ?: "",
            foto = preferences.getString("foto", "") ?: ""
        )
    }

    fun guardarPerfil(perfil: UserProfile) {
        preferences.edit()
            .putString("nombre", perfil.nombre)
            .putString("correo", perfil.correo)
            .putString("telefono", perfil.telefono)
            .putString("tipoNegocio", perfil.tipoNegocio)
            .putString("intereses", perfil.intereses)
            .putString("foto", perfil.foto)
            .apply()
    }
}
