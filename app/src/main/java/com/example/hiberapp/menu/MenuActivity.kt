package com.example.hiberapp.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiberapp.SmartSolarActivity
import com.example.hiberapp.databinding.ActivityMenuBinding
import com.example.hiberapp.ui.factura.FacturaFragment

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Uso del ViewBinding para acceder a los elementos del layout
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón que abre el fragment de facturas dentro de esta misma activity
        binding.btnFacturas.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FacturaFragment())
                .addToBackStack(null)
                .commit()
        }

        // Botón que inicia una activity diferente(SmartSolarActivity)
        binding.btnSmartSolar.setOnClickListener {
            val intent = Intent(this, SmartSolarActivity::class.java)
            startActivity(intent)
        }
    }
}