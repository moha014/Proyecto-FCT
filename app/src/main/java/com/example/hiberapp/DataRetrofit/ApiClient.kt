package com.example.hiberapp.DataRetrofit

import android.content.Context
import android.util.Log
import android.widget.Toast
import co.infinum.retromock.Retromock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader

object ApiClient {
    private const val BASE_URL = "https://e8a0d832-0371-4941-a72a-4bfd76f98e31.mock.pstmn.io/"

    var useMock = false

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun enableMock(context: Context) {
        useMock = !useMock

        val message = if (useMock) "Modo Mock ACTIVADO" else "Modo Mock DESACTIVADO"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getService(context: Context): ApiService {
        return if (useMock) {
            val mockRetrofit = Retromock.Builder()
                .retrofit(retrofit)
                .defaultBodyFactory { context.assets.open("facturas_mock.json") }
                .build()

            mockRetrofit.create(ApiService::class.java)
        } else {
            retrofit.create(ApiService::class.java)
        }
    }

}