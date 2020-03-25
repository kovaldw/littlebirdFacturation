package com.wanoon.littlebirdFacturation.payload.requests.societe

import javax.persistence.Column

class NewSocieteRequest(
        var name:String = "",

        @Column(columnDefinition = "Text")
        var url:String = "",

        var registrationNumber:String = "",

        var favoriteNumber:String = "",

        var termPayment:Int = 0,

        var type:String = "",

        var address:String = ""

        )
{

}