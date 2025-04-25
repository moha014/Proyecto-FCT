package com.example.hiberapp.smartSolar.Fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hiberapp.R
import org.json.JSONException
import org.json.JSONObject

class DetallesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalless, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Icono de información que muestra un popup cuando es clicado
        val infoIcon = view.findViewById<ImageView>(R.id.icono_info)
        infoIcon?.setOnClickListener {
            mostrarDialogoInfo()
        }

        // Recibimiento de un json como argumento y muestra de sus datos
        val jsonData = arguments?.getString("jsonData")
        jsonData?.let {
            displayJsonData(it)
        }
    }

    // Muestra de un diálogo con información adicional
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

    // Toma de un string json, correspondiente conversion a objeto y actualización de los TextView con los valores
    fun displayJsonData(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)

            view?.findViewById<TextView>(R.id.tv_cau)?.text =
                jsonObject.getString("CAU(Código Autoconsumo)")

            view?.findViewById<TextView>(R.id.tv_estado_solicitud)?.text =
                jsonObject.getString("Estado solicitud alta autoconsumidor")

            view?.findViewById<TextView>(R.id.tv_tipo_autoconsumo)?.text =
                jsonObject.getString("Tipo autoconsumo")

            view?.findViewById<TextView>(R.id.tv_compensacion_excedentes)?.text =
                jsonObject.getString("Compensación de excedentes")

            view?.findViewById<TextView>(R.id.tv_potencia_instalacion)?.text =
                jsonObject.getString("Potencia de instalación")

        } catch (e: JSONException) {
            Log.e("DetallesFragment", "Error al parsear JSON: ${e.message}")
            e.printStackTrace()
        }
    }
}
