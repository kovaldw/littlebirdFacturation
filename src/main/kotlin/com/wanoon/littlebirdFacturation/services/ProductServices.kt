package com.wanoon.littlebirdFacturation.services

import com.wanoon.littlebirdFacturation.configuration.Translator
import com.wanoon.littlebirdFacturation.payload.requests.product.NewProductRequest
import com.wanoon.littlebirdFacturation.payload.requests.societe.NewSocieteRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.Exception
import java.math.BigDecimal

@Configuration
class ProductServices
{

    @Bean
    fun getProductServicesInstance(): ProductServices {
        return ProductServices()
    }

    fun validerProductRequest(product:NewProductRequest):MutableMap<String, Any>
    {
        var resultat = mutableMapOf<String, Any>()
        var valid = true; var message = "Ok"

        try {
            if (product.description == null || product.description.trim() == "") {
                valid = false; message = "Description vide ou nul"
                var params = arrayOf("Description")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message
                return resultat;
//                return false
            }

            if (product.gazoil == null || product.gazoil == BigDecimal(0)) {
                valid = false; message = "Gazoil vide ou null"
                var params = arrayOf("Gazoil")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message;
                return resultat;
//                return false
            }

            if (product.price == null || product.price == BigDecimal(0))
            {
                valid = false; message = "Price vide ou null"
                var params = arrayOf("Price")
                message = Translator.toLocale("validationRequest.error", params)
                resultat["valid"] = valid; resultat["message"] = message;

                return resultat
//                return false
            }

            if (product.productCode == null || product.productCode.trim() == "")
            {
                valid = false; message = "ProductCode est vide ou null"
                var params = arrayOf("ProductCode")
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

    }

}