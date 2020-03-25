package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor


interface ProductRepository : JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>
{
    fun findOneByIdAndDeletedFalse(id:Long):Product?
    fun findOneById(id:Long):Product?

    fun existsByProductCode(productCode:String):Boolean
    fun existsByProductCodeAndDeletedFalse(productCode: String):Boolean
    fun findOneByProductCodeAndDeletedFalse(productCode: String): Product?



}