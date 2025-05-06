package com.example.hiberapp.DataRetrofit

import android.content.Context
import co.infinum.retromock.Retromock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader

object ApiClient {
    private const val BASE_URL = "https://5ed569d3-e095-46b6-bfc3-5a8d278ea616.mock.pstmn.io/"

    var useMock = false

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun enableMock(context: Context) {
        useMock = !useMock
    }

    fun getService(context: Context): ApiService {
        return if (useMock) {
            val mockRetrofit = Retromock.Builder()
                .retrofit(retrofit)
                .build()

            mockRetrofit.create(ApiService::class.java)
        } else {
            retrofit.create(ApiService::class.java)
        }
    }
}