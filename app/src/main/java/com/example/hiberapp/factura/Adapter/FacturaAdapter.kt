package com.example.hiberapp.ui.factura

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiberapp.databinding.ItemFacturaBinding
import com.example.hiberapp.factura.Factura

// Adaptador para mostrar una lista de facturas en un RecyclerView
// Recibe la lista de facturas y una función que se ejecuta al hacer clic
class FacturaAdapter(
    // Lista con todas las facturas a mostrar
    private val facturas: List<Factura>,
    // Función que se ejecuta cuando tocas una factura
    private val onItemClick: () -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    // Metodo que crea una nueva vista para cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        // Inflamos el layout XML para cada item de factura
        val binding = ItemFacturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacturaViewHolder(binding)
    }

    // Metodo que conecta los datos de una factura con su vista correspondiente
    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = facturas[position] // Obtenemos la factura en esta posición
        holder.bind(factura, onItemClick) // Le pasamos los datos a la vista
    }

    // Metodo que dice cuántas facturas hay en total
    override fun getItemCount(): Int = facturas.size

    // Clase que representa cada elemento individual de la lista
    class FacturaViewHolder(private val binding: ItemFacturaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Metodo para rellenar la vista con los datos de una factura
        fun bind(factura: Factura, onItemClick: () -> Unit) {

            // Ponemos la fecha de la factura en el TextView correspondiente
            binding.tvFecha.text = try {
                val formatoEntrada = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val formatoSalida = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                val date = formatoEntrada.parse(factura.fecha)
                date?.let { formatoSalida.format(it) } ?: factura.fecha
            } catch (e: Exception) {
                factura.fecha
            }
            // Ponemos el estado de la factura
            binding.tvEstado.text = factura.descEstado

            // Intentamos convertir el importe a número para formatearlo bien
            try {
                val precioNumero = factura.importeOrdenacion.toDouble()
                // Formateamos el precio con 2 decimales y el símbolo del euro
                val precioFormateado = String.format("%.2f €", precioNumero)
                binding.tvImporte.text = precioFormateado
            } catch (e: Exception) {
                // Si no se puede convertir a número, mostramos el texto original
                binding.tvImporte.text = factura.importeOrdenacion
            }

            // Cambiamos el color del texto según el estado de la factura
            if (factura.descEstado == "Pagada") {
                binding.tvEstado.setTextColor(Color.GREEN) // Verde para pagadas
            } else if (factura.descEstado == "Pendiente de pago") {
                binding.tvEstado.setTextColor(Color.RED) // Rojo para pendientes
            }

            // Configuramos qué pasa cuando el usuario toca este elemento
            binding.facturaItemRoot.setOnClickListener {
                onItemClick() // Ejecutamos la función que nos pasaron
            }
        }
    }
}