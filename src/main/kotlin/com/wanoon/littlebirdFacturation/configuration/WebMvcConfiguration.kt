package com.wanoon.littlebirdFacturation.configuration

import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class WebMvcConfiguration : WebMvcConfigurer
{
    companion object{
        var MAX_AGE_SECS:Long = 3600
    }

    override fun addCorsMappings(registry: CorsRegistry)
    {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS)
    }
}