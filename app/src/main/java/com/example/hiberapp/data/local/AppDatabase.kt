package com.example.hiberapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Configuración principal de la base de datos Room
@Database(entities = [FacturaEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Método abstracto para acceder a las operaciones de facturas
    abstract fun facturaDao(): FacturaDao

    companion object {
        // Variable volátil para garantizar visibilidad entre threads
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Método para obtener una instancia única de la base de datos (patrón Singleton)
        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe una instancia, la devolvemos; si no, creamos una nueva de forma segura
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Nombre del archivo de base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}