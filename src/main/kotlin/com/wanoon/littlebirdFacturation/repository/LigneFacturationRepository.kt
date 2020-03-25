package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.LigneFacturation
import org.springframework.data.jpa.repository.JpaRepository

interface LigneFacturationRepository : JpaRepository<LigneFacturation, Long>
{
    fun findOneById(id:Long):LigneFacturation?
    fun findOneByIdAndDeletedFalse(id:Long):LigneFacturation?
}