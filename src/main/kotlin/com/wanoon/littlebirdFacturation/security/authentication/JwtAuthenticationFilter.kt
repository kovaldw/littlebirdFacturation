package com.wanoon.littlebirdFacturation.security.authentication

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


//Filtre qui
/*
- lit le jeton d'authentication JWT dans l'en tete Authorization de toutes les demandes
- valide le jeton
- charge les détails de l'utilisateur asscoiés à ce jeton (recharge l'utilisateur)
- définit les détails de l'utilisateur dans Spring (configure l'utilisateur dans l'authentification)
 */
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var tokenProvider:JwtTokenProvider

    @Autowired
    lateinit var customUserDetailsService: CustomUserDetailsService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain)
    {
        var logger: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

        try {
            val jwt: String? = getJwtFromRequest(request)
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt))
            {
                val userId = tokenProvider.getUserIdFromJWT(jwt)
//                logger.error(userId.toString())
                val userDetails = customUserDetailsService.loadUserById(userId)
//                logger.error("userdeails authotities ${userDetails.authorities}")
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            logger.error("Impossible de définir l'utilisateur dans le context authentication de spring ${ex.message}")
        }

        filterChain.doFilter(request, response)
    }


    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }


}