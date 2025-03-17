package com.nickdferrara.inventoryapp.repository

import com.nickdferrara.inventoryapp.models.Warehouse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WarehouseRepository : JpaRepository<Warehouse, UUID> {
    fun findByNameContainingIgnoreCase(name: String): List<Warehouse>
    fun findByCodeIgnoreCase(code: String): Optional<Warehouse>
    fun findByIsActiveTrue(): List<Warehouse>
}