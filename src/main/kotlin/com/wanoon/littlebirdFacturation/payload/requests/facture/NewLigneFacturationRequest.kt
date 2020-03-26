package com.wanoon.littlebirdFacturation.payload.requests.facture

import java.math.BigDecimal

class NewLigneFacturationRequest (
        var description:String = "",

        var price: BigDecimal = BigDecimal(0),

        var quantite:Long = 0,

        var extraFuel: BigDecimal = BigDecimal(0),

        var vat: BigDecimal = BigDecimal(0),

        var extraMile: BigDecimal = BigDecimal(0),

        var extraWeight: BigDecimal = BigDecimal(0),

        var finalPrice: BigDecimal = BigDecimal(0),

        var type:String = "",

        var colis:Long = 0
)

{
}