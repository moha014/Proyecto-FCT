package com.example.hiberapp.dataretrofit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private const val BASE_URL = "https://5ed569d3-e095-46b6-bfc3-5a8d278ea616.mock.pstmn.io/"

    // Crear instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Crear servicio de API
    val facturaApi: FacturaApi = retrofit.create(FacturaApi::class.java)
}