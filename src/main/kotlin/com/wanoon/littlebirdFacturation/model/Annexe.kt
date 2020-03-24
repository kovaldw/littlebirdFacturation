package com.wanoon.littlebirdFacturation.model

import com.wanoon.littlebirdFacturation.security.model.User
import java.util.*
import javax.persistence.*

@Entity
class Annexe (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null,

        var accountName:String = "",

        var country:String = "",

        var city:String = "",

        var zipCode:String = "",

        var tel:String = "",

        var address:String = "",

        var iban:String = "",

        var bankSwift:String = "",

        @Column(length = 300)
        var email:String = "",

        var website:String = "",

        var defaultContact:String = "",

        var language:String = "",

        var currency:String = "",

        var VAT: String = "",

        var isBillingAddress: Boolean = false,

        @ManyToOne
        var societe: Societe? = null,

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