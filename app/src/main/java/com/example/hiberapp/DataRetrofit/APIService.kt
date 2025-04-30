package com.example.hiberapp.DataRetrofit

import com.example.hiberapp.factura.FacturaResponse
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET("facturas")
    suspend fun getFactura(@Url url:String): FacturaResponse
}
