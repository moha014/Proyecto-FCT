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

    private val mockFacturas = listOf(
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

        // Configurar el título
        binding.tvFacturas.text = "Facturas"

        // Configurar el RecyclerView
        setupRecyclerView()

        // Configurar navegación hacia atrás
        setupBackNavigation()

        // Configurar botón de filtro
        setupFilterButton()
    }

    private fun setupRecyclerView() {
        val adapter = FacturaAdapter(mockFacturas) { factura ->
            AlertDialog.Builder(requireContext())
                .setTitle("Información")
                .setMessage("Esta funcionalidad aún no está disponible")
                .setPositiveButton("Cerrar", null)
                .show()
        }

        binding.recyclerFacturas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFacturas.adapter = adapter
    }

    private fun setupBackNavigation() {
        val backArrow: ImageView = binding.toolbar.findViewById(R.id.backArrow)
        val consumoBack: TextView = binding.toolbar.findViewById(R.id.consumoBack)

        val goBack = View.OnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        backArrow.setOnClickListener(goBack)
        consumoBack.setOnClickListener(goBack)
    }

    private fun setupFilterButton() {
        val filterIcon: ImageView = binding.toolbar.findViewById(R.id.ivFilter)
        filterIcon.setOnClickListener {
            // Usar el ID de contenedor de la actividad principal
            val mainActivity = requireActivity()
            val containerId = android.R.id.content  // Este es el ID del contenedor principal de cualquier actividad

            mainActivity.supportFragmentManager.beginTransaction()
                .add(containerId, FiltrarFacturasFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}