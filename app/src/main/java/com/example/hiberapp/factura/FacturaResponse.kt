package com.example.hiberapp.factura

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class FacturaResponse(
    @SerializedName("numFacturas") var numFacturas: Int,
    @SerializedName("facturas") val facturas: List<Factura>
)
private fun getRetrofit():Retrofit{
    return Retrofit.Builder()
        .baseUrl("https://5ed569d3-e095-46b6-bfc3-5a8d278ea616.mock.pstmn.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}