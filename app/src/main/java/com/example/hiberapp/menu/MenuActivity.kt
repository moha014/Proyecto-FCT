package com.example.hiberapp.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hiberapp.SmartSolarActivity
import com.example.hiberapp.databinding.ActivityMenuBinding
import com.example.hiberapp.factura.FacturaFragment
import com.example.hiberapp.dataretrofit.api.ApiClient
import com.example.hiberapp.data.local.AppDatabase
import com.example.hiberapp.data.local.FacturaRepository
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // INICIALIZAR ROOM Y DESCARGAR DATOS DE LA API
        val db = AppDatabase.getDatabase(this)
        val repo = FacturaRepository(this, db.facturaDao())
        lifecycleScope.launch {
            repo.refreshFacturasFromApi()
        }

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

            val isActive = ApiClient.useMock
            binding.btnMockToggle.alpha = if (isActive) 1.0f else 0.7f

            val message = if (isActive) "Modo Mock ACTIVADO" else "Modo Mock DESACTIVADO"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            // ACTUALIZAR ROOM SEGÚN EL MODO
            lifecycleScope.launch {
                repo.refreshFacturasFromApi()
            }
        }

    }
}