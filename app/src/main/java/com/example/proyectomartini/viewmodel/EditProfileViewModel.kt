
package com.example.proyectomartini.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import com.example.proyectomartini.data.remote.PerfilRemoto
import com.example.proyectomartini.data.repository.ServiciosRepository
import com.example.proyectomartini.utils.ValidadorFormulario
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
    val nombreEmpresa: String = "",
    val direccionEmpresa: String = "",
    val cargoEmpresa: String = "",
    val descripcionEmpresa: String = "",
    val tamanoEmpresa: String = "",
    val rutEmpresa: String = "",
    val tieneEmpresa: Boolean? = null,
    val empresaError: String? = null,
    val rutError: String? = null,
    val nombreError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null,
    val negocioError: String? = null,
    val interesesError: String? = null,
    val guardadoExitoso: Boolean = false,
    val cargando: Boolean = false,
    val errorServidor: String? = null
)

class EditProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ProfileRepository(application)
    private val remoto = ServiciosRepository(application)
    private val perfil = repository.obtenerPerfil()

    private val _uiState = MutableStateFlow(
        EditProfileUiState(
            nombre = perfil.nombre,
            correo = perfil.correo,
            telefono = perfil.telefono,
            tipoNegocio = perfil.tipoNegocio,
            intereses = perfil.intereses,
            foto = perfil.foto,
            nombreEmpresa = perfil.nombreEmpresa,
            direccionEmpresa = perfil.direccionEmpresa,
            cargoEmpresa = perfil.cargoEmpresa,
            descripcionEmpresa = perfil.descripcionEmpresa,
            tamanoEmpresa = perfil.tamanoEmpresa,
            rutEmpresa = perfil.rutEmpresa,
            tieneEmpresa = perfil.declaraEmpresa ?: true.takeIf { perfil.nombreEmpresa.isNotBlank() || perfil.tipoNegocio.isNotBlank() }
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

    fun agregarInteres(valor: String) {
        val nuevo = valor.trim()
        if (nuevo.isBlank() || nuevo.contains(",")) return

        val actuales = _uiState.value.intereses
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (actuales.any { it.equals(nuevo, ignoreCase = true) }) return

        cambiarIntereses((actuales + nuevo).joinToString(", "))
    }

    fun eliminarInteres(valor: String) {
        val restantes = _uiState.value.intereses
            .split(",")
            .map { it.trim() }
            .filter {
                it.isNotEmpty() && !it.equals(valor, ignoreCase = true)
            }

        cambiarIntereses(restantes.joinToString(", "))
    }

    fun cambiarFoto(ruta: String) {
        _uiState.update {
            it.copy(foto = ruta, guardadoExitoso = false)
        }
    }

    fun elegirEmpresa(valor: Boolean) {
        _uiState.update { it.copy(tieneEmpresa = valor, empresaError = null, negocioError = null, rutError = null, guardadoExitoso = false) }
    }

    fun cambiarNombreEmpresa(valor: String) {
        _uiState.update {
            it.copy(nombreEmpresa = valor, empresaError = null, guardadoExitoso = false)
        }
    }

    fun cambiarDireccionEmpresa(valor: String) {
        _uiState.update {
            it.copy(direccionEmpresa = valor, guardadoExitoso = false)
        }
    }

    fun cambiarCargoEmpresa(valor: String) {
        _uiState.update {
            it.copy(cargoEmpresa = valor, guardadoExitoso = false)
        }
    }

    fun cambiarDescripcionEmpresa(valor: String) {
        _uiState.update {
            it.copy(descripcionEmpresa = valor, guardadoExitoso = false)
        }
    }

    fun cambiarTamanoEmpresa(valor: String) {
        if (valor.length <= 80) _uiState.update { it.copy(tamanoEmpresa = valor) }
    }

    fun cambiarRutEmpresa(valor: String) {
        if (valor.length <= 30) _uiState.update { it.copy(rutEmpresa = valor, rutError = null, guardadoExitoso = false) }
    }

    private fun rutValido(valor: String): Boolean {
        val limpio = valor.uppercase().replace(Regex("[. -]"), "")
        if (!Regex("[0-9]{7,8}[0-9K]").matches(limpio)) return false
        val total = limpio.dropLast(1).reversed().mapIndexed { i, n -> n.digitToInt() * (i % 6 + 2) }.sum()
        val digito = (11 - total % 11) % 11
        return limpio.last() == if (digito == 10) 'K' else digito.digitToChar()
    }

    fun guardarPerfil(): Boolean {
        if (_uiState.value.cargando) return false
        val estado = _uiState.value

        val nombreError = if (estado.nombre.trim().length < 2) {
            "Ingresa un nombre válido"
        } else null

        val correoError = if (estado.correo.trim() != perfil.correo) "El correo de acceso no se puede cambiar aquí" else ValidadorFormulario.validarCorreo(estado.correo)

        val telefonoError = if (
            estado.telefono.isNotBlank() && !Regex("""^\+?[0-9]{8,15}$""")
                .matches(estado.telefono.trim().replace(" ", ""))
        ) {
            "Ingresa un teléfono válido"
        } else null

        val empresa = estado.tieneEmpresa == true
        val negocioError = "Selecciona el tipo de negocio".takeIf { empresa && estado.tipoNegocio.isBlank() }
        val empresaError = "Ingresa el nombre de la empresa".takeIf { empresa && estado.nombreEmpresa.isBlank() }
        val rutError = "Revisa el RUT de la empresa".takeIf {
            empresa && estado.rutEmpresa.isNotBlank() && !rutValido(estado.rutEmpresa)
        }

        val interesesError = "Selecciona menos intereses".takeIf { estado.intereses.length > 255 }

        val valido = listOf(
            nombreError,
            correoError,
            telefonoError,
            negocioError,
            empresaError,
            rutError,
            interesesError
        ).all { it == null }

        _uiState.update {
            it.copy(
                nombreError = nombreError,
                correoError = correoError,
                telefonoError = telefonoError,
                negocioError = negocioError,
                empresaError = empresaError,
                rutError = rutError,
                interesesError = interesesError,
                guardadoExitoso = false,
                errorServidor = null
            )
        }

        if (valido) {
            val nuevo = UserProfile(
                    nombre = estado.nombre.trim(),
                    correo = estado.correo.trim(),
                    telefono = estado.telefono.trim(),
                    tipoNegocio = estado.tipoNegocio.trim().takeIf { empresa }.orEmpty(),
                    intereses = estado.intereses.trim(),
                    foto = estado.foto,
                    nombreEmpresa = estado.nombreEmpresa.trim().takeIf { empresa }.orEmpty(),
                    direccionEmpresa = estado.direccionEmpresa.trim().takeIf { empresa }.orEmpty(),
                    cargoEmpresa = estado.cargoEmpresa.trim(),
                    descripcionEmpresa = estado.descripcionEmpresa.trim(),
                    tamanoEmpresa = estado.tamanoEmpresa.trim(),
                    rutEmpresa = estado.rutEmpresa.trim().takeIf { empresa }.orEmpty(),
                    declaraEmpresa = estado.tieneEmpresa
                )
            _uiState.update { it.copy(cargando = true) }
            viewModelScope.launch {
                try {
                    remoto.api.editar(PerfilRemoto(nombre=nuevo.nombre, correo=nuevo.correo, telefono=nuevo.telefono, tipoNegocio=nuevo.tipoNegocio, intereses=nuevo.intereses, nombreEmpresa=nuevo.nombreEmpresa, direccionEmpresa=nuevo.direccionEmpresa, cargoEmpresa=nuevo.cargoEmpresa, descripcionEmpresa=nuevo.descripcionEmpresa, tamanoEmpresa=nuevo.tamanoEmpresa, rutEmpresa=nuevo.rutEmpresa))
                    repository.guardarPerfil(nuevo)
                    _uiState.update { it.copy(guardadoExitoso=true) }
                } catch(e: CancellationException) { throw e }
                catch(e: Exception) { _uiState.update { it.copy(errorServidor="No se pudo guardar en el servidor. Revisa la conexión y vuelve a intentar.") } }
                finally { _uiState.update { it.copy(cargando=false) } }
            }
        }

        return valido
    }
}
