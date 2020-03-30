package com.wanoon.littlebirdFacturation.services

import com.wanoon.littlebirdFacturation.configuration.Translator
import com.wanoon.littlebirdFacturation.model.LigneFacturation
import com.wanoon.littlebirdFacturation.payload.requests.facture.NewLigneFacturationRequest
import com.wanoon.littlebirdFacturation.security.services.UserServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class LigneFacturationServices
{
    fun getInstanceLigneFacturationServices():LigneFacturationServices
    {
        return LigneFacturationServices()
    }

    @Autowired
    lateinit var userServices: UserServices


    fun validerNewLigneFacturationRequest(ligneFacturation: NewLigneFacturationRequest?): MutableMap<String, Any>
    {
        var resultat = mutableMapOf<String, Any>()
        var valid = true; var message = "OK"
        try
        {
            var typesLigneFacturations: ArrayList<String> = arrayListOf("avoir", "du")

            if (ligneFacturation == null)
            {
                valid = false; message = "La ligne de facturation est nullle"
                message = Translator.toLocale("ligneFacturation.new.ligneFacturationNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.colis == null || ligneFacturation.colis.compareTo(0) == 0)
            {
                valid = false; message = "Veuillez rensigner le colis à facturer"
                message = Translator.toLocale("ligneFacturation.new.colisNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.description == null || ligneFacturation.description.trim() == "")
            {
                valid = false; message = "Veuillez rensigner la description de la ligne de facturation"
                message = Translator.toLocale("ligneFacturation.new.colisNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.extraMile == null || ligneFacturation.extraMile == BigDecimal(0))
            {
                valid = false; message = "Veuillez rensigner extraMile"
                message = Translator.toLocale("ligneFacturation.new.extraMileNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.extraWeight == null || ligneFacturation.extraWeight == BigDecimal(0))
            {
                valid = false; message = "Veuillez rensigner extraWeight"
                message = Translator.toLocale("ligneFacturation.new.extraWeightNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.extraFuel == null || ligneFacturation.extraFuel == BigDecimal(0))
            {
                valid = false; message = "Veuillez rensigner extraFuel"
                message = Translator.toLocale("ligneFacturation.new.extraFuelNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.finalPrice == null || ligneFacturation.finalPrice == BigDecimal(0))
            {
                valid = false; message = "Veuillez rensigner finalPrice"
                message = Translator.toLocale("ligneFacturation.new.finalPriceNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.price == null || ligneFacturation.price == BigDecimal(0))
            {
                valid = false; message = "Veuillez rensigner price"
                message = Translator.toLocale("ligneFacturation.new.priceNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.quantite == null || ligneFacturation.quantite.compareTo(0) == 0)
            {
                valid = false; message = "Veuillez rensigner la quantite"
                message = Translator.toLocale("ligneFacturation.new.quantiteNull")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

            if (ligneFacturation.type == null || !typesLigneFacturations.contains(ligneFacturation.type.trim()))
            {
                valid = false; message = "Veuillez Vérifier la valeur du type"
                message = Translator.toLocale("ligneFacturation.new.typeIncorrect")
                resultat["valid"] = valid; resultat["message"] = message
                return resultat
            }

        }
        catch (e:Exception)
        {
            valid = false; message = "Erreur: ${e.message.toString()}"
            message = Translator.toLocale("err")
            resultat["valid"] = valid; resultat["message"] = message
            return resultat
        }

        resultat["valid"] = valid; resultat["message"] = message

        return resultat
    }


    fun validerListeNewLignesFacturations(lignesFacturations: ArrayList<NewLigneFacturationRequest?>?): MutableMap<String, Any>
    {
        var valid = true; var message = "Ok"
        var resultat = mutableMapOf<String, Any>()

        try
        {
            if (lignesFacturations.isNullOrEmpty())
            {
                valid = false; message = "Veuillez renseigner au moins une ligne de facturation"
                message = Translator.toLocale("ligneFacturation.new.listeLigneFacturationVide")
                resultat["valid"] = valid; resultat["message"] = message;
                return resultat
            }

            for ((index,ligneFacturation) in lignesFacturations.withIndex())
            {
                val validationLigne = this.validerNewLigneFacturationRequest(ligneFacturation)
                if (validationLigne["valid"] == false)
                {
                    valid = false; message = "Erreur à la ligne n° ${index+1} : " + validationLigne["message"]
                    var params = arrayOf("${index+1}", validationLigne["message"].toString())
                    message = Translator.toLocale("ligneFacturation.new.erreurAUneLigneFacturation", params)
                    resultat["valid"] = valid; resultat["message"] = message
                    return resultat
                }
            }
        }
        catch (e:Exception)
        {
            valid = false; message = "Erreur: ${e.message.toString()}"
            message = Translator.toLocale("err")
            resultat["valid"] = valid; resultat["message"] = message
            return resultat
        }

        resultat["valid"] = valid; resultat["message"] = message

        return resultat
    }


    fun formatterLignesFacturationsPourEnregistrement(lignesFacturationsAFormatter: ArrayList<NewLigneFacturationRequest?>): ArrayList<LigneFacturation> {

        var lignesFacturationsAEnregistrer = arrayListOf<LigneFacturation>()

        val user = userServices.getCurrentUser()

        try
        {
            for ((index, ligne) in lignesFacturationsAFormatter.withIndex())
            {
                if (ligne == null)
                {
                    break
                }

                var ligneFacturation = LigneFacturation()

                ligneFacturation.description = ligne.description.trim()
                ligneFacturation.price = ligne.price
                ligneFacturation.colisId = ligne.colis
                ligneFacturation.createdBy = user
                ligneFacturation.extraFuel = ligne.extraFuel
                ligneFacturation.extraMile = ligne.extraMile
                ligneFacturation.extraWeight = ligne.extraWeight
                ligneFacturation.finalPrice = ligne.finalPrice
                ligneFacturation.quantite = ligne.quantite
                ligneFacturation.type = ligne.type.trim()
                if (ligne.vat == null)
                {
                    ligneFacturation.vat = BigDecimal(0)
                }
                else
                {
                    ligneFacturation.vat = ligne.vat
                }

                lignesFacturationsAEnregistrer.add(ligneFacturation)
            }
        }
        catch (e:Exception)
        {

        }

        return lignesFacturationsAEnregistrer


    }

}