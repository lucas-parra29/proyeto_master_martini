package com.example.proyectomartini.data.repository

import android.content.Context
import java.security.MessageDigest

class AuthRepository(context: Context) {

    private val preferences = context.getSharedPreferences(
        "auth_master_martini",
        Context.MODE_PRIVATE
    )

    fun registrarUsuario(
        nombre: String,
        correo: String,
        password: String
    ) {
        preferences.edit()
            .putString("nombre", nombre.trim())
            .putString("correo", correo.trim().lowercase())
            .putString("password_hash", crearHash(password))
            .apply()
    }

    fun validarCredenciales(
        correo: String,
        password: String
    ): Boolean {

        val correoNormalizado = correo.trim().lowercase()

        // Usuario demo del MVP
        if (
            correoNormalizado == "demo@martini.cl" &&
            password == "martini123"
        ) {
            return true
        }

        val correoGuardado =
            preferences.getString("correo", null)

        val passwordGuardada =
            preferences.getString("password_hash", null)

        return correoGuardado == correoNormalizado &&
                passwordGuardada == crearHash(password)
    }

    fun existeCuenta(): Boolean {
        return preferences.contains("correo")
    }

    private fun crearHash(texto: String): String {

        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(texto.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }
}

