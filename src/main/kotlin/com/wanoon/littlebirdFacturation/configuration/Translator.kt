package com.wanoon.littlebirdFacturation.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Component


@Component
class Translator
{

//    @Autowired
//    lateinit var messageSource:ResourceBundleMessageSource
//
//    @Bean
//    fun getTranslatorInstance(): Translator {
//        return Translator()
//    }
//
//    @Bean
//    fun getResourceBundleMessageSourceInstance(): ResourceBundleMessageSource {
//        return ResourceBundleMessageSource()
//    }

    @Autowired
    fun Translator(messageSource: ResourceBundleMessageSource) {
        Translator.messageSource = messageSource
    }


    companion object
    {
        lateinit var messageSource: ResourceBundleMessageSource

        fun toLocale(msgCode: String, params:Array<String>? = null): String
        {
            var locale = LocaleContextHolder.getLocale()

//            messageSource.setBasename("messages")

//            logger.error(messageSource.toString())

            return messageSource.getMessage(msgCode, params, locale)
        }
    }








}