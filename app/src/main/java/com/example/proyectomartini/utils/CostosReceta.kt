package com.example.proyectomartini.utils

import java.math.BigDecimal
import java.math.RoundingMode

object CostosReceta {
    private val cantidadInicial = Regex("^([0-9]+(?:[.,][0-9]+)?)(\\s+.+)$")

    fun escalar(ingrediente: String, base: Int, porciones: Int): String {
        require(base > 0 && porciones > 0)
        if (base == porciones) return ingrediente
        val match = cantidadInicial.matchEntire(ingrediente) ?: return ingrediente
        val cantidad = match.groupValues[1].replace(',', '.').toBigDecimal()
        val ajustada = cantidad.multiply(porciones.toBigDecimal())
            .divide(base.toBigDecimal(), 2, RoundingMode.HALF_UP)
            .stripTrailingZeros().toPlainString().replace('.', ',')
        return ajustada + match.groupValues[2]
    }

    data class Resultado(val lote: BigDecimal, val porcion: BigDecimal, val precio: BigDecimal)

    fun calcular(insumos: String, otros: String, margen: String, porciones: Int): Resultado? {
        fun decimal(valor: String) = valor.trim().replace(',', '.').toBigDecimalOrNull()
        val ingredientes = decimal(insumos)?.takeIf { it >= BigDecimal.ZERO } ?: return null
        val gastos = decimal(otros.ifBlank { "0" })?.takeIf { it >= BigDecimal.ZERO } ?: return null
        val objetivo = decimal(margen)?.takeIf { it >= BigDecimal.ZERO && it < BigDecimal(100) } ?: return null
        if (porciones <= 0) return null
        val lote = ingredientes + gastos
        val porcion = lote.divide(porciones.toBigDecimal(), 2, RoundingMode.HALF_UP)
        val precio = lote.multiply(BigDecimal(100)).divide(
            porciones.toBigDecimal().multiply(BigDecimal(100) - objetivo), 0, RoundingMode.CEILING)
        return Resultado(lote, porcion, precio)
    }
}
