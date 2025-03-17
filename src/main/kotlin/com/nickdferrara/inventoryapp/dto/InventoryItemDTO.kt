package com.nickdferrara.inventoryapp.dto

import java.time.LocalDateTime
import java.util.*

data class InventoryItemDTO(
    val id: UUID? = null,
    val productId: UUID,
    val productName: String? = null,
    val productSku: String? = null,
    val locationId: UUID,
    val locationCode: String? = null,
    val quantity: Int,
    val status: String,
    val lotNumber: String? = null,
    val serialNumber: String? = null,
    val expiryDate: LocalDateTime? = null,
    val lastCheckedDate: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null
)