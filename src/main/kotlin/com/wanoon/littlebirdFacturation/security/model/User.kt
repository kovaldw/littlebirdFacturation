package com.wanoon.littlebirdFacturation.security.model


import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
class User (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = -1,

        var name:String = "",

        var username:String = "",

        var email:String = "",

        var passwordEncoder:String? = "",

        var password: String = "",

        @ManyToMany(fetch = FetchType.LAZY)
        var roles: Set<Role> = HashSet()

)

{

}
