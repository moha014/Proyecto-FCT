package Domain.factura

// Lógica de filtrado de facturas (extraída para test unitario)
fun filtrarFacturas(
    facturas: List<Factura>,
    estado: String? = null,
    minImporte: Double? = null,
    maxImporte: Double? = null
): List<Factura> {
    return facturas.filter { factura ->
        val cumpleEstado = estado == null || factura.descEstado == estado
        val importe = factura.importeOrdenacion.toDoubleOrNull() ?: 0.0
        val cumpleMin = minImporte == null || importe >= minImporte
        val cumpleMax = maxImporte == null || importe <= maxImporte
        cumpleEstado && cumpleMin && cumpleMax
    }
} 