package com.example.hiberapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facturas")
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descEstado: String,
    val importeOrdenacion: Double,
    val fecha: String,
    val estado: String
)