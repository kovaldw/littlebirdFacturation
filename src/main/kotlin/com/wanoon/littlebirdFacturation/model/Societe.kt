package com.wanoon.littlebirdFacturation.model

import javax.persistence.*

@Entity
class Societe (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null,

        var name:String = "",

        @Column(columnDefinition = "Text")
        var url:String = "",

        var registrationNumber:String = "",

        var favoriteNumber:String = "",

        var termPayment:Int = 0,

        var type:String = "",

        var address:String = ""

) {
}