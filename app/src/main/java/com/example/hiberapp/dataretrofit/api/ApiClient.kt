package com.example.hiberapp.dataretrofit.api

import android.content.Context
import android.widget.Toast
import co.infinum.retromock.Retromock
import com.example.hiberapp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.app.Activity
import android.view.ViewGroup

object ApiClient {

    private const val BASE_URL = "https://e8a0d832-0371-4941-a72a-4bfd76f98e31.mock.pstmn.io/"

    var useMock = false

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var currentSnackbar: Snackbar? = null

    fun enableMock(context: Context) {
        useMock = !useMock
        val mensaje =
            if (useMock) context.getString(R.string.modo_mock_activado) else context.getString(R.string.modo_mock_desactivado)
        mostrarMensaje(context, mensaje)
    }

    fun getService(context: Context): ApiService {
        return if (useMock) {
            val mockRetrofit = Retromock.Builder()
                .retrofit(retrofit)
                .defaultBodyFactory { path: String ->
                    val archivo = when {
                        path.contains("facturas") -> "facturas_mock.json"
                        path.contains("detalles") -> "detalles.json"
                        else -> throw IllegalArgumentException(
                            context.getString(
                                R.string.ruta_no_soportada,
                                path
                            ))
                    }
                    context.assets.open(archivo)
                }
                .build()

            mockRetrofit.create(ApiService::class.java)
        } else {
            retrofit.create(ApiService::class.java)
        }
    }

    fun mostrarMensaje(context: Context, mensaje: String) {
        // Si hay un Snackbar mostrándose, lo ocultamos
        currentSnackbar?.dismiss()
        
        // Buscamos una vista válida para mostrar el Snackbar
        val activity = context as? Activity
        val rootView = activity?.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)
        
        if (rootView != null) {
            // Creamos y mostramos el nuevo Snackbar
            currentSnackbar = Snackbar.make(rootView, mensaje, Snackbar.LENGTH_SHORT)
            currentSnackbar?.show()
        } else {
            // Si no encontramos una vista válida, usamos Toast como fallback
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }
    }
}