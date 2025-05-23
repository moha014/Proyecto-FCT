package com.example.hiberapp.data.local

import android.content.Context
import com.example.hiberapp.R
import com.example.hiberapp.dataretrofit.api.ApiClient
import com.example.hiberapp.dataretrofit.api.ApiService
import com.example.hiberapp.domain.factura.Factura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Repositorio que maneja los datos de facturas desde la API y base de datos local
class FacturaRepository(private val context: Context, private val facturaDao: FacturaDao) {

    // Método para actualizar las facturas desde la API y guardarlas en Room
    suspend fun refreshFacturasFromApi() = withContext(Dispatchers.IO) {
        try {
            // Obtenemos el servicio de la API
            val apiService: ApiService = ApiClient.getService(context)
            // Hacemos la petición de forma síncrona (execute())
            val response = apiService.obtenerFacturas().execute()

            // Comprobamos si la respuesta fue exitosa
            if (!response.isSuccessful) {
                throw Exception(
                    context.getString(
                        R.string.error_al_obtener_facturas,
                        response.code()
                    )
                )
            }

            // Convertimos las facturas de la API a entidades de Room
            val facturas = response.body()?.facturas?.map { factura ->
                FacturaEntity(
                    fecha = factura.fecha,
                    descEstado = factura.descEstado,
                    importeOrdenacion = factura.importeOrdenacion
                )
            } ?: emptyList() // Si no hay facturas, lista vacía

            // Limpiamos la tabla y insertamos las nuevas facturas
            facturaDao.clearAll()
            facturaDao.insertFacturas(facturas)
        } catch (e: Exception) {
            // Si hay error, lanzamos una excepción con mensaje personalizado
            throw Exception(context.getString(R.string.error_al_actualizar_facturas, e.message))
        }
    }

    // Método para obtener las facturas desde la base de datos local
    fun getFacturas(): Flow<List<Factura>> {
        return facturaDao.getAllFacturas().map { entities ->
            // Convertimos las entidades de Room a objetos del dominio
            entities.map { entity ->
                Factura(
                    fecha = entity.fecha,
                    descEstado = entity.descEstado ?: "", // Si es null, ponemos cadena vacía
                    importeOrdenacion = entity.importeOrdenacion
                )
            }
        }
    }
}