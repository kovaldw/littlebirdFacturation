package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface SocieteRepository: JpaRepository<Societe, Long>, JpaSpecificationExecutor<Societe> {

    override fun findById(id:Long): Optional<Societe>

    fun existsByRegistrationNumber(registrationNumber:String): Boolean
    fun existsByRegistrationNumberAndDeletedFalse(registrationNumber:String): Boolean

    fun findOneByRegistrationNumber(registrationNumber: String):Societe?
    fun findOneByRegistrationNumberAndDeletedFalse(registrationNumber: String):Societe?

    fun findOneByIdAndDeletedFalse(id:Long):Societe?
}