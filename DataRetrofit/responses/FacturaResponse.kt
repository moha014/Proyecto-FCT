package DataRetrofit.responses

import com.example.hiberapp.domain.factura.Factura
import com.google.gson.annotations.SerializedName

// Respuesta de la API para facturas
data class FacturaResponse(
    @SerializedName("numFacturas") val numFacturas: Int,
    @SerializedName("facturas") val facturas: List<Factura>
) 