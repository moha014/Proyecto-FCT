package com.example.hiberapp.dataretrofit.repository

import com.example.hiberapp.Domain.Factura
import com.example.hiberapp.dataretrofit.api.Retrofit


class FacturaRepository {

    // Función para obtener facturas del API
    suspend fun getFacturas(): List<Factura> {
        // Intenta hacer la llamada al API
        val response = Retrofit.facturaApi.getFacturas()

        // Si la respuesta es exitosa, devuelve los datos
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            // Si hay un error, devuelve lista vacía
            emptyList()
        }
    }
}