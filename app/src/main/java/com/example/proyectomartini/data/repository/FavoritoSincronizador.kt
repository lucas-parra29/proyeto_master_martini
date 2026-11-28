package com.example.proyectomartini.data.repository

import android.content.Context
import com.example.proyectomartini.data.local.FavoritoCambio
import com.example.proyectomartini.data.local.FavoritoDao
import com.example.proyectomartini.data.local.FavoritoEntity
import com.example.proyectomartini.data.local.MartiniDatabase
import com.example.proyectomartini.data.remote.RetrofitClient
import com.example.proyectomartini.data.remote.ServiciosApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import java.io.IOException

class FavoritoSincronizador(context: Context) {
    private val auth = AuthRepository(context)
    private val cuenta = auth.correoSesion()
    private val api = RetrofitClient.crear { auth.tokenDeCuenta(cuenta) }.create(ServiciosApi::class.java)
    private val dao = MartiniDatabase.getInstance(context).favoritoDao()
    private val mutex = synchronized(bloqueos) { bloqueos.getOrPut(cuenta) { Mutex() } }

    val favoritos = dao.observarTodos()

    suspend fun alternar(favorito: FavoritoEntity) = editar {
        aplicarLocal(FavoritoCambio(favorito.titulo, favorito.tipo, favorito.duracion,
            favorito.nivel, eliminado = existe(favorito.titulo)))
    }

    suspend fun quitar(titulo: String) = editar {
        aplicarLocal(FavoritoCambio(titulo, null, null, null, eliminado = true))
    }

    suspend fun sincronizar() = editar {}

    private suspend fun editar(cambio: suspend FavoritoDao.() -> Unit) {
        mutex.withLock {
            if (cuenta.isBlank() || auth.correoSesion() != cuenta) return
            dao.prepararSincronizacion()
            dao.cambio()
            try {
                for (pendiente in dao.pendientes()) {
                    if (auth.correoSesion() != cuenta) return
                    api.cambiarFavorito(pendiente)
                    dao.eliminarPendiente(pendiente.titulo)
                }
                if (auth.correoSesion() == cuenta) dao.aplicarRemotos(api.favoritos())
            } catch (_: IOException) {
                // Las operaciones locales se conservan para reintentar al volver.
            } catch (_: HttpException) {
                // Continúa disponible el contenido guardado en este dispositivo.
            }
        }
    }

    companion object {
        private val bloqueos = mutableMapOf<String, Mutex>()
    }
}
