package com.example.hiberapp.factura.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.factura.Facturas
import com.example.hiberapp.R

class FacturasAdapter(private val facturasList: List<Facturas>, private val onClickListener:(Facturas) -> Unit): RecyclerView.Adapter<FacturasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FacturasViewHolder(layoutInflater.inflate(R.layout.item_facturas, parent, false))
    }
    override fun onBindViewHolder(holder: FacturasViewHolder, position: Int) {
        val item = facturasList[position]
        holder.render(item, onClickListener)
    }
    override fun getItemCount(): Int = facturasList.size
}