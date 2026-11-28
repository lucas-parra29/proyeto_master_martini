package com.example.proyectomartini.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Query("SELECT * FROM favoritos ORDER BY fechaAgregado DESC")
    fun observarTodos(): Flow<List<FavoritoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE titulo = :titulo)")
    suspend fun existe(titulo: String): Boolean

    @androidx.room.Transaction
    suspend fun alternar(favorito: FavoritoEntity) {
        if (existe(favorito.titulo)) eliminar(favorito.titulo) else insertar(favorito)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE titulo = :titulo")
    suspend fun eliminar(titulo: String)

    @Query("SELECT * FROM favoritos")
    suspend fun guardados(): List<FavoritoEntity>

    @Query("SELECT * FROM favorito_cambio ORDER BY titulo")
    suspend fun pendientes(): List<FavoritoCambio>

    @Query("DELETE FROM favorito_cambio WHERE titulo = :titulo")
    suspend fun eliminarPendiente(titulo: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorito_sincronizacion WHERE id = 1)")
    suspend fun preparado(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarCambio(cambio: FavoritoCambio)

    @Insert
    suspend fun guardarMarca(marca: FavoritoSincronizacion)

    @Query("DELETE FROM favoritos")
    suspend fun vaciar()

    @androidx.room.Transaction
    suspend fun prepararSincronizacion() {
        if (preparado()) return
        guardados().forEach { guardarCambio(FavoritoCambio(it.titulo, it.tipo, it.duracion, it.nivel, false)) }
        guardarMarca(FavoritoSincronizacion())
    }

    @androidx.room.Transaction
    suspend fun aplicarLocal(cambio: FavoritoCambio) {
        if (cambio.eliminado) eliminar(cambio.titulo)
        else insertar(FavoritoEntity(cambio.titulo, requireNotNull(cambio.tipo),
            requireNotNull(cambio.duracion), requireNotNull(cambio.nivel)))
        guardarCambio(cambio)
    }

    @androidx.room.Transaction
    suspend fun aplicarRemotos(favoritos: List<FavoritoEntity>) {
        vaciar()
        favoritos.forEach { insertar(it) }
    }
}
