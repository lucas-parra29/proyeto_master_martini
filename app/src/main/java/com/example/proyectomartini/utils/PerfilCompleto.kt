package com.example.proyectomartini.utils

import com.example.proyectomartini.data.repository.UserProfile
import java.text.Normalizer
import java.util.Locale

object PerfilCompleto {
    val catalogo = listOf("Chocolatería", "Pastelería", "Panadería", "Heladería", "Bollería",
        "Galletas", "Empanadas", "Food Service", "Horneo")

    enum class Campo(val etiqueta: String) {
        NOMBRE("Nombre"), NEGOCIO("Tipo de negocio"), EMPRESA("Nombre de la empresa"), INTERESES("Intereses")
    }

    fun intereses(texto: String): Set<String> =
        texto.split(',').map(String::trim).filter(String::isNotBlank).toSet()

    fun tieneEmpresa(perfil: UserProfile) = perfil.declaraEmpresa
        ?: (perfil.nombreEmpresa.isNotBlank() || perfil.tipoNegocio.isNotBlank())

    fun pendientes(perfil: UserProfile): List<Campo> = buildList {
        if (perfil.nombre.isBlank() || perfil.nombre.trim().uppercase(Locale.ROOT) in
            setOf("CLIENTE", "ADMIN", "EDITOR", "INSTRUCTOR")) add(Campo.NOMBRE)
        if (tieneEmpresa(perfil)) {
            if (perfil.nombreEmpresa.isBlank()) add(Campo.EMPRESA)
            if (perfil.tipoNegocio.isBlank()) add(Campo.NEGOCIO)
        }
    }

    fun porcentaje(perfil: UserProfile): Int {
        val campos = if (tieneEmpresa(perfil)) 3 else 1
        return ((campos - pendientes(perfil).size) * 100 + campos / 2) / campos
    }

    fun coincide(interes: String, categoria: String): Boolean {
        fun normalizar(valor: String) = Normalizer.normalize(valor, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "").lowercase(Locale.ROOT).trim()
        val termino = normalizar(interes)
        val texto = normalizar(categoria)
        return termino.isNotBlank() && (termino in texto || (texto.isNotBlank() && texto in termino))
    }
}
