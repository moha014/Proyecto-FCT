package com.example.hiberapp.dataretrofit.responses

import com.google.gson.annotations.SerializedName

data class DetallesResponse(
    @SerializedName("CAU(Código Autoconsumo)")
    val cau: String,

    @SerializedName("Estado solicitud alta autoconsumidor")
    val estadoSolicitud: String,

    @SerializedName("Tipo autoconsumo")
    val tipoAutoconsumo: String,

    @SerializedName("Compensación de excedentes")
    val compensacionExcedentes: String,

    @SerializedName("Potencia de instalación")
    val potenciaInstalacion: String
)