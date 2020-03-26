package com.wanoon.littlebirdFacturation.model

import com.wanoon.littlebirdFacturation.security.model.User
import java.math.BigDecimal
import java.util.*
import javax.persistence.*


@Entity
class LigneFacturation (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null,

        var description:String = "",

        var price:BigDecimal = BigDecimal(0),

        var quantite:Long = 0,

        var extraFuel:BigDecimal = BigDecimal(0),

        var vat:BigDecimal = BigDecimal(0),

        var extraMile:BigDecimal = BigDecimal(0),

        var extraWeight:BigDecimal = BigDecimal(0),

        var finalPrice:BigDecimal = BigDecimal(0),

        var type:String = "",

        var colisId:Long = 0,

        var createdAt: Date = Date(),

        @ManyToOne
        var facture: Facture? = null,

        @ManyToOne
        var createdBy: User? = null,

        var updatedAt: Date = Date(),

        @ManyToOne
        var updatedBy: User? = null,

        var deleted:Boolean = false,

        var deletedAt: Date? = null,

        @ManyToOne
        var deletedBy: User? = null
)
{

}