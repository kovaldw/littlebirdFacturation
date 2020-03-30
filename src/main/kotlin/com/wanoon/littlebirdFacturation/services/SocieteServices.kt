package com.wanoon.littlebirdFacturation.services

import com.wanoon.littlebirdFacturation.configuration.Translator
import com.wanoon.littlebirdFacturation.model.Societe
import com.wanoon.littlebirdFacturation.payload.requests.societe.NewSocieteRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SocieteServices {

    @Bean
    fun getSocieteServices():SocieteServices
    {
        return SocieteServices()
    }

    fun validerSociete(societe: NewSocieteRequest):MutableMap<String, Any>
    {
        var resultat = mutableMapOf<String, Any>()
        var valid = true; var message = "OK"


        try {
            if (societe.address == null || societe.address.trim() == "")
            {
                valid = false; message = "Address vide ou nul"
                var params = arrayOf("Adresse")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (societe.favoriteNumber == null || societe.favoriteNumber.trim() == "")
            {
                valid = false; message = "FavoriteNumber est vide ou nul"
                var params = arrayOf("FavoriteNumber")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message;
                return resultat
//                return false
            }
            if (societe.name == null || societe.name.trim() == "")
            {
                valid = false; message = "Name est vide ou nul"
                var params = arrayOf("Name")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (societe.registrationNumber == null || societe.registrationNumber.trim() == "")
            {
                valid = false; message = "RegistrationNumber est vide ou nul"
                var params = arrayOf("RegistrationNumber")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (societe.termPayment == null || societe.termPayment == 0)
            {
                valid = false; message = "TermPayment est null ou vide"
                var params = arrayOf("TermPayment")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (societe.type == null || societe.type.trim() == "")
            {
                valid = false; message = "Type est null ou vide"
                var params = arrayOf("Type")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (societe.url == null || societe.url.trim() == "")
            {
                valid = false; message = "Url est null ou vide"
                var params = arrayOf("Url")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
        }
        catch (e:Exception)
        {
            valid = false; message = "Erreur: ${e.message.toString()}"
            message = Translator.toLocale("err")
            resultat["valid"] = valid; resultat["message"] = message
            return resultat
//            return false
        }

        resultat["valid"] = valid; resultat["message"] = message
        return resultat
//        return true
    }


    fun setSociete(oldSociete: Societe, newSociete: NewSocieteRequest):Societe
    {
        oldSociete.address = newSociete.address.trim()
        oldSociete.favoriteNumber = newSociete.address.trim()
        oldSociete.name = newSociete.name.trim()
        oldSociete.registrationNumber = newSociete.registrationNumber.trim()
        oldSociete.termPayment = newSociete.termPayment
        oldSociete.type = newSociete.type.trim()
        oldSociete.url = newSociete.url.trim()

        return oldSociete
    }

}