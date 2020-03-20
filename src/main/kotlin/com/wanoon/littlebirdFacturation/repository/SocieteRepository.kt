package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.data.jpa.repository.JpaRepository

interface SocieteRepository: JpaRepository<Societe, Long> {
}