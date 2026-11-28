package com.example.proyectomartini.utils

object EstadisticasFavoritos {
    fun contarRecetas(tipos: Iterable<String>): Int = tipos.count { it == "RECETA" }
}
