package com.example.proyectomartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.data.repository.ProfileRepository
import com.example.proyectomartini.data.repository.ResultadoAuth
import com.example.proyectomartini.data.repository.UserProfile
import com.example.proyectomartini.utils.ValidadorFormulario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val correoError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val loginExitoso: Boolean = false,
    val cargando: Boolean = false
)

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)
    private val profileRepository = ProfileRepository(application)

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

        if (estado.cargando) return

        // Limpiar el correo electrónico
        val correoLimpio =
            ValidadorFormulario.limpiarCorreo(estado.correo)

        // Utilizar las validaciones centralizadas
        val correoError =
            ValidadorFormulario.validarCorreo(correoLimpio)

        val passwordError =
            ValidadorFormulario.validarPasswordLogin(
                estado.password
            )

        // Comprobar errores del formulario
        if (correoError != null || passwordError != null) {

            _uiState.update {
                it.copy(
                    correo = correoLimpio,
                    correoError = correoError,
                    passwordError = passwordError,
                    loginExitoso = false
                )
            }

            return
        }

        // Activar estado de carga
        _uiState.update {
            it.copy(
                correo = correoLimpio,
                cargando = true,
                loginError = null,
                loginExitoso = false
            )
        }

        // Consultar al backend
        viewModelScope.launch {

            try {

                when (
                    val resultado = authRepository.iniciarSesion(
                        correo = correoLimpio,
                        password = estado.password
                    )
                ) {

                    is ResultadoAuth.Exito -> {

                        val perfil =
                            profileRepository.obtenerPerfil()

                        // Actualizar perfil si pertenece a otra cuenta
                        if (perfil.correo != resultado.correo) {

                            profileRepository.guardarPerfil(
                                UserProfile(
                                    nombre = resultado.nombre,
                                    correo = resultado.correo
                                )
                            )
                        }

                        _uiState.update {
                            it.copy(
                                loginExitoso = true
                            )
                        }
                    }

                    is ResultadoAuth.Error -> {

                        _uiState.update {
                            it.copy(
                                loginExitoso = false,
                                loginError = resultado.mensaje
                            )
                        }
                    }
                }

            } finally {

                _uiState.update {
                    it.copy(cargando = false)
                }
            }
        }
    }

    fun consumirLoginExitoso() {
        _uiState.update {
            it.copy(loginExitoso = false)
        }
    }
}
