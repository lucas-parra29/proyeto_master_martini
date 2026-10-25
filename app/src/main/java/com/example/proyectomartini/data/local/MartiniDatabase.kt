package com.example.proyectomartini.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritoEntity::class], version = 1, exportSchema = false)
abstract class MartiniDatabase : RoomDatabase() {

    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var instancia: MartiniDatabase? = null

        fun getInstance(context: Context): MartiniDatabase =
            instancia ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MartiniDatabase::class.java,
                    "martini_db"
                ).build().also { instancia = it }
            }
    }
}