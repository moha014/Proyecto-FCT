package com.example.hiberapp.factura.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.factura.Facturas
import com.example.hiberapp.databinding.ItemFacturasBinding

class FacturasViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemFacturasBinding.bind(view)

    fun render(facturasModel: Facturas, onClickListener:(Facturas) -> Unit){
        binding.tvFecha.text = facturasModel.fecha
        binding.tvPrecio.text = facturasModel.precio
        binding.tvPago.text = facturasModel.pago

        itemView.setOnClickListener { onClickListener(facturasModel) }
    }
}