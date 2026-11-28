
package com.example.proyectomartini.data.repository

import android.content.Context

data class UserProfile(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val tipoNegocio: String = "",
    val intereses: String = "",
    val foto: String = "",
    val nombreEmpresa: String = "",
    val direccionEmpresa: String = "",
    val cargoEmpresa: String = "",
    val descripcionEmpresa: String = "",
    val tamanoEmpresa: String = "",
    val rutEmpresa: String = "",
    val declaraEmpresa: Boolean? = null
)

class ProfileRepository(context: Context) {
    private val app = context.applicationContext
    private val preferences get() = app.getSharedPreferences(
        "perfil_master_martini_" + com.example.proyectomartini.utils.IdentidadLocal.clave(AuthRepository(app).correoSesion()),
        Context.MODE_PRIVATE
    )

    fun obtenerPerfil(): UserProfile {
        val declara = preferences.getString("declaraEmpresa", null)?.toBooleanStrictOrNull()
        return UserProfile(
            nombre = preferences.getString("nombre", "") ?: "",
            correo = preferences.getString("correo", "") ?: "",
            telefono = preferences.getString("telefono", "") ?: "",
            tipoNegocio = preferences.getString("tipoNegocio", "") ?: "",
            intereses = preferences.getString("intereses", "") ?: "",
            foto = preferences.getString("foto", "") ?: "",
            nombreEmpresa = preferences.getString("nombreEmpresa", "") ?: "",
            direccionEmpresa = preferences.getString("direccionEmpresa", "") ?: "",
            cargoEmpresa = preferences.getString("cargoEmpresa", "") ?: "",
            descripcionEmpresa = preferences.getString("descripcionEmpresa", "") ?: "",
            tamanoEmpresa = preferences.getString("tamanoEmpresa", "").orEmpty(),
            rutEmpresa = preferences.getString("rutEmpresa", "").orEmpty(),
            declaraEmpresa = declara
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
            .putString("nombreEmpresa", perfil.nombreEmpresa)
            .putString("direccionEmpresa", perfil.direccionEmpresa)
            .putString("cargoEmpresa", perfil.cargoEmpresa)
            .putString("descripcionEmpresa", perfil.descripcionEmpresa)
            .putString("tamanoEmpresa", perfil.tamanoEmpresa)
            .putString("rutEmpresa", perfil.rutEmpresa)
            .apply { perfil.declaraEmpresa?.let { putString("declaraEmpresa", it.toString()) } }
            .apply()
    }
}
