package com.nickdferrara.inventoryapp.dto

import java.util.*

data class WarehouseDTO(
    val id: UUID? = null,
    val name: String,
    val code: String,
    val address: String,
    val city: String,
    val state: String? = null,
    val country: String,
    val zipCode: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val isActive: Boolean = true,
    val locationCount: Int? = null
)