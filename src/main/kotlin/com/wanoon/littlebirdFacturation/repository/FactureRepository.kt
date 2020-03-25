package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Facture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface FactureRepository : JpaRepository<Facture, Long>, JpaSpecificationExecutor<Facture>
{

    fun findOneById(id:Long): Facture?
    fun findOneByIdAndDeletedFalse(id:Long): Facture?

    fun findOneByRefAndDeletedFalse(ref:String):Facture?
    fun existsByRefAndDeletedFalse(ref: String):Boolean

}