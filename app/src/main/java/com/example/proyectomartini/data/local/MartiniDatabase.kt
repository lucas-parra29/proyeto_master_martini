package com.example.proyectomartini.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoritoEntity::class, FavoritoCambio::class, FavoritoSincronizacion::class],
    version = 2, exportSchema = false)
abstract class MartiniDatabase : RoomDatabase() {

    abstract fun favoritoDao(): FavoritoDao

    companion object {
        private val migracion1a2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS favorito_cambio (titulo TEXT NOT NULL PRIMARY KEY, tipo TEXT, duracion TEXT, nivel TEXT, eliminado INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS favorito_sincronizacion (id INTEGER NOT NULL PRIMARY KEY)")
            }
        }
        private val instancias = mutableMapOf<String, MartiniDatabase>()
        @Synchronized
        fun getInstance(context: Context): MartiniDatabase {
            val correo = com.example.proyectomartini.data.repository.AuthRepository(context).correoSesion()
            val clave = com.example.proyectomartini.utils.IdentidadLocal.clave(correo)
            return instancias.getOrPut(clave) {
                Room.databaseBuilder(context.applicationContext, MartiniDatabase::class.java,
                    "martini_db_$clave").addMigrations(migracion1a2).build()
            }
        }
    }
}
