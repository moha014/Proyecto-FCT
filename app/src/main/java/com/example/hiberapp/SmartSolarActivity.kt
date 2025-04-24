package com.example.hiberapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hiberapp.smartSolar.Fragments.DetallesFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONException
import org.json.JSONObject

class SmartSolarActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_smart_solar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SmartSolarPrincipal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backArrow: ImageView = findViewById(R.id.backArrow)
        val backText: TextView = findViewById(R.id.TextBack)

        val goBack = View.OnClickListener {
            finish()
        }

        backArrow.setOnClickListener(goBack)
        backText.setOnClickListener(goBack)

        // Inicializar TabLayout y ViewPager2
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Configurar el adaptador
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Conectar TabLayout con ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.mi_instalaci_n)
                1 -> tab.text = "Energía"
                2 -> tab.text = "Detalles"
            }
        }.attach()

        // Procesar el JSON y mostrar la información en el fragmento DetallesFragment
        val jsonData = """
            {
                "CAU(Código Autoconsumo)": "ES0021000000001994LJ1FA000",
                "Estado solicitud alta autoconsumidor": "No hemos recibido ninguna solicitud de autoconsumo",
                "Tipo autoconsumo": "Con excedentes y compensación individual - Consumo",
                "Compensación de excedentes": "Precio PVPC",
                "Potencia de instalación": "5kWp"
            }
        """.trimIndent()

        val DetallesFragment = supportFragmentManager.findFragmentById(R.id.viewPager) as? DetallesFragment
        DetallesFragment?.displayJsonData(jsonData)
    }

    // Adaptador para el ViewPager2
    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            val jsonData = """
            {
                "CAU(Código Autoconsumo)": "ES0021000000001994LJ1FA000",
                "Estado solicitud alta autoconsumidor": "No hemos recibido ninguna solicitud de autoconsumo",
                "Tipo autoconsumo": "Con excedentes y compensación individual - Consumo",
                "Compensación de excedentes": "Precio PVPC",
                "Potencia de instalación": "5kWp"
            }
        """.trimIndent()

            return when (position) {
                0 -> MiInstalacionFragment()
                1 -> EnergiaFragment()
                2 -> {
                    val fragment = DetallesFragment()
                    val bundle = Bundle()
                    bundle.putString("jsonData", jsonData)
                    fragment.arguments = bundle
                    fragment
                }
                else -> MiInstalacionFragment()
            }
        }
    }

}

// Fragmento para Mi Instalación
class MiInstalacionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mi_instalacion, container, false)
        return view
    }
}

// Fragmento para Energía
class EnergiaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_energia, container, false)
        return view
    }
}



