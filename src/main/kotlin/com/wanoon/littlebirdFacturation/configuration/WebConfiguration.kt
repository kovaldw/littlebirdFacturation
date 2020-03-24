package com.wanoon.littlebirdFacturation.configuration


import com.wanoon.littlebirdFacturation.security.authentication.CustomUserDetailsService
import com.wanoon.littlebirdFacturation.security.authentication.JwtAuthenticationEntryPoint
import com.wanoon.littlebirdFacturation.security.authentication.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


//ressemble a security.yml dans symfony


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class WebConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var customUserDetailsService: CustomUserDetailsService
    //Utilisé par spring boot security pour authentifier faire des verifications basées sur les roles

    @Autowired
    lateinit var unauthorizedHandler: JwtAuthenticationEntryPoint
    //classe permettant de renvoyer une erreur 401 (non authorisee) aux clients qui tenten d'accéder à une
    // ressource protégée sans authentification

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter()
    }
    //Filtre qui
    /*
    - lit le jeton d'authentication JWT dans l'en tete Authorization de toutes les demandes
    - valide le jeton
    - charge les détails de l'utilisateur asscoiés à ce jeton
    - définit les détails de l'utilisateur dans Spring
     */


    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
    @Bean
    fun getJwtAuthenticationEntryPoint():JwtAuthenticationEntryPoint
    {
        return JwtAuthenticationEntryPoint()
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }


    //Interface principale de spring security pour authentifier un utilisateur
    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
    }


    //autorise et bloque l'accès à certaines ressources
    override fun configure(http: HttpSecurity)
    {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                    .antMatchers("/api/auth/**") //les routes de login et register...
                        .permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                        .permitAll()
                .antMatchers("/**/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()

        //On ajoute le filtre de jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

    }


}