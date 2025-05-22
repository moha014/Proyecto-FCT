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
import com.example.hiberapp.R
import kotlinx.coroutines.launch

// Activity principal que funciona como menú de la aplicación
class MenuActivity : AppCompatActivity() {

    // ViewBinding para acceder a los elementos de la interfaz
    private lateinit var binding: ActivityMenuBinding

    // Método que se ejecuta cuando se crea la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuramos el ViewBinding para usar el layout
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la base de datos local (Room) y el repositorio
        val db = AppDatabase.getDatabase(this)
        val repo = FacturaRepository(this, db.facturaDao())
        // Cargamos los datos desde la API de forma asíncrona
        lifecycleScope.launch {
            repo.refreshFacturasFromApi()
        }

        // Botón para ir a la pantalla de facturas
        binding.btnFacturas.setOnClickListener {
            // Abrimos el FacturaFragment reemplazando el contenido actual
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FacturaFragment())
                .addToBackStack(null) // Permitimos volver atrás
                .commit()
        }

        // Botón para ir a la pantalla de SmartSolar
        binding.btnSmartSolar.setOnClickListener {
            // Creamos un Intent para abrir otra Activity
            val intent = Intent(this, SmartSolarActivity::class.java)
            startActivity(intent)
        }

        // Botón para activar/desactivar el modo mock (datos de prueba)
        binding.btnMockToggle.setOnClickListener {
            // Cambiamos el estado del modo mock
            ApiClient.enableMock(this)

            // Comprobamos si está activo o no
            val isActive = ApiClient.useMock
            // Cambiamos la opacidad del botón según el estado
            binding.btnMockToggle.alpha = if (isActive) 1.0f else 0.7f

            // Mostramos un mensaje al usuario
            val message =
                if (isActive) getString(R.string.modo_mock_activado) else getString(R.string.modo_mock_desactivado)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            // Recargamos las facturas con el nuevo modo
            lifecycleScope.launch {
                repo.refreshFacturasFromApi()
            }
        }
    }
}