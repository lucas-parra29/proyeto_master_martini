package com.example.proyectomartini.utils
object AcademiaMartini {
    fun primerNombre(nombre: String): String = nombre.trim().split(Regex("\\s+")).firstOrNull().orEmpty()
    fun nombreVisible(perfil: String, sesion: String): String = sequenceOf(perfil,sesion).map { primerNombre(it) }.firstOrNull { it.isNotBlank() && it.uppercase() !in setOf("CLIENTE","ADMIN","EDITOR","INSTRUCTOR") }.orEmpty()
    fun cursoCompleto(cursoId: String, total: Int, completadas: Set<String>): Boolean = total > 0 && contarLecciones(cursoId,total,completadas) == total
    fun contarLecciones(cursoId: String, total: Int, completadas: Set<String>): Int = (0 until total).count { "$cursoId:$it" in completadas }
    fun siguienteLeccion(cursoId: String, total: Int, completadas: Set<String>): Int? = (0 until total).firstOrNull { "$cursoId:$it" !in completadas }
}
