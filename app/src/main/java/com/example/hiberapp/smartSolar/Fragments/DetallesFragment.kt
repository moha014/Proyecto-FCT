package com.example.hiberapp.smartSolar.Fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hiberapp.dataretrofit.api.ApiClient
import com.example.hiberapp.R
import com.example.hiberapp.dataretrofit.responses.DetallesResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalless, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val infoIcon = view.findViewById<ImageView>(R.id.icono_info)
        infoIcon?.setOnClickListener {
            mostrarDialogoInfo()
        }

        cargarDetallesDesdeApi()

        val jsonData = arguments?.getString("jsonData")
        jsonData?.let {
            displayJsonData(it)
        }
    }

    private fun cargarDetallesDesdeApi() {
        context?.let { ctx ->
            val apiService = ApiClient.getService(ctx)
            val call = apiService.obtenerDetalles()

            call.enqueue(object : Callback<DetallesResponse> {
                override fun onResponse(
                    call: Call<DetallesResponse>,
                    response: Response<DetallesResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val detalles = response.body()!!
                        actualizarUI(detalles)
                    } else {
                        Toast.makeText(
                            context,
                            "Error al cargar los detalles: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("DetallesFragment", "Error en la respuesta: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DetallesResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Error al cargar los detalles: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("DetallesFragment", "Error en la llamada: ${t.message}")
                }
            })
        }
    }

    private fun actualizarUI(detalles: DetallesResponse) {
        view?.findViewById<TextView>(R.id.tv_cau)?.text = detalles.cau
        view?.findViewById<TextView>(R.id.tv_estado_solicitud)?.text = detalles.estadoSolicitud
        view?.findViewById<TextView>(R.id.tv_tipo_autoconsumo)?.text = detalles.tipoAutoconsumo
        view?.findViewById<TextView>(R.id.tv_compensacion_excedentes)?.text =
            detalles.compensacionExcedentes
        view?.findViewById<TextView>(R.id.tv_potencia_instalacion)?.text =
            detalles.potenciaInstalacion
    }

    private fun mostrarDialogoInfo() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.informacion_estado)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnAceptar = dialog.findViewById<Button>(R.id.btn_aceptar)
        btnAceptar?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun displayJsonData(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)

            view?.findViewById<TextView>(R.id.tv_cau)?.text =
                jsonObject.getString(getString(R.string.cau_c_digo_autoconsumo))

            view?.findViewById<TextView>(R.id.tv_estado_solicitud)?.text =
                jsonObject.getString(getString(R.string.estado_solicitud_alta_autoconsumidor))

            view?.findViewById<TextView>(R.id.tv_tipo_autoconsumo)?.text =
                jsonObject.getString(getString(R.string.tipo_autoconsumo))

            view?.findViewById<TextView>(R.id.tv_compensacion_excedentes)?.text =
                jsonObject.getString(getString(R.string.compensaci_n_de_excedentes))

            view?.findViewById<TextView>(R.id.tv_potencia_instalacion)?.text =
                jsonObject.getString(getString(R.string.potencia_de_instalaci_n))

        } catch (e: JSONException) {
            Log.e(getString(R.string.detallesfragment), getString(R.string.error_al_parsear_json, e.message))
            e.printStackTrace()
        }
    }
}