package com.example.hiberapp.ui.factura

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiberapp.R
import com.example.hiberapp.databinding.FragmentFacturaBinding
import com.example.hiberapp.factura.Factura

class FacturaFragment : Fragment() {

    private var _binding: FragmentFacturaBinding? = null
    private val binding get() = _binding!!

    // Lista de facturas de ejemplo para mostrar en la pantalla
    private val facturas = listOf(
        Factura("31 Ago 2020", "Pendiente de pago", "54,56 €"),
        Factura("31 Jul 2020", "Pendiente de pago", "67,54 €"),
        Factura("22 Jun 2020", "Pendiente de pago", "56,38 €"),
        Factura("31 May 2020", null, "57,38 €"),
        Factura("22 Abr 2020", null, "65,23 €"),
        Factura("20 Mar 2020", null, "74,54 €"),
        Factura("22 Feb 2020", null, "67,64 €")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacturaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFacturas.text = "Facturas"

        // Inicialización del listado de facturas
        setupRecyclerView()
        // Configuración de los botones para volver atrás
        setupBackNavigation()
        // Preparacion del botón(imagen) de filtro
        setupFilterButton()
    }

    // Configuracion del RecyclerView con los datos y un popup al pulsar una factura
    private fun setupRecyclerView() {
        val adapter = FacturaAdapter(facturas) {
            AlertDialog.Builder(requireContext())
                .setTitle("Información")
                .setMessage("Esta funcionalidad aún no está disponible")
                .setPositiveButton("Cerrar", null)
                .show()
        }

        binding.recyclerFacturas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFacturas.adapter = adapter
    }

    // Configuración del botón de volver atrás
    private fun setupBackNavigation() {
        val backArrow = binding.toolbar.findViewById<ImageView>(R.id.backArrow)
        val consumoBack = binding.toolbar.findViewById<TextView>(R.id.consumoBack)

        val goBackListener = View.OnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        backArrow.setOnClickListener(goBackListener)
        consumoBack.setOnClickListener(goBackListener)
    }

    // Abre FiltrarFacturasFragment de filtros al pulsar la imagen de filtros
    private fun setupFilterButton() {
        val filterIcon = binding.toolbar.findViewById<ImageView>(R.id.ivFilter)

        filterIcon.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(android.R.id.content, FiltrarFacturasFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    // Metodo para limpiar las elecciones de filtro
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
