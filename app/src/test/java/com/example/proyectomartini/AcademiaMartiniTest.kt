package com.example.proyectomartini
import com.example.proyectomartini.utils.AcademiaMartini as A
import com.example.proyectomartini.ui.screens.tutorialesMartini
import org.junit.Assert.*
import org.junit.Test
class AcademiaMartiniTest {
    @Test fun primerNombre() { assertEquals("Ana", A.primerNombre(" Ana Torres ")); assertEquals("", A.primerNombre(" ")) }
    @Test fun avancePorCurso() { assertEquals(1, A.contarLecciones("c",3,setOf("c:0","otro:0","c:4"))) }
    @Test fun continuar() { assertEquals(1, A.siguienteLeccion("c",3,setOf("c:0","c:2"))); assertNull(A.siguienteLeccion("c",1,setOf("c:0"))) }
    @Test fun nombreNoEsRol() { assertEquals("Ana",A.nombreVisible("CLIENTE","Ana Torres")); assertEquals("",A.nombreVisible("CLIENTE","ADMIN")); assertEquals("Luisa",A.nombreVisible("Luisa Pérez","Ana Torres")) }
    @Test fun medallaSoloAlFinal() { assertFalse(A.cursoCompleto("c",3,setOf("c:0","c:1","otro:2"))); assertTrue(A.cursoCompleto("c",3,setOf("c:0","c:1","c:2"))); assertFalse(A.cursoCompleto("c",0,emptySet())) }
    @Test fun catalogo() {
        val ids = tutorialesMartini.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertTrue(ids.containsAll(listOf("chocolate", "pastel", "helado", "gratin-bravo")))
        assertTrue(tutorialesMartini.all { it.pasos.size == 3 && it.producto.isNotBlank() })
    }
}
