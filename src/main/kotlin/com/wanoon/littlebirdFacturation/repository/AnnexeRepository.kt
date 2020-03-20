package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Annexe
import org.springframework.data.jpa.repository.JpaRepository

interface AnnexeRepository : JpaRepository<Annexe, Long> {
}