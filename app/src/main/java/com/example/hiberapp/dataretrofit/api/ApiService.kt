package com.example.hiberapp.dataretrofit.api

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.example.hiberapp.dataretrofit.responses.FacturaResponse
import com.example.hiberapp.dataretrofit.responses.DetallesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @Mock
    @MockResponse(body = "facturas_mock.json")
    @GET("facturas")
    fun obtenerFacturas(): Call<FacturaResponse>

    @Mock
    @MockResponse(body = "detalles.json")
    @GET("detalles")
    fun obtenerDetalles(): Call<DetallesResponse>
}