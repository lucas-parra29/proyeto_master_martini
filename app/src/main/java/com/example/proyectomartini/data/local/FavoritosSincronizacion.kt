package com.example.proyectomartini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorito_cambio")
data class FavoritoCambio(
    @PrimaryKey val titulo: String,
    val tipo: String?,
    val duracion: String?,
    val nivel: String?,
    val eliminado: Boolean
)

@Entity(tableName = "favorito_sincronizacion")
data class FavoritoSincronizacion(@PrimaryKey val id: Int = 1)
