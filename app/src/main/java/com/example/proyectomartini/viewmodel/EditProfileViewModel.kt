package com.example.proyectomartini.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.proyectomartini.data.repository.ProfileRepository
import com.example.proyectomartini.data.repository.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EditProfileUiState(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val tipoNegocio: String = "",
    val intereses: String = "",
    val foto: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null,
    val negocioError: String? = null,
    val interesesError: String? = null,
    val guardadoExitoso: Boolean = false
)

class EditProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ProfileRepository(application)

    private val perfil = repository.obtenerPerfil()

    private val _uiState = MutableStateFlow(
        EditProfileUiState(
            nombre = perfil.nombre,
            correo = perfil.correo,
            telefono = perfil.telefono,
            tipoNegocio = perfil.tipoNegocio,
            intereses = perfil.intereses,
            foto = perfil.foto
        )
    )

    val uiState = _uiState.asStateFlow()

    fun cambiarNombre(valor: String) {
        _uiState.update {
            it.copy(
                nombre = valor,
                nombreError = null,
                guardadoExitoso = false
            )
        }
    }

    fun cambiarCorreo(valor: String) {
        _uiState.update {
            it.copy(
                correo = valor,
                correoError = null,
                guardadoExitoso = false
            )
        }
    }

    fun cambiarTelefono(valor: String) {
        _uiState.update {
            it.copy(
                telefono = valor,
                telefonoError = null,
                guardadoExitoso = false
            )
        }
    }

    fun cambiarNegocio(valor: String) {
        _uiState.update {
            it.copy(
                tipoNegocio = valor,
                negocioError = null,
                guardadoExitoso = false
            )
        }
    }

    fun cambiarIntereses(valor: String) {
        _uiState.update {
            it.copy(
                intereses = valor,
                interesesError = null,
                guardadoExitoso = false
            )
        }
    }

    fun cambiarFoto(ruta: String) {
        _uiState.update {
            it.copy(
                foto = ruta,
                guardadoExitoso = false
            )
        }
    }

    fun guardarPerfil(): Boolean {
        val estado = _uiState.value

        val nombreError = if (estado.nombre.trim().length < 2) {
            "Ingresa un nombre válido"
        } else null

        val correoError = if (
            !Patterns.EMAIL_ADDRESS.matcher(
                estado.correo.trim()
            ).matches()
        ) {
            "Ingresa un correo válido"
        } else null

        val telefonoError = if (
            !Regex("""^\+?[0-9]{8,15}$""").matches(
                estado.telefono.trim().replace(" ", "")
            )
        ) {
            "Ingresa un teléfono válido"
        } else null

        val negocioError = if (estado.tipoNegocio.isBlank()) {
            "Ingresa tu tipo de negocio"
        } else null

        val interesesError = if (estado.intereses.isBlank()) {
            "Ingresa tus intereses"
        } else null

        val valido = listOf(
            nombreError,
            correoError,
            telefonoError,
            negocioError,
            interesesError
        ).all { it == null }

        _uiState.update {
            it.copy(
                nombreError = nombreError,
                correoError = correoError,
                telefonoError = telefonoError,
                negocioError = negocioError,
                interesesError = interesesError,
                guardadoExitoso = valido
            )
        }

        if (valido) {
            repository.guardarPerfil(
                UserProfile(
                    nombre = estado.nombre.trim(),
                    correo = estado.correo.trim(),
                    telefono = estado.telefono.trim(),
                    tipoNegocio = estado.tipoNegocio.trim(),
                    intereses = estado.intereses.trim(),
                    foto = estado.foto
                )
            )
        }

        return valido
    }
}
