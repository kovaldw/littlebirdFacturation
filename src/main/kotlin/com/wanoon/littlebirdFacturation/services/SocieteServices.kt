package com.wanoon.littlebirdFacturation.services

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

    fun validerSociete(societe: NewSocieteRequest):Boolean
    {


        try {
            if (societe.address == null || societe.address == "")
            {
                return false
            }
            if (societe.favoriteNumber == null || societe.favoriteNumber == "")
            {
                return false
            }
            if (societe.name == null || societe.name == "")
            {
                return false
            }
            if (societe.registrationNumber == null || societe.registrationNumber == "")
            {
                return false
            }
            if (societe.termPayment == null || societe.termPayment == 0)
            {
                return false
            }
            if (societe.type == null || societe.type == "")
            {
                return false
            }
            if (societe.url == null || societe.url == "")
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