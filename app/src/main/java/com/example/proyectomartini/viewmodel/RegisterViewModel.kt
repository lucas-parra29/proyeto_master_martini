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

    val cargando: Boolean = false,
    val registroError: String? = null,
    val registroExitoso: Boolean = false
)

class RegisterViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authRepository =
        AuthRepository(application)

    private val profileRepository =
        ProfileRepository(application)

    private val _uiState =
        MutableStateFlow(RegisterUiState())

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

        if (estado.cargando) return

        // Eliminar espacios del correo
        val correoLimpio =
            ValidadorFormulario.limpiarCorreo(estado.correo)

        // Validar nombre
        val nombreError =
            ValidadorFormulario.validarNombre(
                estado.nombre
            )

        // Validar correo
        val correoError =
            ValidadorFormulario.validarCorreo(
                correoLimpio
            )

        // Validar teléfono
        val telefonoError =
            ValidadorFormulario.validarTelefono(
                estado.telefono
            )

        // Validar tipo de negocio
        val negocioError =
            ValidadorFormulario.validarTipoNegocio(
                estado.tipoNegocio
            )

        // Validar contraseña
        val passwordError =
            ValidadorFormulario.validarPasswordRegistro(
                estado.password
            )

        // Validar confirmación de contraseña
        val confirmarError =
            ValidadorFormulario.validarConfirmarPassword(
                estado.password,
                estado.confirmarPassword
            )

        // Comprobar si existen errores
        val hayErrores =
            nombreError != null ||
                    correoError != null ||
                    telefonoError != null ||
                    negocioError != null ||
                    passwordError != null ||
                    confirmarError != null

        // Actualizar el formulario
        _uiState.update {
            it.copy(
                correo = correoLimpio,
                nombreError = nombreError,
                correoError = correoError,
                telefonoError = telefonoError,
                tipoNegocioError = negocioError,
                passwordError = passwordError,
                confirmarPasswordError = confirmarError,
                registroExitoso = false,
                registroError = null
            )
        }

        // Detener el registro si existen errores
        if (hayErrores) return

        // Activar estado de carga
        _uiState.update {
            it.copy(cargando = true)
        }

        // Enviar los datos al backend
        viewModelScope.launch {

            try {

                when (
                    val resultado = authRepository.registrar(
                        nombre = estado.nombre,
                        correo = correoLimpio,
                        password = estado.password
                    )
                ) {

                    is ResultadoAuth.Exito -> {

                        // Guardar el perfil local
                        profileRepository.guardarPerfil(
                            UserProfile(
                                nombre = resultado.nombre,
                                correo = resultado.correo,
                                telefono = estado.telefono,
                                tipoNegocio = estado.tipoNegocio
                            )
                        )

                        _uiState.update {
                            it.copy(
                                registroExitoso = true
                            )
                        }
                    }

                    is ResultadoAuth.Error -> {

                        _uiState.update {
                            it.copy(
                                registroError = resultado.mensaje
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
}
