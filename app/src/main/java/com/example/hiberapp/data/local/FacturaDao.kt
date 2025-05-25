package com.example.hiberapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data Access Object - Define las operaciones que podemos hacer con las facturas
@Dao
interface FacturaDao {

    // Consulta para obtener todas las facturas de la base de datos
    @Query("SELECT * FROM facturas")
    fun getAllFacturas(): Flow<List<FacturaEntity>>

    // Metodo para insertar una lista de facturas (si ya existe una, la reemplaza)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFacturas(facturas: List<FacturaEntity>)

    // Metodo para borrar todas las facturas de la tabla
    @Query("DELETE FROM facturas")
    suspend fun clearAll()
}