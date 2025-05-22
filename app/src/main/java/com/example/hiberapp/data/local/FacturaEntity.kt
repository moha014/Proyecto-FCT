package com.example.hiberapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad que representa una factura en la base de datos
@Entity(tableName = "facturas")
data class FacturaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ID único que se genera automáticamente
    val fecha: String, // Fecha de la factura
    val descEstado: String?, // Estado de la factura (puede ser null)
    val importeOrdenacion: String // Importe de la factura como texto
)