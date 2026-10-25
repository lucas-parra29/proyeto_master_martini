package com.example.proyectomartini.data.repository


import com.example.proyectomartini.data.local.FavoritoDao
import com.example.proyectomartini.data.local.FavoritoEntity
import kotlinx.coroutines.flow.Flow

class FavoritoRepository(private val dao: FavoritoDao) {

    val favoritos: Flow<List<FavoritoEntity>> = dao.observarTodos()

    suspend fun agregar(favorito: FavoritoEntity) = dao.insertar(favorito)

    suspend fun quitar(titulo: String) = dao.eliminar(titulo)
}