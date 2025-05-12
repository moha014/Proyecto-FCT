package com.example.hiberapp.DataRetrofit

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.example.hiberapp.factura.FacturaResponse
import com.example.hiberapp.smartSolar.Fragments.DetallesResponse
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