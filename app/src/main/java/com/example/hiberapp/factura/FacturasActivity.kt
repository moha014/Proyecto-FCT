package com.example.hiberapp.factura

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.R
import com.example.hiberapp.factura.Adapter.FacturasAdapter

class FacturasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_facturas)
    }

    fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerFacturas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FacturasAdapter(FacturasProvider.facturasList)
    }

}