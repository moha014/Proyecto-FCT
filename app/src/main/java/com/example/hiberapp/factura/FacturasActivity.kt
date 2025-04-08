package com.example.hiberapp.factura

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiberapp.databinding.ActivityFacturasBinding
import com.example.hiberapp.factura.Adapter.FacturasAdapter

class FacturasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacturasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    fun initRecyclerView() {
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        binding.recyclerFacturas.layoutManager = LinearLayoutManager(this)
        binding.recyclerFacturas.adapter =
            FacturasAdapter(FacturasProvider.facturasList) { facturas ->
                onItemSelected(
                    facturas
                )
            }
        binding.recyclerFacturas.addItemDecoration(decoration) // Agregar la decoración al RecyclerView
    }

    fun onItemSelected(facturas: Facturas) {
        Toast.makeText(this, facturas.fecha, Toast.LENGTH_SHORT).show()
    }

}