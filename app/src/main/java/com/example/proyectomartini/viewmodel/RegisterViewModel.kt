package com.example.proyectomartini.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val tipoNegocio: String = "",
    val password: String = "",
    val confirmarPassword: String = "",

    val nombreError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null,
    val tipoNegocioError: String? = null,
    val passwordError: String? = null,
    val confirmarPasswordError: String? = null,

    val registroExitoso: Boolean = false
)

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> =
        _uiState.asStateFlow()

    fun onNombreChange(valor: String) {
        _uiState.update {
            it.copy(
                nombre = valor,
                nombreError = null
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _uiState.update {
            it.copy(
                correo = valor,
                correoError = null
            )
        }
    }

    fun onTelefonoChange(valor: String) {
        _uiState.update {
            it.copy(
                telefono = valor,
                telefonoError = null
            )
        }
    }

    fun onTipoNegocioChange(valor: String) {
        _uiState.update {
            it.copy(
                tipoNegocio = valor,
                tipoNegocioError = null
            )
        }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update {
            it.copy(
                password = valor,
                passwordError = null
            )
        }
    }

    fun onConfirmarPasswordChange(valor: String) {
        _uiState.update {
            it.copy(
                confirmarPassword = valor,
                confirmarPasswordError = null
            )
        }
    }

    fun registrar() {

        val estado = _uiState.value

        val nombreError =
            if (estado.nombre.isBlank())
                "Ingresa tu nombre"
            else null

        val correoError =
            when {
                estado.correo.isBlank() ->
                    "Ingresa tu correo"

                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(estado.correo)
                    .matches() ->
                    "Ingresa un correo válido"

                else -> null
            }

        val telefonoError =
            when {
                estado.telefono.isBlank() ->
                    "Ingresa tu teléfono"

                estado.telefono.length < 8 ->
                    "El teléfono es demasiado corto"

                else -> null
            }

        val negocioError =
            if (estado.tipoNegocio.isBlank())
                "Ingresa tu tipo de negocio"
            else null

        val passwordError =
            when {
                estado.password.isBlank() ->
                    "Ingresa una contraseña"

                estado.password.length < 6 ->
                    "Debe tener al menos 6 caracteres"

                else -> null
            }

        val confirmarError =
            when {
                estado.confirmarPassword.isBlank() ->
                    "Confirma tu contraseña"

                estado.password != estado.confirmarPassword ->
                    "Las contraseñas no coinciden"

                else -> null
            }

        val hayErrores =
            nombreError != null ||
                    correoError != null ||
                    telefonoError != null ||
                    negocioError != null ||
                    passwordError != null ||
                    confirmarError != null

        _uiState.update {
            it.copy(
                nombreError = nombreError,
                correoError = correoError,
                telefonoError = telefonoError,
                tipoNegocioError = negocioError,
                passwordError = passwordError,
                confirmarPasswordError = confirmarError,
                registroExitoso = !hayErrores
            )
        }
    }
}
