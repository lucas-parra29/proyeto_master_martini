package com.example.proyectomartini.utils

import org.junit.Assert.*
import com.example.proyectomartini.data.repository.UserProfile
import org.junit.Test

class PerfilYCostosTest {
    @Test fun perfilMinimoSinDatosSensibles() {
        val cliente = UserProfile(nombre = "  CLIENTE ")
        assertEquals(listOf(PerfilCompleto.Campo.NOMBRE), PerfilCompleto.pendientes(cliente))
        assertEquals(0, PerfilCompleto.porcentaje(cliente))
        val clienteSinEmpresa = UserProfile(nombre = "Ana", declaraEmpresa = false)
        assertEquals(100, PerfilCompleto.porcentaje(clienteSinEmpresa))
        val pastelera = UserProfile(nombre = "Ana", tipoNegocio = "Pastelería")
        assertEquals(listOf(PerfilCompleto.Campo.EMPRESA), PerfilCompleto.pendientes(pastelera))
        assertEquals(67, PerfilCompleto.porcentaje(pastelera))
        assertEquals(100, PerfilCompleto.porcentaje(pastelera.copy(nombreEmpresa = "Pastelería Ana")))
    }

    @Test fun interesesMultiplesYOpcionesAntiguas() {
        assertEquals(setOf("Galletas", "Heladería"), PerfilCompleto.intereses("Galletas, Heladería, Galletas, "))
        assertTrue(PerfilCompleto.coincide("pasteleria de vitrina", "Pastelería"))
        assertTrue(PerfilCompleto.coincide("CHOCOLATERÍA", "chocolateria"))
        assertFalse(PerfilCompleto.coincide("Heladería", "Bollería"))
        assertFalse(PerfilCompleto.coincide("", "Bollería"))
    }

    @Test fun escalaIngredientesConUnidadesYConservaCondimentos() {
        assertEquals("300 g de harina", CostosReceta.escalar("200 g de harina", 4, 6))
        assertEquals("0,5 huevo", CostosReceta.escalar("1 huevo", 12, 6))
        assertEquals("Sal a gusto", CostosReceta.escalar("Sal a gusto", 4, 8))
        assertEquals("200 g de harina", CostosReceta.escalar("200 g de harina", 4, 4))
    }

    @Test fun calculaPrecioConMargenSobreVenta() {
        val resultado = requireNotNull(CostosReceta.calcular("7200", "800", "20", 10))
        assertEquals("8000", resultado.lote.toPlainString())
        assertEquals("800.00", resultado.porcion.toPlainString())
        assertEquals("1000", resultado.precio.toPlainString())
        assertEquals("67", CostosReceta.calcular("100", "", "25", 2)?.precio?.toPlainString())
    }

    @Test fun rechazaCostosNegativosMargenCienYPorcionesCero() {
        assertNull(CostosReceta.calcular("-1", "", "0", 1))
        assertNull(CostosReceta.calcular("100", "-10", "0", 1))
        assertNull(CostosReceta.calcular("100", "", "100", 1))
        assertNull(CostosReceta.calcular("100", "", "-1", 1))
        assertNull(CostosReceta.calcular("100", "", "20", 0))
        assertNull(CostosReceta.calcular("abc", "", "0", 1))
    }
}
