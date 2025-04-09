package com.example.hiberapp.factura

class FacturaProvider {
    companion object{
        val facturasList = listOf<Factura>(
            Factura(
                "31 Ago 2020",
                "Pendiente de pago",
                "54,56€",

            ),

            Factura(
                "31 Jul 2020",
                "Pendiente de pago",
                "54,56€",

            ),
            Factura(
                "31 Ago 2020",
                "Pendiente de pago",
                "67,54€",

            ),
            Factura(
                "22 Jun 2020",
                "Pendiente de pago",
                "56,38€",

            ),
            Factura(
                "31 May 2020",
                "",
                "57,38€",

            ),
            Factura(
                "22 Abr 2020",
                "",
                "65,23€",

            ),
            Factura(
                "20 Mar 2020",
                "",
                "74,54€",

            ),
            Factura(
                "22 Feb 2020",
                "",
                "67,64€",

            )
        )
    }
}