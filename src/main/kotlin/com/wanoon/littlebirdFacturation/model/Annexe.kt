package com.wanoon.littlebirdFacturation.model

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
        var societe: Societe? = null
)
{

}