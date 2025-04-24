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
import com.example.hiberapp.R
import org.json.JSONException
import org.json.JSONObject

class DetallesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("DetallesFragment", "onCreateView: inflando el layout...")
        return inflater.inflate(R.layout.fragment_detalless, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("DetallesFragment", "onViewCreated: Fragment ya visible")

        // Manejamos el click del icono de información
        val infoIcon = view.findViewById<ImageView>(R.id.icono_info)
        if (infoIcon == null) {
            Log.e("DetallesFragment", "icono_info NO encontrado. Revisa que el layout sea el correcto.")
            Toast.makeText(context, "icono_info no encontrado", Toast.LENGTH_LONG).show()
        } else {
            Log.d("DetallesFragment", "icono_info encontrado correctamente")
            infoIcon.setOnClickListener { view ->
                Toast.makeText(context, "Icono clickeado", Toast.LENGTH_SHORT).show()
                mostrarDialogoInfo()
            }
        }

        // Recuperar y mostrar los datos del JSON
        val jsonData = arguments?.getString("jsonData")
        if (jsonData != null) {
            Log.d("DetallesFragment", "Recibido JSON: $jsonData")
            displayJsonData(jsonData)
        } else {
            Log.e("DetallesFragment", "No se recibió ningún JSON por Bundle.")
        }
    }

    private fun mostrarDialogoInfo() {
        Log.d("DetallesFragment", "mostrarDialogoInfo: Mostrando diálogo")

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.informacion_estado)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnAceptar = dialog.findViewById<Button>(R.id.btn_aceptar)
        if (btnAceptar == null) {
            Log.e("DetallesFragment", "Botón aceptar no encontrado en el diálogo.")
        } else {
            Log.d("DetallesFragment", "Botón aceptar encontrado")
            btnAceptar.setOnClickListener {
                Toast.makeText(context, "Botón clickeado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun displayJsonData(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)

            val cau = jsonObject.getString("CAU(Código Autoconsumo)")
            val estadoSolicitud = jsonObject.getString("Estado solicitud alta autoconsumidor")
            val tipoAutoconsumo = jsonObject.getString("Tipo autoconsumo")
            val compensacionExcedentes = jsonObject.getString("Compensación de excedentes")
            val potenciaInstalacion = jsonObject.getString("Potencia de instalación")

            Log.d("DetallesFragment", "Mostrando datos en la UI")

            view?.findViewById<TextView>(R.id.tv_cau)?.text = cau
            view?.findViewById<TextView>(R.id.tv_estado_solicitud)?.text = estadoSolicitud
            view?.findViewById<TextView>(R.id.tv_tipo_autoconsumo)?.text = tipoAutoconsumo
            view?.findViewById<TextView>(R.id.tv_compensacion_excedentes)?.text = compensacionExcedentes
            view?.findViewById<TextView>(R.id.tv_potencia_instalacion)?.text = potenciaInstalacion

        } catch (e: JSONException) {
            Log.e("DetallesFragment", "Error al parsear JSON: ${e.message}")
            e.printStackTrace()
        }
    }
}
