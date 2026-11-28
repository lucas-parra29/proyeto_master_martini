package com.example.proyectomartini.utils
object Permisos {
    val roles = listOf("CLIENTE", "EDITOR", "INSTRUCTOR", "ADMIN")
    val tipos = listOf("RECETA", "CURSO", "LECCION", "VIDEO", "COMUNICADO")
    fun puedeEditar(rol: String, tipo: String): Boolean = when (tipo) {
        "RECETA" -> rol in setOf("ADMIN", "EDITOR", "INSTRUCTOR")
        "CURSO", "LECCION" -> rol in setOf("ADMIN", "INSTRUCTOR")
        "VIDEO", "COMUNICADO" -> rol in setOf("ADMIN", "EDITOR")
        else -> false
    }
    fun puedeAdministrar(rol: String) = rol == "ADMIN"
    fun validarContenido(titulo: String, descripcion: String): String? = when {
        titulo.isBlank() || titulo.length > 200 -> "Título obligatorio (máximo 200 caracteres)"
        descripcion.isBlank() || descripcion.length > 2000 -> "Descripción obligatoria (máximo 2000 caracteres)"
        else -> null
    }
}
