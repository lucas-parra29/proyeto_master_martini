package com.example.proyectomartini.data.repository

import android.content.Context
import com.example.proyectomartini.data.remote.AvanceRemoto
import com.example.proyectomartini.data.remote.ConstanciaRemota
import com.example.proyectomartini.data.remote.EvaluacionRemota
import com.example.proyectomartini.data.remote.RetrofitClient
import com.example.proyectomartini.data.remote.ServiciosApi
import com.example.proyectomartini.utils.IdentidadLocal

class AprendizajeRepository(context: Context) {
    private val auth = AuthRepository(context)
    private val cuenta = auth.correoSesion()
    private val api = RetrofitClient.crear { auth.tokenDeCuenta(cuenta) }.create(ServiciosApi::class.java)
    private val prefs = context.getSharedPreferences("academia_" + IdentidadLocal.clave(cuenta), Context.MODE_PRIVATE)

    fun leer(): Set<String> = prefs.getStringSet("lecciones", emptySet()).orEmpty().toSet()

    fun guardar(lecciones: Set<String>): Set<String> {
        val acumuladas = leer() + lecciones
        prefs.edit().putStringSet("lecciones", acumuladas).apply()
        return acumuladas
    }

    suspend fun sincronizar(): Set<String> {
        val locales = leer()
        if (cuenta.isBlank() || auth.correoSesion() != cuenta) return locales
        val remoto = api.sincronizarAvance(AvanceRemoto(locales))
        val acumuladas = guardar(remoto.lecciones)
        prefs.all.keys.filter { it.startsWith(EVALUACION_PENDIENTE) }.forEach { clave ->
            val curso = clave.removePrefix(EVALUACION_PENDIENTE)
            val respuestas = prefs.getString(clave, null)?.split(',')?.mapNotNull(String::toIntOrNull)
            try {
                conservarConstancia(curso, api.constancia(curso, EvaluacionRemota(respuestas)))
            } catch (error: retrofit2.HttpException) {
                if (error.code() != 422) throw error
            }
        }
        return acumuladas
    }

    fun guardarEvaluacion(curso: String, respuestas: List<Int>) {
        prefs.edit().putString(EVALUACION_PENDIENTE + curso, respuestas.joinToString(",")).apply()
    }

    fun constanciaGuardada(curso: String): ConstanciaRemota? {
        val nombre = prefs.getString("constancia_nombre_$curso", null) ?: return null
        return ConstanciaRemota(nombre, prefs.getLong("constancia_fecha_$curso", 0))
    }

    suspend fun obtenerConstancia(curso: String): ConstanciaRemota {
        constanciaGuardada(curso)?.let { return it }
        sincronizar()
        return constanciaGuardada(curso) ?: api.constancia(curso, EvaluacionRemota()).also {
            conservarConstancia(curso, it)
        }
    }

    private fun conservarConstancia(curso: String, datos: ConstanciaRemota) {
        prefs.edit().putString("constancia_nombre_$curso", datos.nombre)
            .putLong("constancia_fecha_$curso", datos.fechaEmision)
            .remove(EVALUACION_PENDIENTE + curso).apply()
    }

    private companion object {
        const val EVALUACION_PENDIENTE = "constancia_pendiente_"
    }
}
