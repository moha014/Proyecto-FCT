package com.example.hiberapp.dataretrofit.responses

import com.example.hiberapp.factura.Factura
import com.google.gson.annotations.SerializedName

data class FacturaResponse(
    @SerializedName("numFacturas") val numFacturas: Int,
    @SerializedName("facturas") val facturas: List<Factura>
)