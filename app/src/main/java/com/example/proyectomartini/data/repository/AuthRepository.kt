package com.example.proyectomartini.data.repository

import android.content.Context
import com.example.proyectomartini.data.remote.AuthApi
import com.example.proyectomartini.data.remote.AuthResponse
import com.example.proyectomartini.data.remote.LoginRequest
import com.example.proyectomartini.data.remote.RegistroRequest
import com.example.proyectomartini.data.remote.RetrofitClient
import retrofit2.HttpException
import com.example.proyectomartini.data.remote.PerfilRemoto
import java.io.IOException

sealed interface ResultadoAuth {
    data class Exito(val nombre: String, val correo: String) : ResultadoAuth
    data class Error(val mensaje: String) : ResultadoAuth
}

class AuthRepository(context: Context) {

    private val preferences = context.getSharedPreferences(
        "auth_master_martini",
        Context.MODE_PRIVATE
    )

    private val api: AuthApi = RetrofitClient
        .crear { obtenerToken() }
        .create(AuthApi::class.java)

    suspend fun registrar(
        nombre: String,
        correo: String,
        password: String,
        perfil: PerfilRemoto? = null
    ): ResultadoAuth = llamar {
        api.registrar(
            RegistroRequest(nombre.trim(), correo.trim().lowercase(), password, perfil)
        )
    }

    suspend fun iniciarSesion(
        correo: String,
        password: String
    ): ResultadoAuth = llamar {
        api.iniciarSesion(
            LoginRequest(correo.trim().lowercase(), password)
        )
    }

    fun nombreSesion(): String = preferences.getString("nombre", "").orEmpty()

    fun obtenerToken(): String? = preferences.getString("token", null)

    internal fun tokenDeCuenta(cuenta: String): String? {
        val sesion = preferences.all
        val correo = (sesion["correo"] as? String)?.trim()?.lowercase()
        return (sesion["token"] as? String).takeIf { correo == cuenta }
    }

    fun correoSesion(): String = preferences.getString("correo", "")?.trim()?.lowercase().orEmpty()

    fun rol(): String = preferences.getString("rol", "CLIENTE") ?: "CLIENTE"

    fun haySesion(): Boolean = obtenerToken() != null

    fun cerrarSesion() {
        preferences.edit().clear().apply()
    }

    private suspend fun llamar(accion: suspend () -> AuthResponse): ResultadoAuth {
        return try {
            val respuesta = accion()

            preferences.edit()
                .putString("token", respuesta.token)
                .putString("nombre", respuesta.nombre)
                .putString("correo", respuesta.correo)
                .putString("rol", respuesta.rol)
                .apply()

            ResultadoAuth.Exito(respuesta.nombre, respuesta.correo)

        } catch (e: HttpException) {
            ResultadoAuth.Error(
                when (e.code()) {
                    400 -> "Revisa los datos ingresados"
                    401 -> "Correo o contraseña incorrectos"
                    409 -> "Ya existe una cuenta con ese correo"
                    else -> "El servidor respondió con un error (${e.code()})"
                }
            )
        } catch (e: IOException) {
            ResultadoAuth.Error(
                "No se pudo conectar con el servidor. Revisa tu conexión a internet."
            )
        }
    }
}
