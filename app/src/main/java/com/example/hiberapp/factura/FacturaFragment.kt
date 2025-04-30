package com.example.hiberapp.factura

import android.app.AlertDialog
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiberapp.databinding.FragmentFacturaBinding
import com.example.hiberapp.factura.viewmodel.FacturaViewModel
import com.example.hiberapp.factura.databinding.FragmentFacturaBinding  // Asegúrate de que coincide con tu paquete
import com.example.hiberapp.ui.factura.FacturaAdapter

class FacturaFragment : Fragment() {

    private var _binding: FragmentFacturaBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FacturaViewModel
    private lateinit var adapter: FacturaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacturaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this)[FacturaViewModel::class.java]

        // Configurar RecyclerView y adaptador
        setupRecyclerView()

        // Observadores para los LiveData
        setupObservers()

        // Cargar datos
        viewModel.loadFacturas()

        // Configurar botón de filtro
        binding.btnFiltrar.setOnClickListener {
            // Aquí implementarías la navegación a la pantalla de filtros
            // Por ejemplo:
            // findNavController().navigate(R.id.action_facturaFragment_to_filtrarFacturasFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = FacturaAdapter(emptyList()) { factura ->
            // Mostrar diálogo al hacer clic en una factura
            AlertDialog.Builder(requireContext())
                .setTitle("Información")
                .setMessage("Esta funcionalidad aún no está disponible")
                .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }

        binding.recyclerView.apply {  // Asegúrate de que el ID coincide con tu layout
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@FacturaFragment.adapter
        }
    }

    private fun setupObservers() {
        // Observar cambios en la lista de facturas
        viewModel.facturas.observe(viewLifecycleOwner) { facturas ->
            adapter.updateFacturas(facturas)

            // Mostrar mensaje si no hay facturas
            if (facturas.isEmpty()) {
                binding.tvNoFacturas.visibility = View.VISIBLE  // Asegúrate de que este ID existe
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.tvNoFacturas.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        // Observar estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE  // Asegúrate de que este ID existe
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}