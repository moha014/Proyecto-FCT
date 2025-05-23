package DataRetrofit.api

import android.content.Context
import android.widget.Toast
import co.infinum.retromock.Retromock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente para obtener el servicio de la API o el mock
object ApiClient {
    private const val BASE_URL = "https://e8a0d832-0371-4941-a72a-4bfd76f98e31.mock.pstmn.io/"
    var useMock = false

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun enableMock(context: Context) {
        useMock = !useMock
        val mensaje = if (useMock) "Modo Mock ACTIVADO" else "Modo Mock DESACTIVADO"
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun getService(context: Context): ApiService {
        return if (useMock) {
            val mockRetrofit = Retromock.Builder()
                .retrofit(retrofit)
                .defaultBodyFactory { path: String ->
                    val archivo = when {
                        path.contains("facturas") -> "facturas_mock.json"
                        path.contains("detalles") -> "detalles.json"
                        else -> throw IllegalArgumentException("Ruta no soportada: $path")
                    }
                    context.assets.open(archivo)
                }
                .build()
            mockRetrofit.create(ApiService::class.java)
        } else {
            retrofit.create(ApiService::class.java)
        }
    }
} 