package com.example.proyectomartini
import com.example.proyectomartini.utils.ValidadorFormulario as V
import com.example.proyectomartini.utils.Permisos as P
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
class LogicaEftTest {
    @Test fun caso01() { assertTrue("Correo válido", V.validarCorreo("demo@martini.cl") == null) }
    @Test fun caso02() { assertTrue("Correo vacío", V.validarCorreo("") != null) }
    @Test fun caso03() { assertTrue("Correo inválido", V.validarCorreo("incorrecto") != null) }
    @Test fun caso04() { assertTrue("Correo con espacios", V.limpiarCorreo(" demo@martini.cl ") == "demo@martini.cl") }
    @Test fun caso05() { assertTrue("Nombre vacío", V.validarNombre(" ") != null) }
    @Test fun caso06() { assertTrue("Nombre válido", V.validarNombre("Daniel") == null) }
    @Test fun caso07() { assertTrue("Teléfono vacío", V.validarTelefono("") != null) }
    @Test fun caso08() { assertTrue("Teléfono corto", V.validarTelefono("123") != null) }
    @Test fun caso09() { assertTrue("Teléfono no numérico", V.validarTelefono("abcdefgh") != null) }
    @Test fun caso10() { assertTrue("Teléfono válido", V.validarTelefono("+56 912345678") == null) }
    @Test fun caso11() { assertTrue("Negocio vacío", V.validarTipoNegocio("") != null) }
    @Test fun caso12() { assertTrue("Negocio válido", V.validarTipoNegocio("Pastelería") == null) }
    @Test fun caso13() { assertTrue("Registro: contraseña vacía", V.validarPasswordRegistro("") != null) }
    @Test fun caso14() { assertTrue("Registro: contraseña corta", V.validarPasswordRegistro("1234567") != null) }
    @Test fun caso15() { assertTrue("Registro: contraseña válida", V.validarPasswordRegistro("12345678") == null) }
    @Test fun caso16() { assertTrue("Login: contraseña vacía", V.validarPasswordLogin("") != null) }
    @Test fun caso17() { assertTrue("Login: contraseña corta", V.validarPasswordLogin("12345") != null) }
    @Test fun caso18() { assertTrue("Login: contraseña válida", V.validarPasswordLogin("123456") == null) }
    @Test fun caso19() { assertTrue("Confirmación vacía", V.validarConfirmarPassword("12345678", "") != null) }
    @Test fun caso20() { assertTrue("Confirmación diferente", V.validarConfirmarPassword("12345678", "87654321") != null) }
    @Test fun caso21() { assertTrue("Confirmación correcta", V.validarConfirmarPassword("12345678", "12345678") == null) }
    @Test fun caso22() { assertTrue("Cliente sin edición", !P.puedeEditar("CLIENTE", "RECETA") && !P.puedeEditar("CLIENTE", "CURSO")) }
    @Test fun caso23() { assertTrue("Editor solo recetas", P.puedeEditar("EDITOR", "RECETA") && !P.puedeEditar("EDITOR", "CURSO")) }
    @Test fun caso24() {
        assertTrue(P.puedeEditar("INSTRUCTOR", "CURSO"))
        assertTrue(P.puedeEditar("INSTRUCTOR", "RECETA"))
        assertTrue(P.puedeEditar("INSTRUCTOR", "LECCION"))
        assertFalse(P.puedeEditar("INSTRUCTOR", "COMUNICADO"))
    }
    @Test fun caso25() { assertTrue("Admin ambas categorías", P.puedeEditar("ADMIN", "RECETA") && P.puedeEditar("ADMIN", "CURSO")) }
    @Test fun caso26() {
        assertFalse("Tipo desconocido denegado", P.puedeEditar("ADMIN", "DESCONOCIDO"))
        assertTrue(P.puedeEditar("ADMIN", "VIDEO"))
        assertTrue(P.puedeEditar("EDITOR", "COMUNICADO"))
    }
    @Test fun caso27() { assertTrue("Administración exclusiva", P.roles.all { P.puedeAdministrar(it) == (it == "ADMIN") }) }
    @Test fun caso28() { assertTrue("Título vacío", P.validarContenido("", "Texto") != null) }
    @Test fun caso29() { assertTrue("Título demasiado largo", P.validarContenido("x".repeat(201), "Texto") != null) }
    @Test fun caso30() { assertTrue("Descripción vacía", P.validarContenido("Tarta", "") != null) }
    @Test fun caso31() { assertTrue("Descripción demasiado larga", P.validarContenido("Tarta", "x".repeat(2001)) != null) }
    @Test fun caso32() { assertTrue("Contenido válido", P.validarContenido("Tarta", "Preparación de chocolate") == null) }
}
