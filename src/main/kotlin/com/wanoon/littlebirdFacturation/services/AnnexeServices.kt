package com.wanoon.littlebirdFacturation.services

import NewAnnexeRequest
import com.wanoon.littlebirdFacturation.configuration.Translator
import com.wanoon.littlebirdFacturation.model.Annexe
import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnnexeServices
{

    @Bean
    fun getAnnexeServices():AnnexeServices
    {
        return AnnexeServices()
    }

    fun validerAnnexeRequest(annexe: NewAnnexeRequest):MutableMap<String, Any>
    {

        var resultat = mutableMapOf<String, Any>()
        var valid = true; var message = "Ok"
        try
        {

            if (annexe.accountName == null || annexe.accountName.trim() == "")
            {
                valid = false; message = "AccountName est vie ou null"
                var params = arrayOf("AccountName")
                message = Translator.toLocale("validationRequest.error", params)

                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (annexe.email == null || annexe.email.trim() == "")
            {
                valid = false; message = "Email est vide ou null"
                var params = arrayOf("Email")
                message = Translator.toLocale("validationRequest.error", params)

                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (annexe.defaultContact == null || annexe.defaultContact.trim() == "")
            {
                valid = false; message = "DefaultContact est null ou vide"
                var params = arrayOf("DefaultContact")
                message = Translator.toLocale("validationRequest.error", params)

                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (annexe.tel == null || annexe.tel.trim() == "")
            {
                valid = false; message = "Tel est vide ou nul"
                var params = arrayOf("Tel")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }
            if (annexe.societeId == null)
            {
                valid = false; message = "Societe est vide ou null"
                var params = arrayOf("SocieteId")
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

    fun setAnnexe(oldAnnexe: Annexe, newAnnexe: Annexe): Annexe
    {
        oldAnnexe.VAT = newAnnexe.VAT
        oldAnnexe.accountName = oldAnnexe.accountName
        oldAnnexe.bankSwift = newAnnexe.bankSwift
        oldAnnexe.city = newAnnexe.city
        oldAnnexe.country = newAnnexe.country
        oldAnnexe.currency = newAnnexe.currency
        oldAnnexe.defaultContact = newAnnexe.defaultContact
        oldAnnexe.email = newAnnexe.email
        oldAnnexe.iban = newAnnexe.iban
        oldAnnexe.isBillingAddress = newAnnexe.isBillingAddress
        oldAnnexe.language = newAnnexe.language
        oldAnnexe.tel = newAnnexe.tel
        oldAnnexe.website = newAnnexe.website
        oldAnnexe.zipCode = newAnnexe.zipCode
        oldAnnexe.societe = newAnnexe.societe

        return oldAnnexe
    }

}