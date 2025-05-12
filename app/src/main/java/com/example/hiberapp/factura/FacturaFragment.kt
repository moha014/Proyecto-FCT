package com.example.hiberapp.factura

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiberapp.DataRetrofit.ApiClient
import com.example.hiberapp.databinding.FragmentFacturaBinding
import com.example.hiberapp.ui.factura.FacturaAdapter
import com.example.hiberapp.ui.factura.FiltrarFacturasFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacturaFragment : Fragment() {
    private var _binding: FragmentFacturaBinding? = null
    private val binding get() = _binding!!

    private val facturas = mutableListOf<Factura>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFacturaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.recyclerFacturas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFacturas.adapter = FacturaAdapter(facturas) {
            AlertDialog.Builder(requireContext())
                .setTitle("Información")
                .setMessage("Esta funcionalidad aún no está disponible")
                .setPositiveButton("Cerrar", null)
                .show()
        }

        // Configurar los clicks de la toolbar
        setupToolbar()

        obtenerFacturas()
    }

    private fun setupToolbar() {
        // Botón de flecha atrás
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.consumoBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Botón para abrir FiltrarFacturasFragment
        binding.ivFilter.setOnClickListener {
            val filtrarFragment = FiltrarFacturasFragment.newInstance()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, filtrarFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun obtenerFacturas() {
        val apiService = ApiClient.getService(requireContext())
        val call = apiService.obtenerFacturas()

        call.enqueue(object : Callback<FacturaResponse> {
            override fun onResponse(call: Call<FacturaResponse>, response: Response<FacturaResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { facturaResponse ->
                        facturas.clear()
                        facturas.addAll(facturaResponse.facturas)
                        binding.recyclerFacturas.adapter?.notifyDataSetChanged()
                    }
                } else {
                    showErrorDialog("Error al obtener las facturas: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FacturaResponse>, t: Throwable) {
                showErrorDialog("Error de conexión: ${t.message}")
            }
        })
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