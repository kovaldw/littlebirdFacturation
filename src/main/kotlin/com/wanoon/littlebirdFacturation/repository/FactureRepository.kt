package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Facture
import org.springframework.data.jpa.repository.JpaRepository

interface FactureRepository : JpaRepository<Facture, Long> {
}