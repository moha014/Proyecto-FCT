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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtrar_facturas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referenciamos los elementos de la interfaz(FiltrarFacturasFragment)
        etFechaInicio = view.findViewById(R.id.etFechaInicio)
        etFechaFinal = view.findViewById(R.id.etFechaFinal)
        rangeSlider = view.findViewById(R.id.rangeSlider)
        tvMinSeleccionado = view.findViewById(R.id.tvMinSeleccionado)
        tvMaxSeleccionado = view.findViewById(R.id.tvMaxSeleccionado)

        // Configuración de la seleccion de las fechas
        setupDatePickers()
        // Configuración de la selección del importe de precio-precio(Slider)
        setupRangeSlider()

        // Botón para cerrar el fragment
        view.findViewById<ImageView>(R.id.ivCerrar).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Botón para aplicar lod filtros seleccionados
        view.findViewById<Button>(R.id.btnAplicar).setOnClickListener {
            aplicarFiltros()
        }

        // Botón para borrar los filtros seleccionador
        view.findViewById<Button>(R.id.btnEliminarFiltros).setOnClickListener {
            limpiarFiltros()
        }
    }

    // Muestra un calendario desplegable (DatePickerDialog) al clicar en los campos "Fecha"
    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val showDateDialog: (EditText) -> Unit = { editText ->
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    editText.setText(formatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etFechaInicio.setOnClickListener { showDateDialog(etFechaInicio) }
        etFechaFinal.setOnClickListener { showDateDialog(etFechaFinal) }
    }

    // Configuracion del Slider para seleccionar un rango de precios
    private fun setupRangeSlider() {
        rangeSlider.setValues(5f, 300f)
        rangeSlider.stepSize = 1f

        // Actualiza la vista inicial con los valores predeterminados
        tvMinSeleccionado.text = "5 €"
        tvMaxSeleccionado.text = "300 €"

        rangeSlider.addOnChangeListener { slider, _, _ ->
            val valores = slider.values
            val minValor = valores[0].toInt()
            val maxValor = valores[1].toInt()
            tvMinSeleccionado.text = "$minValor €"
            tvMaxSeleccionado.text = "$maxValor €"
        }
    }

    // Aplicacion de los filtros y cierre del fragment
    private fun aplicarFiltros() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    // Restablecimiento de todos los campos del filtro a su estado inicial
    private fun limpiarFiltros() {
        etFechaInicio.setText("")
        etFechaFinal.setText("")
        rangeSlider.setValues(1f, 300f)

        val checkBoxIds = listOf(
            R.id.cbPagado, R.id.cbAnuladas, R.id.cbCuotaFija,
            R.id.cbPendiente, R.id.cbPlanPago
        )

        checkBoxIds.forEach {
            view?.findViewById<CheckBox>(it)?.isChecked = false
        }
    }

    companion object {
        fun newInstance() = FiltrarFacturasFragment()
    }
}