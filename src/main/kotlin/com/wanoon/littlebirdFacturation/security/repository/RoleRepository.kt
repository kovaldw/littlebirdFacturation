package com.wanoon.littlebirdFacturation.security.repository

import com.wanoon.littlebirdFacturation.security.model.Role
import com.wanoon.littlebirdFacturation.security.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name:RoleName):Role?
}