package com.example.hiberapp.ui.factura

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.hiberapp.R
import com.example.hiberapp.factura.FacturaFragment
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

// Fragment para configurar los filtros de las facturas
class FiltrarFacturasFragment(private val maxImporte: Float = 70f) : Fragment() {

    // Variables para acceder a los elementos de la interfaz
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

    // Metodo que infla el layout del fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtrar_facturas, container, false)
    }

    // Metodo que se ejecuta después de crear la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Conectamos las variables con los elementos de la interfaz
        etFechaInicio = view.findViewById(R.id.etFechaInicio)
        etFechaFinal = view.findViewById(R.id.etFechaFinal)
        rangeSlider = view.findViewById(R.id.rangeSlider)
        tvMinSeleccionado = view.findViewById(R.id.tvMinSeleccionado)
        tvMaxSeleccionado = view.findViewById(R.id.tvMaxSeleccionado)

        // Conectamos los checkboxes para los diferentes estados
        cbPagado = view.findViewById(R.id.cbPagado)
        cbAnuladas = view.findViewById(R.id.cbAnuladas)
        cbCuotaFija = view.findViewById(R.id.cbCuotaFija)
        cbPendiente = view.findViewById(R.id.cbPendiente)
        cbPlanPago = view.findViewById(R.id.cbPlanPago)

        setupDatePickers() // Configuramos los selectores de fecha
        setupRangeSlider() // Configuramos el slider de rango de precios

        // Botón para cerrar este fragment y volver al anterior
        view.findViewById<ImageView>(R.id.ivCerrar).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Botón para aplicar los filtros seleccionados
        view.findViewById<Button>(R.id.btnAplicar).setOnClickListener {
            aplicarFiltros()
        }

        // Botón para limpiar todos los filtros
        view.findViewById<Button>(R.id.btnEliminarFiltros).setOnClickListener {
            limpiarFiltros()
        }
    }

    // Configura los selectores de fecha (calendario desplegable)
    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())

        // Función que muestra el calendario cuando tocas un campo de fecha
        val showDateDialog: (EditText) -> Unit = { editText ->
            // Creamos un DatePickerDialog con tema verde personalizado
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialog,
                { _, year, month, day ->
                    // Cuando el usuario selecciona una fecha, la ponemos en el EditText
                    calendar.set(year, month, day)
                    editText.setText(formatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        // Asignamos el evento de click a cada campo de fecha
        etFechaInicio.setOnClickListener { showDateDialog(etFechaInicio) }
        etFechaFinal.setOnClickListener { showDateDialog(etFechaFinal) }
    }

    // Configura el slider para seleccionar rango de precios
    private fun setupRangeSlider() {
        // Establecemos los valores inicial y final del slider (1€ a maxImporte)
        rangeSlider.valueFrom = 1f
        rangeSlider.valueTo = maxImporte
        rangeSlider.setValues(1f, maxImporte)
        rangeSlider.stepSize = 1f // Se mueve de 1 en 1

        // Mostramos los valores iniciales en los textos
        tvMinSeleccionado.text = getString(R.string._1)
        tvMaxSeleccionado.text = String.format("%.0f €", maxImporte)

        // Los TextView de arriba muestran siempre el mínimo y el máximo
        val tvMinImporte = requireView().findViewById<TextView>(R.id.tvMinImporte)
        val tvMaxImporte = requireView().findViewById<TextView>(R.id.tvMaxImporte)
        tvMinImporte.text = getString(R.string._1)
        tvMaxImporte.text = String.format("%.0f €", maxImporte)

        // Cuando el usuario mueve el slider, solo los textos de abajo cambian
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val valores = slider.values
            val minValor = valores[0].toInt()
            val maxValor = valores[1].toInt()
            tvMinSeleccionado.text = "$minValor €"
            tvMaxSeleccionado.text = "$maxValor €"
        }
    }

    // Metodo que recoge todos los filtros y los envía al fragment principal
    private fun aplicarFiltros() {
        try {
            // Recogemos los valores de fecha (si están vacíos, los ponemos como null)
            val fechaInicio = etFechaInicio.text.toString().ifEmpty { null }
            val fechaFin = etFechaFinal.text.toString().ifEmpty { null }

            // Recogemos los valores del slider de precio
            val valores = rangeSlider.values
            val montoMinimo = valores[0].toInt()
            val montoMaximo = valores[1].toInt()

            // Creamos un Bundle con todos los filtros seleccionados
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

            // Enviamos los filtros al fragment principal usando FragmentResult
            parentFragmentManager.setFragmentResult("filtrosFacturas", bundle)

            // Cerramos este fragment para volver al anterior
            requireActivity().supportFragmentManager.popBackStack()

        } catch (e: Exception) {
            // Si hay algún error, mostramos un mensaje al usuario
            Snackbar.make(
                requireView(),
                getString(R.string.error_al_aplicar_filtros, e.message),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    // Metodo para limpiar todos los filtros y volver a los valores por defecto
    private fun limpiarFiltros() {
        etFechaInicio.setText("") // Limpiamos fecha inicio
        etFechaFinal.setText("") // Limpiamos fecha fin
        rangeSlider.setValues(1f, maxImporte) // Volvemos el slider a los valores iniciales

        // Desmarcamos todos los checkboxes
        cbPagado.isChecked = false
        cbAnuladas.isChecked = false
        cbCuotaFija.isChecked = false
        cbPendiente.isChecked = false
        cbPlanPago.isChecked = false
    }

    // Companion object para crear nuevas instancias del fragment
    companion object {
        fun newInstance() = FiltrarFacturasFragment()
    }
} 