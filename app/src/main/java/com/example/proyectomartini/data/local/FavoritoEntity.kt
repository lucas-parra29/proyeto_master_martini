package com.example.proyectomartini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val titulo: String,
    val tipo: String,
    val duracion: String,
    val nivel: String,
    val fechaAgregado: Long = System.currentTimeMillis()
)