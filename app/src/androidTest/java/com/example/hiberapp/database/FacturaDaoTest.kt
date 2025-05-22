package com.example.hiberapp.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.example.hiberapp.factura.Factura
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

/**
 * Test de instrumentación sencillo para FacturaDao usando Room en memoria.
 */
@RunWith(JUnit4::class)
class FacturaDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: FacturaDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.facturaDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertarYLeerFactura() = runBlocking {
        val factura = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        dao.insertarFactura(factura)
        val lista = dao.obtenerTodasLasFacturas().first()
        Assert.assertEquals(1, lista.size)
        Assert.assertEquals("Pagada", lista[0].descEstado)
        Assert.assertEquals("100.00", lista[0].importeOrdenacion)
    }

    @Test
    fun borrarFactura() = runBlocking {
        val factura = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        dao.insertarFactura(factura)
        val listaAntes = dao.obtenerTodasLasFacturas().first()
        Assert.assertEquals(1, listaAntes.size)
        dao.eliminarFactura(listaAntes[0])
        val listaDespues = dao.obtenerTodasLasFacturas().first()
        Assert.assertEquals(0, listaDespues.size)
    }

    @Test
    fun insertarVariasYLeer() = runBlocking {
        val f1 = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        val f2 = Factura(fecha = "02/01/2024", descEstado = "Pendiente", importeOrdenacion = "50.00")
        dao.insertarFacturas(listOf(f1, f2))
        val lista = dao.obtenerTodasLasFacturas().first()
        Assert.assertEquals(2, lista.size)
    }

    @Test
    fun buscarPorEstado() = runBlocking {
        val f1 = Factura(fecha = "01/01/2024", descEstado = "Pagada", importeOrdenacion = "100.00")
        val f2 = Factura(fecha = "02/01/2024", descEstado = "Pendiente", importeOrdenacion = "50.00")
        dao.insertarFacturas(listOf(f1, f2))
        val pagadas = dao.obtenerFacturasPorEstado("Pagada")
        Assert.assertEquals(1, pagadas.size)
        Assert.assertEquals("Pagada", pagadas[0].descEstado)
    }
} 