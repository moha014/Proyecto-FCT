package com.example.hiberapp.ui.factura

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.databinding.ItemFacturaBinding
import com.example.hiberapp.factura.Factura

class FacturaAdapter(
    private val facturas: List<Factura>,
    private val onItemClick: (Factura) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    inner class FacturaViewHolder(private val binding: ItemFacturaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(factura: Factura) {
            binding.tvFecha.text = factura.fecha
            binding.tvPrecio.text = factura.precio
            binding.tvEstado.text = factura.estado ?: ""

            binding.facturaItemRoot.setOnClickListener {
                onItemClick(factura)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val binding = ItemFacturaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FacturaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        holder.bind(facturas[position])
    }

    override fun getItemCount() = facturas.size
}
