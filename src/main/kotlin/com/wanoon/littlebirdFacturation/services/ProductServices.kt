package com.wanoon.littlebirdFacturation.services

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
        var valid = true; var messsage = "Ok"

        try {
            if (product.description == null || product.description.trim() == "") {
                valid = false; messsage = "Description vide ou nul"
                resultat["valid"] = valid; resultat["message"] = messsage
                return resultat;
//                return false
            }

            if (product.gazoil == null || product.gazoil == BigDecimal(0)) {
                valid = false; messsage = "Gazoil vide ou null"
                resultat["valid"] = valid; resultat["message"] = messsage;
                return resultat;
//                return false
            }

            if (product.price == null || product.price == BigDecimal(0))
            {
                valid = false; messsage = "Price vide ou null"
                resultat["valid"] = valid; resultat["message"] = messsage;

                return resultat
//                return false
            }

            if (product.productCode == null || product.productCode.trim() == "")
            {
                valid = false; messsage = "ProductCode est vide ou null"
                resultat["valid"] = valid; resultat["message"] = messsage
                return resultat
//                return false
            }

        }
        catch (e:Exception)
        {
            valid = false; messsage = "Erreur: ${e.message.toString()}"
            resultat["valid"] = valid; resultat["message"] = messsage
            return resultat
//            return false
        }


        resultat["valid"] = valid; resultat["message"] = messsage
        return resultat

    }

}