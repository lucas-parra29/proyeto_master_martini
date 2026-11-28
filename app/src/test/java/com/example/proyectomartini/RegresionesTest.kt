package com.example.proyectomartini

import com.example.proyectomartini.utils.*
import org.junit.Assert.*
import org.junit.Test

class RegresionesTest {
    @Test fun contadorReal() {
        assertEquals(0, EstadisticasFavoritos.contarRecetas(emptyList()))
        assertEquals(1, EstadisticasFavoritos.contarRecetas(listOf("RECETA", "VIDEO")))
        assertEquals(0, EstadisticasFavoritos.contarRecetas(listOf("VIDEO")))
        assertEquals(2, EstadisticasFavoritos.contarRecetas(listOf("RECETA", "RECETA")))
    }
    @Test fun almacenamientoSeparado() {
        assertEquals(IdentidadLocal.clave(" Chef@martini.cl "), IdentidadLocal.clave("chef@martini.cl"))
        assertNotEquals(IdentidadLocal.clave("a@martini.cl"), IdentidadLocal.clave("b@martini.cl"))
    }
    @Test fun videoSeguro() {
        assertTrue(VideoMartini.idValido("VglM0C55oZ0"))
        assertFalse(VideoMartini.idValido("https://bad.com"))
        assertTrue(VideoMartini.navegacionPermitida(VideoMartini.pagina("VglM0C55oZ0")))
        assertFalse(VideoMartini.navegacionPermitida("https://youtube.com.example.org"))
        assertFalse(VideoMartini.navegacionPermitida("intent://youtube.com"))
    }
}
