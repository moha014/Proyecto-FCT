package com.example.hiberapp.factura

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test unitario sencillo para la función de filtrado de facturas.
 */
class FiltradoFacturasTest {
    private val facturas = listOf(
        Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00"),
        Factura(fecha = "02/01/2024", descEstado = "Pendiente", importeOrdenacion = "50.00"),
        Factura(fecha = "03/01/2024", descEstado = "Pagada", importeOrdenacion = "200.00")
    )

    @Test
    fun filtrarPorEstado() {
        val pagadas = filtrarFacturas(facturas, estado = "Pagada")
        assertEquals(2, pagadas.size)
    }

    @Test
    fun filtrarPorImporteMinimo() {
        val min100 = filtrarFacturas(facturas, minImporte = 100.0)
        assertEquals(2, min100.size)
    }

    @Test
    fun filtrarPorImporteMaximo() {
        val max100 = filtrarFacturas(facturas, maxImporte = 100.0)
        assertEquals(2, max100.size)
    }

    @Test
    fun filtrarPorEstadoYImporte() {
        val pagadasYMin150 = filtrarFacturas(facturas, estado = "Pagada", minImporte = 150.0)
        assertEquals(1, pagadasYMin150.size)
        assertEquals("200.00", pagadasYMin150[0].importeOrdenacion)
    }
} 