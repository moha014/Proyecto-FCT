package com.example.hiberapp.factura.Adapter

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.factura.Facturas
import com.example.hiberapp.R

class FacturasViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val fecha = view.findViewById<TextView>(R.id.tvFecha)
    val pago = view.findViewById<TextView>(R.id.tvPago)
    val precio = view.findViewById<TextView>(R.id.tvPrecio)

    fun render(facturasModel: Facturas){
        fecha.text = facturasModel.fecha
        pago.text = facturasModel.pago
        precio.text = facturasModel.precio
    }
}