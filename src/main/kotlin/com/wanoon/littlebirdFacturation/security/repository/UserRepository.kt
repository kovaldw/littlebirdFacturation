package com.wanoon.littlebirdFacturation.security.repository

import com.wanoon.littlebirdFacturation.security.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<User, Int>
{
    fun findByUsernameOrEmail(username:String, email:String = username):User?

    fun findById(id:Long): User?

    fun existsByUsername(username: String):Boolean

    fun existsByEmail(email: String):Boolean

}