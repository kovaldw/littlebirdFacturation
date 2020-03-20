package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.PermissionList
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionListRepository : JpaRepository<PermissionList, Long> {
}