package com.example.hiberapp.factura

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiberapp.R
import com.example.hiberapp.dataretrofit.api.ApiClient
import com.example.hiberapp.dataretrofit.responses.FacturaResponse
import com.example.hiberapp.databinding.FragmentFacturaBinding
import com.example.hiberapp.ui.factura.FacturaAdapter
import com.example.hiberapp.ui.factura.FiltrarFacturasFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import android.graphics.Color

class FacturaFragment : Fragment() {

    private var _binding: FragmentFacturaBinding? = null
    private val binding get() = _binding!!

    private val todasLasFacturas = mutableListOf<Factura>()
    private val facturasFiltradas = mutableListOf<Factura>()

    // Variables para filtros
    private var fechaInicioActual: String? = null
    private var fechaFinActual: String? = null
    private var montoMinimoActual: Int = 1
    private var montoMaximoActual: Int = 70
    private var pagadoActual: Boolean = false
    private var anuladasActual: Boolean = false
    private var cuotaFijaActual: Boolean = false
    private var pendienteActual: Boolean = false
    private var planPagoActual: Boolean = false
    private var hayFiltrosAplicados: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacturaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        binding.recyclerFacturas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFacturas.adapter = FacturaAdapter(facturasFiltradas) {
            val popupView = LayoutInflater.from(requireContext())
                .inflate(R.layout.informacion_estado2, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(popupView)
                .create()
            popupView.findViewById<View>(R.id.btnCerrar)?.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        setupToolbar()

        // Cargar datos
        mostrarCargando(true)
        obtenerFacturas()

        // Escuchar filtros aplicados desde FiltrarFacturasFragment
        parentFragmentManager.setFragmentResultListener(
            "filtrosFacturas",
            viewLifecycleOwner
        ) { _, bundle ->
            aplicarFiltros(
                fechaInicio = bundle.getString("fechaInicio"),
                fechaFin = bundle.getString("fechaFin"),
                montoMinimo = bundle.getInt("montoMinimo", 1),
                montoMaximo = bundle.getInt("montoMaximo", 70),
                pagado = bundle.getBoolean("pagado"),
                anuladas = bundle.getBoolean("anuladas"),
                cuotaFija = bundle.getBoolean("cuotaFija"),
                pendiente = bundle.getBoolean("pendiente"),
                planPago = bundle.getBoolean("planPago")
            )
        }

        binding.switchChart.setOnCheckedChangeListener { _, _ ->
            actualizarGrafica()
        }
    }

    private fun setupToolbar() {
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.consumoBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.ivFilter.setOnClickListener {
            val filtrarFragment = FiltrarFacturasFragment.newInstance()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, filtrarFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun mostrarCargando(mostrar: Boolean) {
        binding.loadingProgressBar.visibility = if (mostrar) View.VISIBLE else View.GONE
        binding.recyclerFacturas.visibility = if (mostrar) View.GONE else View.VISIBLE
    }

    private fun obtenerFacturas() {
        val apiService = ApiClient.getService(requireContext())
        val call = apiService.obtenerFacturas()

        call.enqueue(object : Callback<FacturaResponse> {
            override fun onResponse(
                call: Call<FacturaResponse>,
                response: Response<FacturaResponse>
            ) {
                mostrarCargando(false)

                if (response.isSuccessful) {
                    response.body()?.let { facturaResponse ->
                        todasLasFacturas.clear()
                        facturaResponse.facturas?.let {
                            todasLasFacturas.addAll(it)
                        }

                        if (hayFiltrosAplicados) {
                            aplicarFiltrosGuardados()
                        } else {
                            facturasFiltradas.clear()
                            facturasFiltradas.addAll(todasLasFacturas)
                            binding.recyclerFacturas.adapter?.notifyDataSetChanged()
                            actualizarGrafica()
                        }
                    }
                } else {
                    showErrorDialog(getString(R.string.error_al_obtener_las_facturas))
                }
            }

            override fun onFailure(call: Call<FacturaResponse>, t: Throwable) {
                mostrarCargando(false)
                showErrorDialog(getString(R.string.error_de_conexi_n, t.message))
            }
        })
    }

    private fun aplicarFiltrosGuardados() {
        aplicarFiltros(
            fechaInicioActual,
            fechaFinActual,
            montoMinimoActual,
            montoMaximoActual,
            pagadoActual,
            anuladasActual,
            cuotaFijaActual,
            pendienteActual,
            planPagoActual
        )
    }

    fun aplicarFiltros(
        fechaInicio: String?,
        fechaFin: String?,
        montoMinimo: Int,
        montoMaximo: Int,
        pagado: Boolean,
        anuladas: Boolean,
        cuotaFija: Boolean,
        pendiente: Boolean,
        planPago: Boolean
    ) {
        fechaInicioActual = fechaInicio
        fechaFinActual = fechaFin
        montoMinimoActual = montoMinimo
        montoMaximoActual = montoMaximo
        pagadoActual = pagado
        anuladasActual = anuladas
        cuotaFijaActual = cuotaFija
        pendienteActual = pendiente
        planPagoActual = planPago

        hayFiltrosAplicados = fechaInicio != null || fechaFin != null ||
                montoMinimo > 1 || montoMaximo < 70 ||
                pagado || anuladas || cuotaFija || pendiente || planPago

        if (todasLasFacturas.isEmpty()) return

        facturasFiltradas.clear()

        for (factura in todasLasFacturas) {
            var cumpleFiltros = true

            // Fecha inicio
            if (!fechaInicio.isNullOrEmpty()) {
                try {
                    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val inicio = formato.parse(fechaInicio)
                    val fechaFactura = formato.parse(factura.fecha)
                    if (fechaFactura.before(inicio)) cumpleFiltros = false
                } catch (_: Exception) {
                    Toast.makeText(context,
                        getString(R.string.error_en_fecha_inicio), Toast.LENGTH_SHORT).show()
                }
            }

            // Fecha fin
            if (cumpleFiltros && !fechaFin.isNullOrEmpty()) {
                try {
                    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fin = formato.parse(fechaFin)
                    val fechaFactura = formato.parse(factura.fecha)
                    if (fechaFactura.after(fin)) cumpleFiltros = false
                } catch (_: Exception) {
                    Toast.makeText(context,
                        getString(R.string.error_en_fecha_fin), Toast.LENGTH_SHORT).show()
                }
            }

            // Monto
            if (cumpleFiltros) {
                try {
                    val monto = factura.importeOrdenacion.toDoubleOrNull()
                    if (monto == null || monto < montoMinimo || monto > montoMaximo) cumpleFiltros = false
                } catch (_: Exception) {
                    // Error silencioso
                }
            }

            // Estado
            if (cumpleFiltros && (pagado || anuladas || cuotaFija || pendiente || planPago)) {
                val estado = factura.descEstado?.lowercase(Locale.getDefault()) ?: ""
                val coincide = (pagado && estado.contains("pagad")) ||
                        (anuladas && estado.contains("anulad")) ||
                        (cuotaFija && estado.contains("cuota")) ||
                        (pendiente && estado.contains("pendient")) ||
                        (planPago && estado.contains("plan"))
                if (!coincide) cumpleFiltros = false
            }

            if (cumpleFiltros) facturasFiltradas.add(factura)
        }

        binding.recyclerFacturas.adapter?.notifyDataSetChanged()
        actualizarGrafica()

        if (facturasFiltradas.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_hay_facturas_que_coincidan_con_los_filtros),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun actualizarGrafica() {
        val isBar = binding.switchChart.isChecked

        // Fechas para el eje X
        val fechas = facturasFiltradas.map { factura ->
            try {
                val inputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM.yy", Locale.getDefault())
                outputFormat.format(inputFormat.parse(factura.fecha) ?: Date())
            } catch (e: Exception) {
                factura.fecha
            }
        }

        if (isBar) {
            // Mostrar solo el BarChart
            binding.barChart.visibility = View.VISIBLE
            binding.lineChart.visibility = View.GONE

            val chart = binding.barChart
            val entriesPonta = facturasFiltradas.mapIndexedNotNull { index, factura ->
                factura.importeOrdenacion.toFloatOrNull()?.let { importe ->
                    BarEntry(index.toFloat(), floatArrayOf(importe * 0.7f, importe * 0.3f))
                }
            }
            if (entriesPonta.isNotEmpty()) {
                val dataSet = BarDataSet(entriesPonta, "Consumo")
                dataSet.setColors(Color.parseColor("#8BC34A"), Color.parseColor("#B3E5FC"))
                dataSet.stackLabels = arrayOf("Ponta", "Cheias")
                chart.data = BarData(dataSet)
                chart.axisLeft.axisMinimum = 0f
            } else {
                chart.clear()
            }
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index in fechas.indices) fechas[index] else ""
                }
            }
            chart.xAxis.granularity = 1f
            chart.xAxis.labelRotationAngle = -30f
            chart.xAxis.textColor = Color.DKGRAY
            chart.axisLeft.textColor = Color.DKGRAY
            chart.axisRight.isEnabled = false
            chart.legend.isEnabled = true
            chart.description.isEnabled = false
            chart.invalidate()
        } else {
            // Mostrar solo el LineChart
            binding.barChart.visibility = View.GONE
            binding.lineChart.visibility = View.VISIBLE

            val chart = binding.lineChart
            val entries = facturasFiltradas.mapIndexedNotNull { index, factura ->
                factura.importeOrdenacion.toFloatOrNull()?.let { importe ->
                    Entry(index.toFloat(), importe)
                }
            }
            if (entries.isNotEmpty()) {
                val dataSet = LineDataSet(entries, "Importe de facturas")
                dataSet.color = Color.parseColor("#8BC34A")
                dataSet.setCircleColor(Color.parseColor("#8BC34A"))
                dataSet.circleRadius = 7f
                dataSet.lineWidth = 3f
                dataSet.setDrawFilled(true)
                dataSet.fillColor = Color.parseColor("#8BC34A")
                dataSet.fillAlpha = 60
                dataSet.valueTextSize = 12f
                chart.data = LineData(dataSet)
                chart.axisLeft.axisMinimum = 0f
            } else {
                chart.clear()
            }
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index in fechas.indices) fechas[index] else ""
                }
            }
            chart.xAxis.granularity = 1f
            chart.xAxis.labelRotationAngle = -30f
            chart.xAxis.textColor = Color.DKGRAY
            chart.axisLeft.textColor = Color.DKGRAY
            chart.axisRight.isEnabled = false
            chart.legend.isEnabled = true
            chart.description.isEnabled = false
            chart.invalidate()
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setPositiveButton(getString(R.string.cerrar), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
