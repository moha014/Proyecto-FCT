package com.example.hiberapp.DataRetrofit

import com.example.hiberapp.factura.FacturaResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("facturas")
    fun obtenerFacturas(): Call<FacturaResponse>
}