package com.example.proyectomartini.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val correoError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val loginExitoso: Boolean = false
)

class LoginViewModel(
    application: android.app.Application
) : androidx.lifecycle.AndroidViewModel(application) {

    private val authRepository =
        com.example.proyectomartini.data.repository.AuthRepository(
            application
        )

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onCorreoChange(valor: String) {
        _uiState.update {
            it.copy(
                correo = valor,
                correoError = null,
                loginError = null
            )
        }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update {
            it.copy(
                password = valor,
                passwordError = null,
                loginError = null
            )
        }
    }

    fun iniciarSesion() {

        val estado = _uiState.value

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

        val passwordError =
            when {
                estado.password.isBlank() ->
                    "Ingresa tu contraseña"

                estado.password.length < 6 ->
                    "La contraseña debe tener al menos 6 caracteres"

                else -> null
            }

        if (correoError != null || passwordError != null) {

            _uiState.update {
                it.copy(
                    correoError = correoError,
                    passwordError = passwordError,
                    loginExitoso = false
                )
            }

            return
        }

        val credencialesCorrectas =
            authRepository.validarCredenciales(
                correo = estado.correo,
                password = estado.password
            )

        _uiState.update {
            it.copy(
                loginExitoso = credencialesCorrectas,
                loginError =
                    if (!credencialesCorrectas)
                        "Correo o contraseña incorrectos"
                    else null
            )
        }
    }

    fun consumirLoginExitoso() {
        _uiState.update {
            it.copy(loginExitoso = false)
        }
    }
}

