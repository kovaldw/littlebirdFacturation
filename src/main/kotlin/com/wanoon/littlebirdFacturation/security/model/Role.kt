package com.wanoon.littlebirdFacturation.security.model

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class Role (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long = -1,

        @Enumerated(EnumType.STRING)
        @NaturalId
        var name:RoleName = RoleName.ROLE_USER

)
