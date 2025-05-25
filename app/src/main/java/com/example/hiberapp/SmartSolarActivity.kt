package com.example.hiberapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hiberapp.smartSolar.Fragments.DetallesFragment
import com.example.hiberapp.smartSolar.Fragments.EnergiaFragment
import com.example.hiberapp.smartSolar.Fragments.MiInstalacionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// Activity para mostrar información de Smart Solar con pestañas
class SmartSolarActivity : AppCompatActivity() {

    // Variables para el ViewPager (deslizar entre pestañas) y TabLayout (pestañas superiores)
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    // Metodo que se ejecuta cuando se crea la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_solar)

        // Configuramos el botón de volver (flecha hacia atrás)
        findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            finish() // Cierra esta activity y vuelve a la anterior
        }

        // También el texto "Volver" funciona igual que la flecha
        findViewById<TextView>(R.id.TextBack).setOnClickListener {
            finish()
        }

        // Conectamos el ViewPager y TabLayout con sus elementos del layout
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Asignamos el adaptador al ViewPager
        viewPager.adapter = ViewPagerAdapter(this)

        // Conectamos las pestañas con el ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Asignamos nombres a cada pestaña según su posición
            tab.text = when (position) {
                0 -> getString(R.string.mi_instalaci_n) // Primera pestaña: "Mi instalación"
                1 -> getString(R.string.energ_a)        // Segunda pestaña: "Energía"
                2 -> getString(R.string.detalles)       // Tercera pestaña: "Detalles"
                else -> ""
            }
        }.attach()
    }

    // Adaptador que maneja qué fragment mostrar en cada pestaña
    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        // Metodo que dice cuántas pestañas hay en total
        override fun getItemCount(): Int = 3

        // Metodo que crea el fragment correspondiente a cada posición
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MiInstalacionFragment() // Primera pestaña
                1 -> EnergiaFragment()        // Segunda pestaña
                2 -> {
                    // Tercera pestaña - DetallesFragment con datos JSON
                    val fragment = DetallesFragment()

                    // Creamos un Bundle con información de la instalación solar
                    val bundle = Bundle().apply {
                        putString(
                            "jsonData", """
                            {
                                "CAU(Código Autoconsumo)": "ES0021000000001994LJ1FA000",
                                "Estado solicitud alta autoconsumidor": "No hemos recibido ninguna solicitud de autoconsumo",
                                "Tipo autoconsumo": "Con excedentes y compensación individual - Consumo",
                                "Compensación de excedentes": "Precio PVPC",
                                "Potencia de instalación": "5kWp"
                            }
                        """.trimIndent()
                        )
                    }
                    // Le pasamos los datos al fragment
                    fragment.arguments = bundle
                    fragment
                }
                else -> MiInstalacionFragment() // Por defecto, primera pestaña
            }
        }
    }
}