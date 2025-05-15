package com.example.hiberapp.ui.factura

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.hiberapp.R
import com.example.hiberapp.factura.FacturaFragment
import com.google.android.material.slider.RangeSlider
import java.text.SimpleDateFormat
import java.util.*

class FiltrarFacturasFragment : Fragment() {

    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFinal: EditText
    private lateinit var rangeSlider: RangeSlider
    private lateinit var tvMinSeleccionado: TextView
    private lateinit var tvMaxSeleccionado: TextView
    private lateinit var cbPagado: CheckBox
    private lateinit var cbAnuladas: CheckBox
    private lateinit var cbCuotaFija: CheckBox
    private lateinit var cbPendiente: CheckBox
    private lateinit var cbPlanPago: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtrar_facturas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFechaInicio = view.findViewById(R.id.etFechaInicio)
        etFechaFinal = view.findViewById(R.id.etFechaFinal)
        rangeSlider = view.findViewById(R.id.rangeSlider)
        tvMinSeleccionado = view.findViewById(R.id.tvMinSeleccionado)
        tvMaxSeleccionado = view.findViewById(R.id.tvMaxSeleccionado)

        // Inicializar checkboxes
        cbPagado = view.findViewById(R.id.cbPagado)
        cbAnuladas = view.findViewById(R.id.cbAnuladas)
        cbCuotaFija = view.findViewById(R.id.cbCuotaFija)
        cbPendiente = view.findViewById(R.id.cbPendiente)
        cbPlanPago = view.findViewById(R.id.cbPlanPago)

        setupDatePickers()
        setupRangeSlider()

        // Botón para cerrar fragment
        view.findViewById<ImageView>(R.id.ivCerrar).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Botón para aplicar los filtros
        view.findViewById<Button>(R.id.btnAplicar).setOnClickListener {
            aplicarFiltros()
        }

        // Botón para borrar los filtros
        view.findViewById<Button>(R.id.btnEliminarFiltros).setOnClickListener {
            limpiarFiltros()
        }
    }

    // Muestra un calendario desplegable
    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val showDateDialog: (EditText) -> Unit = { editText ->
            // Usamos un tema personalizado para el DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.GreenDatePickerTheme,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    editText.setText(formatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        etFechaInicio.setOnClickListener { showDateDialog(etFechaInicio) }
        etFechaFinal.setOnClickListener { showDateDialog(etFechaFinal) }
    }


    private fun setupRangeSlider() {
        rangeSlider.setValues(1f, 70f)
        rangeSlider.stepSize = 1f

        tvMinSeleccionado.text = "1 €"
        tvMaxSeleccionado.text = "70 €"

        rangeSlider.addOnChangeListener { slider, _, _ ->
            val valores = slider.values
            val minValor = valores[0].toInt()
            val maxValor = valores[1].toInt()
            tvMinSeleccionado.text = "$minValor €"
            tvMaxSeleccionado.text = "$maxValor €"
        }
    }




    private fun aplicarFiltros() {
        try {
            val fechaInicio = etFechaInicio.text.toString().ifEmpty { null }
            val fechaFin = etFechaFinal.text.toString().ifEmpty { null }
            val valores = rangeSlider.values
            val montoMinimo = valores[0].toInt()
            val montoMaximo = valores[1].toInt()

            val bundle = Bundle().apply {
                putString("fechaInicio", fechaInicio)
                putString("fechaFin", fechaFin)
                putInt("montoMinimo", montoMinimo)
                putInt("montoMaximo", montoMaximo)
                putBoolean("pagado", cbPagado.isChecked)
                putBoolean("anuladas", cbAnuladas.isChecked)
                putBoolean("cuotaFija", cbCuotaFija.isChecked)
                putBoolean("pendiente", cbPendiente.isChecked)
                putBoolean("planPago", cbPlanPago.isChecked)
            }

            // Enviamos los datos al fragment receptor
            parentFragmentManager.setFragmentResult("filtrosFacturas", bundle)

            // Cerramos el fragmento
            requireActivity().supportFragmentManager.popBackStack()

        } catch (e: Exception) {
            Toast.makeText(context, "Error al aplicar filtros: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



    private fun limpiarFiltros() {
        etFechaInicio.setText("")
        etFechaFinal.setText("")
        rangeSlider.setValues(1f, 70f)

        cbPagado.isChecked = false
        cbAnuladas.isChecked = false
        cbCuotaFija.isChecked = false
        cbPendiente.isChecked = false
        cbPlanPago.isChecked = false
    }

    companion object {
        fun newInstance() = FiltrarFacturasFragment()
    }
}