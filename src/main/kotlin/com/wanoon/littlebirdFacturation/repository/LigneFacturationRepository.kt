package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.LigneFacturation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface LigneFacturationRepository : JpaRepository<LigneFacturation, Long>, JpaSpecificationExecutor<LigneFacturation>
{
    fun findOneById(id:Long):LigneFacturation?
    fun findOneByIdAndDeletedFalse(id:Long):LigneFacturation?
}