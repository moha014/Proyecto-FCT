package com.example.hiberapp.factura

class FacturasProvider {
    companion object{
        val facturasList = listOf<Facturas>(
            Facturas(
                "31 Ago 2020",
                "Pendiente de pago",
                "54,56€"),

            Facturas(
                "31 Jul 2020",
                "Pendiente de pago",
                "54,56€"
            ),
            Facturas(
                "31 Ago 2020",
                "Pendiente de pago",
                "67,54€"
            ),
            Facturas(
                "22 Jun 2020",
                "Pendiente de pago",
                "56,38€"
            ),
            Facturas(
                "31 May 2020",
                "",
                "57,38€"
            ),
            Facturas(
                "22 Abr 2020",
                "",
                "65,23€"
            ),
            Facturas(
                "20 Mar 2020",
                "",
                "74,54€"
            ),
            Facturas(
                "22 Feb 2020",
                "",
                "67,64€"
            )
        )
    }
}