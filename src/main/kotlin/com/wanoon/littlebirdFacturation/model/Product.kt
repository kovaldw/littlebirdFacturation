package com.wanoon.littlebirdFacturation.model

import com.wanoon.littlebirdFacturation.security.model.User
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
class Product (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null,

        var productCode:String = "",

        var description:String = "",

        var price:BigDecimal = BigDecimal(0),

        var gazoil:BigDecimal = BigDecimal(0),

        var vat:Boolean = false,

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
)
{
}