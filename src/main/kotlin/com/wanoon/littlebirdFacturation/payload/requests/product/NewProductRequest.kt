package com.wanoon.littlebirdFacturation.payload.requests.product

import java.math.BigDecimal

class NewProductRequest(

        var productCode:String = "",

        var description:String = "",

        var price: BigDecimal = BigDecimal(0),

        var gazoil: BigDecimal = BigDecimal(0),

        var vat:Boolean = false

        )
{

}