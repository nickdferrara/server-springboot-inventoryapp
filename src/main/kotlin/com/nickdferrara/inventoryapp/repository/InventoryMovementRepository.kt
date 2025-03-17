package com.nickdferrara.inventoryapp.repository

import com.nickdferrara.inventoryapp.models.InventoryItem
import com.nickdferrara.inventoryapp.models.InventoryMovement
import com.nickdferrara.inventoryapp.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface InventoryMovementRepository : JpaRepository<InventoryMovement, UUID> {
    fun findByInventoryItem(inventoryItem: InventoryItem): List<InventoryMovement>
    fun findByType(type: String): List<InventoryMovement>
    fun findByTimestampBetween(start: LocalDateTime, end: LocalDateTime): List<InventoryMovement>
    fun findByCreatedBy(user: User): List<InventoryMovement>
    fun findByReferenceNumber(referenceNumber: String): List<InventoryMovement>
}