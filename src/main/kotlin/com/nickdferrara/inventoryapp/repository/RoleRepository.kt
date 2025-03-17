package com.nickdferrara.inventoryapp.repository

import com.nickdferrara.inventoryapp.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, UUID> {
    fun findByName(name: String): Optional<Role>
}