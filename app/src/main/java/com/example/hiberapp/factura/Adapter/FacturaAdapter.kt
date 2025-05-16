package com.example.hiberapp.ui.factura

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.databinding.ItemFacturaBinding
import com.example.hiberapp.factura.Factura

class FacturaAdapter(
    private val facturas: List<Factura>,
    private val onItemClick: () -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val binding = ItemFacturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacturaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = facturas[position]
        holder.bind(factura, onItemClick)
    }

    override fun getItemCount(): Int = facturas.size

    class FacturaViewHolder(private val binding: ItemFacturaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(factura: Factura, onItemClick: () -> Unit) {
            // Asignar los datos de la factura a las vistas
            binding.tvFecha.text = factura.fecha
            binding.tvEstado.text = factura.descEstado


            try {
                val precioNumero = factura.importeOrdenacion.toDouble()
                val precioFormateado = String.format("%.2f €", precioNumero)
                binding.tvImporte.text = precioFormateado
            } catch (e: Exception) {
                binding.tvImporte.text = factura.importeOrdenacion
            }

            if (factura.descEstado == "Pagada") {
                binding.tvEstado.setTextColor(Color.GREEN) // Color verde para "Pagado"
            } else if (factura.descEstado == "Pendiente de pago") {
                binding.tvEstado.setTextColor(Color.RED) // Color rojo para "Pendiente de pago"
            }

            binding.facturaItemRoot.setOnClickListener {
                onItemClick()
            }
        }
    }
}