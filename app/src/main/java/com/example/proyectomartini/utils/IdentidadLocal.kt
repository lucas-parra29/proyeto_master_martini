package com.example.proyectomartini.utils

import java.security.MessageDigest

object IdentidadLocal {
    fun clave(correo: String): String = MessageDigest.getInstance("SHA-256")
        .digest(correo.trim().lowercase(java.util.Locale.ROOT).toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it.toInt() and 0xff) }
}
