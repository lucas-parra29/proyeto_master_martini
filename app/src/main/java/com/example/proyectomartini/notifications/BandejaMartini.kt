package com.example.proyectomartini.notifications

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import org.json.JSONObject

internal data class AvisoMartini(
    val id: String, val titulo: String, val mensaje: String, val destino: String, val leido: Boolean = false
)

internal class BandejaMartini(context: Context) {
    private val ajustes = context.getSharedPreferences("avisos_martini", Context.MODE_PRIVATE)
    val cambios get() = revision

    fun leer(): List<AvisoMartini> {
        val datos = JSONArray(ajustes.getString("avisos", "[]"))
        return (0 until datos.length()).map { i ->
            val item = datos.getJSONObject(i)
            AvisoMartini(item.getString("id"), item.getString("titulo"), item.getString("mensaje"),
                item.getString("destino"), item.optBoolean("leido"))
        }
    }

    fun agregar(titulo: String, mensaje: String, destino: String) {
        val aviso = AvisoMartini(java.util.UUID.randomUUID().toString(), titulo, mensaje, destino)
        guardar((listOf(aviso) + leer()).take(40))
    }

    fun marcarLeido(id: String) = guardar(leer().map { if (it.id == id) it.copy(leido = true) else it })

    private fun guardar(avisos: List<AvisoMartini>) {
        val datos = JSONArray()
        avisos.forEach { aviso ->
            datos.put(JSONObject().put("id", aviso.id).put("titulo", aviso.titulo)
                .put("mensaje", aviso.mensaje).put("destino", aviso.destino).put("leido", aviso.leido))
        }
        ajustes.edit().putString("avisos", datos.toString()).apply()
        revision.value++
    }

    companion object {
        private val revision = MutableStateFlow(0)
    }
}
