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
import com.example.hiberapp.databinding.FragmentDetallessBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.snackbar.Snackbar

class DetallesFragment : Fragment() {
    private var _binding: FragmentDetallessBinding? = null
    private val binding get() = _binding!!
    private var currentSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetallessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el icono de información
        binding.iconoInfo.setOnClickListener {
            mostrarDialogoInfo()
        }

        cargarDetallesDesdeApi()

        val jsonData = arguments?.getString("jsonData")
        jsonData?.let {
            displayJsonData(it)
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        // Si hay un Snackbar mostrándose, lo ocultamos
        currentSnackbar?.dismiss()
        
        // Creamos y mostramos el nuevo Snackbar
        currentSnackbar = Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT)
        currentSnackbar?.show()
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
                        mostrarMensaje("Error al cargar los detalles: ${response.code()}")
                        Log.e("DetallesFragment", "Error en la respuesta: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DetallesResponse>, t: Throwable) {
                    mostrarMensaje("Error al cargar los detalles: ${t.message}")
                    Log.e("DetallesFragment", "Error en la llamada: ${t.message}")
                }
            })
        }
    }

    private fun actualizarUI(detalles: DetallesResponse) {
        binding.tvCau.text = detalles.cau
        binding.tvEstadoSolicitud.text = detalles.estadoSolicitud
        binding.tvTipoAutoconsumo.text = detalles.tipoAutoconsumo
        binding.tvCompensacionExcedentes.text =
            detalles.compensacionExcedentes
        binding.tvPotenciaInstalacion.text =
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

            binding.tvCau.text =
                jsonObject.getString(getString(R.string.cau_c_digo_autoconsumo))

            binding.tvEstadoSolicitud.text =
                jsonObject.getString(getString(R.string.estado_solicitud_alta_autoconsumidor))

            binding.tvTipoAutoconsumo.text =
                jsonObject.getString(getString(R.string.tipo_autoconsumo))

            binding.tvCompensacionExcedentes.text =
                jsonObject.getString(getString(R.string.compensaci_n_de_excedentes))

            binding.tvPotenciaInstalacion.text =
                jsonObject.getString(getString(R.string.potencia_de_instalaci_n))

        } catch (e: JSONException) {
            Log.e(getString(R.string.detallesfragment), getString(R.string.error_al_parsear_json, e.message))
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}