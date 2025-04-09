package com.example.hiberapp.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiberapp.SmartSolarActivity
import com.example.hiberapp.databinding.ActivityMenuBinding
import com.example.hiberapp.ui.factura.FacturaFragment

class MenuActivity : AppCompatActivity() {

    // Se declara el objeto de ViewBinding
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicializa el ViewBinding
        binding = ActivityMenuBinding.inflate(layoutInflater)

        // Establecer el contenido de la actividad
        setContentView(binding.root)

        // Configurar la acción de los botones
        binding.btnFacturas.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FacturaFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSmartSolar.setOnClickListener {
            // Aquí debes agregar la acción para navegar a la pantalla de Smart Solar
            val intent = Intent(this, SmartSolarActivity::class.java)
            startActivity(intent)
        }
    }
}