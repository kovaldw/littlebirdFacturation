package com.wanoon.littlebirdFacturation.configuration

import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*
import javax.servlet.http.HttpServletRequest
import com.wanoon.littlebirdFacturation.extensions.LOCALES
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource


@Configuration
class WebMvcConfiguration : AcceptHeaderLocaleResolver(), WebMvcConfigurer
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

//
//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }


    override fun resolveLocale(request: HttpServletRequest): Locale
    {
        var headerLang = request.getHeader("Accept-Language")

        if (headerLang == null || headerLang.isEmpty())
        {
            Locale.setDefault(Locale("fr"))
            return Locale.getDefault()
        }
        else
        {
            return Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES)
        }
    }


    @Bean
    fun messageSource(): ResourceBundleMessageSource
    {
        var rs = ResourceBundleMessageSource()

        rs.setBasename("messages")
        rs.setDefaultEncoding("UTF-8")
        rs.setUseCodeAsDefaultMessage(true)

        return rs
    }

}