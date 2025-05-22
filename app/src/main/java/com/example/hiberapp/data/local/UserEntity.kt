package com.example.hiberapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad que representa un usuario en la base de datos
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ID único generado automáticamente
    val name: String, // Nombre del usuario
    val email: String // Email del usuario
)