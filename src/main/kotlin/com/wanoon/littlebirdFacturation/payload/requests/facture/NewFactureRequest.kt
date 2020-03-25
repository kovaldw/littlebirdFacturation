package com.wanoon.littlebirdFacturation.payload.requests.facture

import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class NewFactureRequest (
        var billingAddress:String = "",

        var ref:String = "",

        var amount: BigDecimal = BigDecimal(0),

        var filePath:String = "",

        var type:String = "",

        var lignesFacturations : ArrayList<Long?> = arrayListOf()
)
{

}