package com.example.proyectomartini.utils
object ValidadorFormulario {

    fun limpiarCorreo(correo: String): String {
        return correo.trim()
    }

    fun validarNombre(nombre: String): String? {
        return if (nombre.isBlank()) {
            "Ingresa tu nombre"
        } else {
            null
        }
    }

    fun validarCorreo(correo: String): String? {

        val correoLimpio = limpiarCorreo(correo)

        val patron = Regex(
            "^[A-Za-z0-9+_.%-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$"
        )

        return when {
            correoLimpio.isBlank() ->
                "Ingresa tu correo"

            !patron.matches(correoLimpio) ->
                "Ingresa un correo válido"

            else -> null
        }
    }

    fun validarTelefono(telefono: String): String? {
        return when {
            telefono.isBlank() ->
                "Ingresa tu teléfono"

            telefono.length < 8 ->
                "El teléfono es demasiado corto"

            else -> null
        }
    }

    fun validarTipoNegocio(tipoNegocio: String): String? {
        return if (tipoNegocio.isBlank()) {
            "Ingresa tu tipo de negocio"
        } else {
            null
        }
    }

    fun validarPasswordRegistro(password: String): String? {
        return when {
            password.isBlank() ->
                "Ingresa una contraseña"

            password.length < 8 ->
                "Debe tener al menos 8 caracteres"

            else -> null
        }
    }

    fun validarPasswordLogin(password: String): String? {
        return when {
            password.isBlank() ->
                "Ingresa tu contraseña"

            password.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"

            else -> null
        }
    }

    fun validarConfirmarPassword(
        password: String,
        confirmarPassword: String
    ): String? {

        return when {
            confirmarPassword.isBlank() ->
                "Confirma tu contraseña"

            password != confirmarPassword ->
                "Las contraseñas no coinciden"

            else -> null
        }
    }
}
