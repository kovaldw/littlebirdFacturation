package com.wanoon.littlebirdFacturation.services

import com.wanoon.littlebirdFacturation.security.authentication.UserPrincipal
import com.wanoon.littlebirdFacturation.security.model.User
import com.wanoon.littlebirdFacturation.security.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.math.log


@Configuration
class ApiServices
{

    @Bean
    fun getInstancesApiServices():ApiServices
    {
        return ApiServices()
    }


}