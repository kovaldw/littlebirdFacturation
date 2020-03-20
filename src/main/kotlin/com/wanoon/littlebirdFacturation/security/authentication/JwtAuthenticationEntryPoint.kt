package com.wanoon.littlebirdFacturation.security.authentication

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//Permet de lancer le schéma d'authentification
//Classe permettant de renvoyer une erreur 401 (non authorisee) aux clients qui tente
// d'accéder à une ressource protégée sans authentification

@Configuration
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {



    companion object
    {
        var logger: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(httpServletRequest:HttpServletRequest,
                 httpServletResponse:HttpServletResponse,
                 e:AuthenticationException)
    {
        logger.error("Exception levee lors de l'authentification - {}", e.message)
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
    }

}