package DataRetrofit.api

import DataRetrofit.responses.FacturaResponse
import DataRetrofit.responses.DetallesResponse
import retrofit2.Call
import retrofit2.http.GET

// Interfaz de la API para Retrofit
interface ApiService {
    @GET("facturas")
    fun obtenerFacturas(): Call<FacturaResponse>

    @GET("detalles")
    fun obtenerDetalles(): Call<DetallesResponse>
} 