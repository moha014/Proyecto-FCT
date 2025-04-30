package com.example.hiberapp.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiberapp.DataRetrofit.ApiCliente
import com.example.hiberapp.SmartSolarActivity
import com.example.hiberapp.databinding.ActivityMenuBinding
import com.example.hiberapp.factura.FacturaFragment


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnFacturas.setOnClickListener {
            val intent = Intent(this, FacturaFragment::class.java)
            startActivity(intent)
        }

        binding.btnSmartSolar.setOnClickListener {
            val intent = Intent(this, SmartSolarActivity::class.java)
            startActivity(intent)
        }

        binding.btnToggleMock.setOnClickListener {
            val useMock = !binding.btnToggleMock.isChecked
            ApiCliente.setUseMock(useMock)

            val message = if (useMock) "Usando datos mock" else "Usando datos reales"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            binding.btnToggleMock.isChecked = useMock
        }
    }
}