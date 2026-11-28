package com.example.proyectomartini.ui.screens

// Numeración de los pasos e ingredientes tal como aparecen en cada receta.
private val ingredientesPorPaso = mapOf(
    "mousse" to mapOf(1 to listOf(3, 4), 3 to listOf(1), 4 to listOf(2, 6), 5 to listOf(5), 7 to listOf(5)),
    "brownies" to mapOf(2 to listOf(1, 2), 3 to listOf(3, 4), 4 to listOf(5, 6, 7)),
    "trufas" to mapOf(1 to listOf(1), 2 to listOf(2), 4 to listOf(3), 5 to listOf(4)),
    "torta-vainilla" to mapOf(2 to listOf(2, 3, 4), 3 to listOf(1, 5, 6, 7), 6 to listOf(8)),
    "cheesecake" to mapOf(2 to listOf(1, 2), 3 to listOf(3, 4, 5), 4 to listOf(6), 6 to listOf(7)),
    "cupcakes" to mapOf(2 to listOf(2, 3, 4, 7), 3 to listOf(1, 5, 6), 7 to listOf(8, 9)),
    "helado-vainilla" to mapOf(1 to listOf(1), 2 to listOf(2, 3)),
    "helado-chocolate" to mapOf(1 to listOf(2, 3), 2 to listOf(1), 4 to listOf(4)),
    "sorbete-frutilla" to mapOf(1 to listOf(2, 3), 2 to listOf(1, 4)),
    "rollos-canela" to mapOf(1 to listOf(1, 2, 3, 5, 6), 2 to listOf(4), 4 to listOf(7, 8)),
    "brioche" to mapOf(1 to listOf(1, 2, 3, 4, 5, 6), 3 to listOf(7)),
    "bollos-leche" to mapOf(1 to listOf(1, 2, 3, 5, 6, 7), 2 to listOf(4)),
    "milhojas" to mapOf(2 to listOf(1), 3 to listOf(2, 6), 4 to listOf(3, 4, 5), 6 to listOf(7)),
    "palmeritas" to mapOf(2 to listOf(2, 3), 3 to listOf(1)),
    "tartaletas-hojaldre" to mapOf(2 to listOf(1), 3 to listOf(2), 4 to listOf(3, 4, 5), 5 to listOf(6)),
    "galletas-mantequilla" to mapOf(1 to listOf(2, 3), 2 to listOf(4, 5), 3 to listOf(1)),
    "galletas-chips" to mapOf(1 to listOf(2, 3, 4), 2 to listOf(5), 3 to listOf(1, 7), 4 to listOf(6)),
    "galletas-avena" to mapOf(2 to listOf(3, 4, 5), 3 to listOf(1, 2, 7), 4 to listOf(6)),
    "empanada-pino" to mapOf(1 to listOf(5, 6, 9), 4 to listOf(1, 2, 3, 4), 6 to listOf(7, 8)),
    "empanada-queso" to mapOf(1 to listOf(1, 2, 3, 4), 4 to listOf(5), 6 to listOf(6)),
    "empanada-pollo" to mapOf(1 to listOf(2), 2 to listOf(3, 4, 5), 3 to listOf(6), 4 to listOf(1), 6 to listOf(7)),
    "pan-amasado" to mapOf(1 to listOf(1, 2, 4, 5), 2 to listOf(3)),
    "focaccia" to mapOf(1 to listOf(1, 2, 3, 4, 5), 5 to listOf(5, 6, 7)),
    "pan-integral" to mapOf(1 to listOf(1, 2, 3, 4, 5, 6)),
)

internal fun ingredientesDelPaso(receta: RecetaMartini, indice: Int): List<String> =
    ingredientesPorPaso[receta.id]?.get(indice + 1).orEmpty().map { receta.ingredientes[it - 1] }
