package com.example.hiberapp.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hiberapp.factura.Factura
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first
import org.junit.runner.RunWith

/**
 * Test unitario sencillo para FacturaDao usando Room en memoria.
 * Comprueba que se pueden insertar y leer facturas correctamente.
 */
@RunWith(AndroidJUnit4::class)
class FacturaDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: FacturaDao

    @Before
    fun setUp() {
        // Creamos la base de datos en memoria (no se guarda en disco)
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.facturaDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertarYLeerFactura() = runBlocking {
        // Creamos una factura de prueba
        val factura = Factura(
            fecha = "01/01/2024",
            descEstado = "Pagada",
            importeOrdenacion = "100.00"
        )
        // Insertamos la factura
        dao.insertarFactura(factura)
        // Leemos todas las facturas
        val lista = dao.obtenerTodasLasFacturas().first()
        // Comprobamos que hay una factura y que los datos coinciden
        assertEquals(1, lista.size)
        assertEquals("Pagada", lista[0].descEstado)
        assertEquals("100.00", lista[0].importeOrdenacion)
    }

    @Test
    fun borrarFactura() = runBlocking {
        val factura = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        dao.insertarFactura(factura)
        val listaAntes = dao.obtenerTodasLasFacturas().first()
        assertEquals(1, listaAntes.size)
        dao.eliminarFactura(listaAntes[0])
        val listaDespues = dao.obtenerTodasLasFacturas().first()
        assertEquals(0, listaDespues.size)
    }

    @Test
    fun insertarVariasYLeer() = runBlocking {
        val f1 = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        val f2 = Factura(fecha = "02/01/2024", descEstado = "Pendiente", importeOrdenacion = "50.00")
        dao.insertarFacturas(listOf(f1, f2))
        val lista = dao.obtenerTodasLasFacturas().first()
        assertEquals(2, lista.size)
    }

    @Test
    fun buscarPorEstado() = runBlocking {
        val f1 = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        val f2 = Factura(fecha = "02/01/2024", descEstado = "Pendiente", importeOrdenacion = "50.00")
        dao.insertarFacturas(listOf(f1, f2))
        val pagadas = dao.obtenerFacturasPorEstado("Pagada")
        assertEquals(1, pagadas.size)
        assertEquals("Pagada", pagadas[0].descEstado)
    }
} 