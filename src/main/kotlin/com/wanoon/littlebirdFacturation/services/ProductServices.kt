package com.wanoon.littlebirdFacturation.services

import com.wanoon.littlebirdFacturation.payload.requests.product.NewProductRequest
import com.wanoon.littlebirdFacturation.payload.requests.societe.NewSocieteRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class ProductServices
{

    @Bean
    fun getProductServicesInstance(): ProductServices {
        return ProductServices()
    }

    fun validerProductRequest(product:NewProductRequest):Boolean
    {
        if (product.description == "" || product.description == null) return false

        if (product.gazoil == null || product.gazoil == BigDecimal(0)) return false

        if (product.price == null || product.price == BigDecimal(0)) return false

        if (product.productCode == null || product.productCode == "") return false

        if (product.vat == null) return false

        return true
    }

}