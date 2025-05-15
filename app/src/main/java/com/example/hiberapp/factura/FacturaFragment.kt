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

            // Aplicar filtros
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
                        todasLasFacturas.addAll(facturaResponse.facturas)

                        if (hayFiltrosAplicados) {
                            aplicarFiltrosGuardados()
                        } else {
                            facturasFiltradas.clear()
                            facturasFiltradas.addAll(todasLasFacturas)
                            binding.recyclerFacturas.adapter?.notifyDataSetChanged()
                        }
                    }
                } else {
                    showErrorDialog(
                        "Error al obtener las facturas: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            }

            override fun onFailure(call: Call<FacturaResponse>, t: Throwable) {
                mostrarCargando(false)
                showErrorDialog("Error de conexión: ${t.message}")
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
        // Guardar filtros actuales
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
                    Toast.makeText(context, "Error en fecha inicio", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "Error en fecha fin", Toast.LENGTH_SHORT).show()
                }
            }

            // Monto
            if (cumpleFiltros) {
                try {
                    val monto = factura.importeOrdenacion.toFloat()
                    if (monto < montoMinimo || monto > montoMaximo) cumpleFiltros = false
                } catch (_: Exception) {
                }
            }

            // Estado
            if (cumpleFiltros && (pagado || anuladas || cuotaFija || pendiente || planPago)) {
                val estado = factura.descEstado ?: ""
                val coincide = (pagado && estado == "pagado") ||
                        (anuladas && estado == "anulada") ||
                        (cuotaFija && estado == "cuota fija") ||
                        (pendiente && estado == "pendiente") ||
                        (planPago && estado == "plan de pago")
                if (!coincide) cumpleFiltros = false
            }

            if (cumpleFiltros) facturasFiltradas.add(factura)
        }

        binding.recyclerFacturas.adapter?.notifyDataSetChanged()

        if (facturasFiltradas.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "No hay facturas que coincidan con los filtros",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
