package com.wanoon.littlebirdFacturation.security.authentication

import com.wanoon.littlebirdFacturation.security.model.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

open class UserPrincipal(
        val id: Long?,
        var name:String,
        private val username:String,

        @JsonIgnore
        var email:String,

        @JsonIgnore
        private var password:String,
//        private var authorities:MutableCollection<out GrantedAuthority>
        private var authorities: Collection<GrantedAuthority>? = null

): UserDetails
{

    override fun isAccountNonExpired():Boolean = true

    override fun isAccountNonLocked():Boolean = true

    override fun isCredentialsNonExpired():Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority>? = authorities

    override fun isEnabled():Boolean = true
    override fun getUsername(): String = username


    override fun equals(other:Any?):Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as UserPrincipal
        return Objects.equals(id, that.id)
    }
    override fun hashCode():Int {
        return Objects.hash(id)
    }

    override fun getPassword(): String = password

    companion object
    {
        fun create(user: User):UserPrincipal
        {
            val authorities: List<GrantedAuthority> = user.roles.stream().map { role -> SimpleGrantedAuthority(role.name.name) }.collect(Collectors.toList())


            return UserPrincipal(
                    user.id,
                    user.name,
                    user.username,
                    user.email,
                    user.password,
                    authorities
            )
        }
    }
}