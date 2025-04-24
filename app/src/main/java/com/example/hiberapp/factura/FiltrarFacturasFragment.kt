package com.example.hiberapp.ui.factura

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.hiberapp.R
import com.google.android.material.slider.RangeSlider
import java.text.SimpleDateFormat
import java.util.*

class FiltrarFacturasFragment : Fragment() {

    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFinal: EditText
    private lateinit var rangeSlider: RangeSlider
    private lateinit var tvMinSeleccionado: TextView
    private lateinit var tvMaxSeleccionado: TextView

    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtrar_facturas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        etFechaInicio = view.findViewById(R.id.etFechaInicio)
        etFechaFinal = view.findViewById(R.id.etFechaFinal)
        rangeSlider = view.findViewById(R.id.rangeSlider)
        tvMinSeleccionado = view.findViewById(R.id.tvMinSeleccionado)
        tvMaxSeleccionado = view.findViewById(R.id.tvMaxSeleccionado)


        // Configurar calendarios
        setupDatePickers()

        // Configurar slider
        setupRangeSlider()

        // Hacer clic en la imagen de cierre
        view.findViewById<ImageView>(R.id.ivCerrar).setOnClickListener {
            // Cerrar este fragmento y volver al anterior en la pila
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Manejar clics de botones
        view.findViewById<Button>(R.id.btnAplicar).setOnClickListener {
            aplicarFiltros()
        }

        view.findViewById<Button>(R.id.btnEliminarFiltros).setOnClickListener {
            limpiarFiltros()
        }
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        etFechaInicio.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    etFechaInicio.setText(dateFormatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etFechaFinal.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    etFechaFinal.setText(dateFormatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupRangeSlider() {
        // Establecer valores iniciales
        rangeSlider.setValues(5f, 250f)

        // Listener para actualizar los textos cuando el slider cambia
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            tvMinSeleccionado.text = "${values[0].toInt()} €"
            tvMaxSeleccionado.text = "${values[1].toInt()} €"
        }
    }

    private fun aplicarFiltros() {
        // Implementa la lógica para aplicar los filtros
        Toast.makeText(requireContext(), "Filtros aplicados", Toast.LENGTH_SHORT).show()
        // Cierra el fragmento al aplicar filtros
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun limpiarFiltros() {
        etFechaInicio.setText("")
        etFechaFinal.setText("")
        rangeSlider.setValues(1f, 300f) // Resetear a valores iniciales

        // Limpiar checkboxes
        view?.findViewById<CheckBox>(R.id.cbPagado)?.isChecked = false
        view?.findViewById<CheckBox>(R.id.cbAnuladas)?.isChecked = false
        view?.findViewById<CheckBox>(R.id.cbCuotaFija)?.isChecked = false
        view?.findViewById<CheckBox>(R.id.cbPendiente)?.isChecked = false
        view?.findViewById<CheckBox>(R.id.cbPlanPago)?.isChecked = false

        Toast.makeText(requireContext(), "Filtros eliminados", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = FiltrarFacturasFragment()
    }
}
