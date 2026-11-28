package com.example.proyectomartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomartini.data.remote.PerfilRemoto
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.data.repository.ResultadoAuth
import com.example.proyectomartini.utils.ValidadorFormulario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val paso: Int = 0,
    val perfil: PerfilRemoto = PerfilRemoto(),
    val password: String = "",
    val confirmacion: String = "",
    val errores: Map<String, String> = emptyMap(),
    val mensaje: String? = null,
    val terminado: Boolean = false,
    val cargando: Boolean = false
)

class RegisterViewModel internal constructor(
    application: Application,
    private val auth: AuthRepository
) : AndroidViewModel(application) {
    constructor(application: Application) : this(application, AuthRepository(application))
    private val state = MutableStateFlow(RegisterUiState())
    val uiState = state.asStateFlow()

    fun modificar(cambio: (RegisterUiState) -> RegisterUiState) {
        state.update { cambio(it).copy(errores = emptyMap(), mensaje = null) }
    }

    fun volver() = modificar { it.copy(paso = (it.paso - 1).coerceAtLeast(0)) }

    fun omitirIntereses() {
        if (state.value.paso != 1 || state.value.cargando) return
        modificar { it.copy(perfil = it.perfil.copy(intereses = "")) }
        continuar()
    }

    fun continuar() {
        val actual = state.value
        if (actual.cargando || actual.terminado) return
        if (actual.paso == 0) {
            val perfil = actual.perfil
            val errores = listOf(
                "nombre" to ValidadorFormulario.validarNombre(perfil.nombre),
                "correo" to ValidadorFormulario.validarCorreo(perfil.correo.trim()),
                "password" to ValidadorFormulario.validarPasswordRegistro(actual.password),
                "confirmacion" to ValidadorFormulario.validarConfirmarPassword(actual.password, actual.confirmacion)
            ).mapNotNull { (campo, error) -> error?.let { campo to it } }.toMap()
            if (errores.isNotEmpty()) {
                state.update { it.copy(errores = errores) }
                return
            }
        }
        if (actual.paso == 0) {
            modificar { it.copy(paso = it.paso + 1) }
            return
        }
        state.update { it.copy(cargando = true, mensaje = null) }
        viewModelScope.launch {
            try {
                val p = actual.perfil
                when (val resultado = auth.registrar(p.nombre, p.correo, actual.password, p)) {
                    is ResultadoAuth.Exito -> {
                        auth.cerrarSesion()
                        state.update { it.copy(password = "", confirmacion = "", terminado = true) }
                    }
                    is ResultadoAuth.Error -> state.update { it.copy(mensaje = resultado.mensaje) }
                }
            } finally {
                state.update { it.copy(cargando = false) }
            }
        }
    }
}
