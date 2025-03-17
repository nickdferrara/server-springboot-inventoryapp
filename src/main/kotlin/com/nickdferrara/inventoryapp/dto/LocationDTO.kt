package com.nickdferrara.inventoryapp.dto

import java.util.*

data class LocationDTO(
    val id: UUID? = null,
    val name: String,
    val code: String,
    val type: String,
    val area: String? = null,
    val aisle: String? = null,
    val rack: String? = null,
    val shelf: String? = null,
    val bin: String? = null,
    val isActive: Boolean = true,
    val warehouseId: UUID,
    val warehouseName: String? = null
)