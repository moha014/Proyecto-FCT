package com.example.hiberapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hiberapp.smartSolar.Fragments.DetallesFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SmartSolarActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_solar)

        // Botón de vuelta
        findViewById<ImageView>(R.id.backArrow).setOnClickListener { finish() }
        findViewById<TextView>(R.id.TextBack).setOnClickListener { finish() }

        // Configurar el ViewPager y los Tabs
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.mi_instalaci_n)
                1 -> "Energía"
                2 -> "Detalles"
                else -> ""
            }
        }.attach()
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MiInstalacionFragment()
                1 -> EnergiaFragment()
                2 -> {
                    val fragment = DetallesFragment()
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
                    fragment.arguments = bundle
                    fragment
                }

                else -> MiInstalacionFragment()
            }
        }
    }
}

// Fragment para Mi Instalación
class MiInstalacionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_mi_instalacion, container, false)
}

// Fragment para Energía
class EnergiaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_energia, container, false)
}
