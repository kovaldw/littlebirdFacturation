package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SocieteRepository: JpaRepository<Societe, Long> {

    override fun findById(id:Long): Optional<Societe>

    fun existsByRegistrationNumber(registrationNumber:String): Boolean

    fun findOneByRegistrationNumber(registrationNumber: String):Societe?
}