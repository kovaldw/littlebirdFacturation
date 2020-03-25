package com.wanoon.littlebirdFacturation.model

import com.wanoon.littlebirdFacturation.security.model.User
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
class Facture (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null,

        var billingAddress:String = "",

        var ref:String = "",

        var amount:BigDecimal = BigDecimal(0),

        var amountTTC:BigDecimal = BigDecimal(0),

        var filePath:String = "",

        var type:String = "",

        var createdAt: Date = Date(),

        @ManyToOne
        var createdBy: User? = null,

        var updatedAt: Date = Date(),

        @ManyToOne
        var updatedBy: User? = null,

        var deleted:Boolean = false,

        var deletedAt: Date? = null,

        @ManyToOne
        var deletedBy: User? = null
) {
}