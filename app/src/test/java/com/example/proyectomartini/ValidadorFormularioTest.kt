package com.example.proyectomartini

import com.example.proyectomartini.utils.ValidadorFormulario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorFormularioTest {

    @Test
    fun correoValido() {

        val resultado = ValidadorFormulario.validarCorreo(
            "prueba@example.com"
        )

        assertNull(resultado)
    }

    @Test
    fun correoInvalido() {

        val resultado = ValidadorFormulario.validarCorreo(
            "correoIncorrecto"
        )

        assertEquals(
            "Ingresa un correo válido",
            resultado
        )
    }

    @Test
    fun correoVacio() {

        val resultado = ValidadorFormulario.validarCorreo("")

        assertEquals(
            "Ingresa tu correo",
            resultado
        )
    }

    @Test
    fun correoConEspacios() {

        val correo = " prueba@example.com "

        val resultado = ValidadorFormulario.limpiarCorreo(
            correo
        )

        assertEquals(
            "prueba@example.com",
            resultado
        )

        assertNull(
            ValidadorFormulario.validarCorreo(correo)
        )
    }

    @Test
    fun passwordValida() {

        val resultado =
            ValidadorFormulario.validarPasswordRegistro(
                "Abcd1234"
            )

        assertNull(resultado)
    }

    @Test
    fun passwordCorta() {

        val resultado =
            ValidadorFormulario.validarPasswordRegistro(
                "Abc1234"
            )

        assertEquals(
            "Debe tener al menos 8 caracteres",
            resultado
        )
    }

    @Test
    fun passwordVacia() {

        val resultado =
            ValidadorFormulario.validarPasswordRegistro("")

        assertEquals(
            "Ingresa una contraseña",
            resultado
        )
    }

    @Test
    fun passwordsIguales() {

        val resultado =
            ValidadorFormulario.validarConfirmarPassword(
                "Martini123",
                "Martini123"
            )

        assertNull(resultado)
    }

    @Test
    fun passwordsDiferentes() {

        val resultado =
            ValidadorFormulario.validarConfirmarPassword(
                "Martini123",
                "Martini456"
            )

        assertEquals(
            "Las contraseñas no coinciden",
            resultado
        )
    }

    @Test
    fun nombreVacio() {

        val resultado =
            ValidadorFormulario.validarNombre("")

        assertEquals(
            "Ingresa tu nombre",
            resultado
        )
    }

    @Test
    fun telefonoCorto() {

        val resultado =
            ValidadorFormulario.validarTelefono("12345")

        assertEquals(
            "El teléfono es demasiado corto",
            resultado
        )
    }

    @Test
    fun tipoNegocioVacio() {

        val resultado =
            ValidadorFormulario.validarTipoNegocio("")

        assertEquals(
            "Ingresa tu tipo de negocio",
            resultado
        )
    }
}
