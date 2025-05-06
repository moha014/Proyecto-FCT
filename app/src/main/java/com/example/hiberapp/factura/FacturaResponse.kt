package com.example.hiberapp.factura

import com.google.gson.annotations.SerializedName

data class FacturaResponse(
    @SerializedName("numFacturas") val numFacturas: Int,
    @SerializedName("facturas") val facturas: List<Factura>
)