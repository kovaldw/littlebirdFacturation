package com.wanoon.littlebirdFacturation.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Societe (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null
) {
}