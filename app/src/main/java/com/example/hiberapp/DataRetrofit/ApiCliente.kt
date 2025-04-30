package com.example.hiberapp.DataRetrofit

import co.infinum.retromock.Retromock
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object ApiCliente {
    private const val BASE_URL = "https://viewnextandroid.wiremockapi.cloud/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retromock = Retromock.Builder()
        .retrofit(retrofit)
        .defaultBodyFactory(ResourceBodyFactory())
        .build()

    private var useMock = false

    fun setUseMock(useMock: Boolean) {
        this.useMock = useMock
    }

    fun getFacturaApi(): FacturaApi {
        return if (useMock) {
            retromock.create(FacturaApi::class.java)
        } else {
            retrofit.create(FacturaApi::class.java)
        }
    }
}