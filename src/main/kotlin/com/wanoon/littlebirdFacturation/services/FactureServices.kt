package com.wanoon.littlebirdFacturation.services

import com.wanoon.littlebirdFacturation.model.Facture
import com.wanoon.littlebirdFacturation.model.LigneFacturation
import com.wanoon.littlebirdFacturation.payload.requests.facture.NewFactureRequest
import com.wanoon.littlebirdFacturation.repository.LigneFacturationRepository
import com.wanoon.littlebirdFacturation.security.services.UserServices
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

@Configuration
class FactureServices
{

    var logger:Logger = LoggerFactory.getLogger(FactureServices::class.java)

    @Autowired
    lateinit var ligneFacturationRepository: LigneFacturationRepository

    @Autowired
    lateinit var userServices: UserServices

    @Bean
    fun getInstanceFacture(): FactureServices {
        return FactureServices()
    }

    fun validerNewFactureRequest(facture:NewFactureRequest): MutableMap<String, Any>
    {
        var resultat = mutableMapOf<String, Any>()
        var valid = true; var message = "OK"

        try {
            var typesFactures: ArrayList<String> = arrayListOf("avoir", "du")


            if (facture.billingAddress == null || facture.billingAddress.trim() == "")
            {
                valid = false; message = "BillingAdress est vide ou nul"
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }

            if (facture.ref == null || facture.ref.trim() == "") {
                valid = false; message = "Ref est null ou vide"
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }

            if (facture.type == null || !typesFactures.contains(facture.type.trim()))
            {
                valid = false; message = "La valeur de type est incorrect"
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
//                return false
            }

            val validationLignesFacturations = this.validerLignesFacturations(facture.lignesFacturations, facture)
            if (validationLignesFacturations["valid"] == false)
            {
                valid = false;  message = validationLignesFacturations["message"] as String
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

        }
        catch (e:Exception)
        {
            valid = false; message = "Erreur: ${e.message.toString()}"
            resultat["valid"] = valid; resultat["message"] = message
            return resultat
        }

        resultat["valid"] = valid; resultat["message"] = message
        return resultat
    }

    fun validerLignesFacturations(lignesFacturation:ArrayList<Long?>, facture:NewFactureRequest):MutableMap<String, Any>
    {
        var resultat:MutableMap<String, Any> = mutableMapOf()

        var valid = true; var message = "OK"

        try
        {
            if (lignesFacturation == null || lignesFacturation.isEmpty())
            {
                valid = false; message = "Aucune ligne de facturation envoyee"
                resultat["valid"] = valid; resultat["message"] = message

                return resultat
            }

            for ((index,element) in lignesFacturation.withIndex())
            {
                if (element == null)
                {
                    valid = false
                    message = "La ligne de facturation $element est null"

                    break
                }
                val ligneFacturation = ligneFacturationRepository.findOneByIdAndDeletedFalse(element)
                if (ligneFacturation == null)
                {
                    valid = false
                    message = "La ligne de facuration $element n'existe pas"

                    break
                }
                if (ligneFacturation.facture != null)
                {
                    valid = false
                    message = "La ligne de facturation $element a déjà été facturée"

                    break
                }

                if (ligneFacturation.type != facture.type)
                {
                    valid = false; message = "Le type de la ligne de facturation $element ne correspond pas à celui de la facture"

                    break
                }
            }

        }catch (e:Exception)
        {
            valid = false; message = e.message.toString()
        }


        resultat["valid"] = valid
        resultat["message"] = message

        return resultat
    }


    fun recupererFacturationsFromIds(lignesFacturationsIds:ArrayList<Long?>): ArrayList<LigneFacturation>
    {
        var ligneFacturations = arrayListOf<LigneFacturation>()

        try
        {
            for (id in lignesFacturationsIds)
            {
                val ligneFacturation = ligneFacturationRepository.findOneById(id!!)
                ligneFacturations.add(ligneFacturation!!)
            }

        }
        catch (e:Exception)
        {

        }


        return ligneFacturations
    }

    fun calculerMontantFacture(lignesFacturations: ArrayList<LigneFacturation>):BigDecimal
    {
        var montant = BigDecimal(0)

        try
        {
            for (ligneFacturation in lignesFacturations)
            {
                montant += BigDecimal(ligneFacturation.quantite) * ligneFacturation.price
            }

        }
        catch (e:Exception)
        {

        }
        return montant
    }

    fun calculerMontantFactureTTC(lignesFacturations: ArrayList<LigneFacturation>):BigDecimal
    {
        var montant = BigDecimal(0)

        try
        {
            for (ligneFacturation in lignesFacturations)
            {
                montant += (BigDecimal(ligneFacturation.quantite) * ligneFacturation.price) + ligneFacturation.vat
            }
        }
        catch (e:Exception)
        {
            logger.error(e.toString())
        }

        return montant
    }

    fun setFactureForLigneFacturation(lignesFacturations: ArrayList<LigneFacturation>, facture:Facture)
    {
        val user = userServices.getCurrentUser()
        try
        {
            for (ligneFacturation in lignesFacturations)
            {
                ligneFacturation.facture = facture
                ligneFacturation.updatedAt = Date()
                ligneFacturation.updatedBy = user
                ligneFacturationRepository.save(ligneFacturation)
            }
        }
        catch (e:Exception)
        {

        }

    }

}