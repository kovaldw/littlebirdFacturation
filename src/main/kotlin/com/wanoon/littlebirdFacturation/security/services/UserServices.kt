package com.wanoon.littlebirdFacturation.security.services

import com.wanoon.littlebirdFacturation.security.authentication.UserPrincipal
import com.wanoon.littlebirdFacturation.security.model.User
import com.wanoon.littlebirdFacturation.security.repository.UserRepository
import com.wanoon.littlebirdFacturation.services.ApiServices
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
class UserServices
{
    @Bean
    fun getUserServicesInstance():UserServices
    {
        return UserServices()
    }

    companion object
    {
        var logger: Logger = LoggerFactory.getLogger(ApiServices::class.java)
    }

    @Autowired
    lateinit var userRepository: UserRepository

    fun getCurrentUser(): User?
    {
        try
        {
            val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
            var user = userRepository.findById(principal.id.toString().toLong())
//            logger.error(principal.id.toString())
            return user
//            return null
        }
        catch (e:Exception)
        {
            logger.error(e.message)
            return null
        }
    }

}