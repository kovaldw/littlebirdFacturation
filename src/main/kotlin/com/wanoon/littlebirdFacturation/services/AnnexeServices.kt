package com.wanoon.littlebirdFacturation.services

import NewAnnexeRequest
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

    fun validerAnnexeRequest(annexe: NewAnnexeRequest):Boolean
    {

        try {
            if (annexe.accountName == null || annexe.accountName == "")
            {
                return false
            }
            if (annexe.email == null || annexe.email == "")
            {
                return false
            }
            if (annexe.defaultContact == null || annexe.defaultContact == "")
            {
                return false
            }
            if (annexe.tel == null || annexe.tel == "")
            {
                return false
            }
            if (annexe.societeId == null)
            {
                return false
            }
        }
        catch (e:Exception)
        {
            return false
        }

        return true
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