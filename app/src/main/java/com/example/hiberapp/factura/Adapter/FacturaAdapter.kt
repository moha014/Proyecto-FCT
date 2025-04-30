package com.example.hiberapp.ui.factura

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.databinding.ItemFacturaBinding
import com.example.hiberapp.factura.Factura

class FacturaAdapter(private val facturas: List<Factura>) : RecyclerView.Adapter<FacturaAdapter.ViewHolder>() {

    private var onItemClickListener: ((Factura) -> Unit)? = null

    fun setOnItemClickListener(listener: (Factura) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFacturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val factura = facturas[position]
        holder.bind(factura)
    }

    override fun getItemCount(): Int = facturas.size

    inner class ViewHolder(private val binding: ItemFacturaBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(facturas[position])
                }
            }
        }

        fun bind(facturas: Factura) {
            binding.tvFacturaId.text = factura.id
            binding.tvFacturaFecha.text = factura.fecha
            binding.tvFacturaImporte.text = "${factura.importe} €"
            binding.tvFacturaEstado.text = factura.estado
            binding.tvFacturaDescripcion.text = factura.descripcion
        }
    }
}