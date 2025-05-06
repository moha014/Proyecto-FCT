package com.example.hiberapp.dataretrofit.api

import com.example.hiberapp.Domain.Factura
import retrofit2.Response
import retrofit2.http.GET

interface FacturaApi {
    @GET("facturas")
    suspend fun getFacturas(): Response<List<Factura>>
}