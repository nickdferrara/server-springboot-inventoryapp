package com.nickdferrara.inventoryapp.repository

import com.nickdferrara.inventoryapp.models.Location
import com.nickdferrara.inventoryapp.models.Warehouse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LocationRepository : JpaRepository<Location, UUID> {
    fun findByWarehouse(warehouse: Warehouse): List<Location>
    fun findByCodeIgnoreCase(code: String): Optional<Location>
    fun findByWarehouseAndCodeIgnoreCase(warehouse: Warehouse, code: String): Optional<Location>
    fun findByIsActiveTrue(): List<Location>
}