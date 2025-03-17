package com.nickdferrara.inventoryapp.dto

import java.time.LocalDateTime
import java.util.*

data class InventoryMovementDTO(
    val id: UUID? = null,
    val type: String,
    val inventoryItemId: UUID,
    val productName: String? = null,
    val quantity: Int,
    val sourceLocationId: UUID? = null,
    val sourceLocationCode: String? = null,
    val destinationLocationId: UUID? = null,
    val destinationLocationCode: String? = null,
    val reason: String? = null,
    val referenceNumber: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null,
    val createdById: UUID? = null,
    val createdByUsername: String? = null
)