package com.example.hiberapp.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hiberapp.SmartSolarActivity
import com.example.hiberapp.databinding.ActivityMenuBinding
import com.example.hiberapp.factura.FacturaFragment
import com.example.hiberapp.DataRetrofit.ApiClient

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFacturas.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FacturaFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSmartSolar.setOnClickListener {
            val intent = Intent(this, SmartSolarActivity::class.java)
            startActivity(intent)
        }

        binding.btnMockToggle.setOnClickListener {
            ApiClient.enableMock(this)
            val message = if (ApiClient.useMock) "Modo Mock ACTIVADO" else "Modo Mock DESACTIVADO"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
