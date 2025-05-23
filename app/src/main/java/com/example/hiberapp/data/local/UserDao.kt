package com.example.hiberapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data Access Object para operaciones con usuarios
@Dao
interface UserDao {

    // Obtener todos los usuarios como Flow (se actualiza automáticamente)
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    // Insertar un nuevo usuario (si ya existe, lo reemplaza)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Borrar todos los usuarios de la tabla
    @Query("DELETE FROM users")
    suspend fun clearAll()

    // Obtener todos los usuarios de una vez (no como Flow)
    @Query("SELECT * FROM users")
    suspend fun getAllUsersOnce(): List<UserEntity>
}