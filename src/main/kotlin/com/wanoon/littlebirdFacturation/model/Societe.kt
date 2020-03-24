package com.wanoon.littlebirdFacturation.model

import com.wanoon.littlebirdFacturation.security.model.User
import java.util.*
import javax.persistence.*

@Entity
class Societe(
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

        var address:String = "",

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