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

    class FacturaViewHolder(private val binding: ItemFacturaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(factura: Factura, onItemClick: () -> Unit) {
// ... existing code ...
        }
    }
} 