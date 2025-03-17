package com.nickdferrara.inventoryapp.dto

import java.math.BigDecimal
import java.util.*

data class ProductDTO(
    val id: UUID? = null,
    val name: String,
    val description: String,
    val sku: String,
    val barcode: String? = null,
    val price: BigDecimal,
    val costPrice: BigDecimal,
    val weight: Double? = null,
    val dimensions: String? = null,
    val category: String,
    val brand: String? = null,
    val supplier: String? = null,
    val minStockLevel: Int,
    val maxStockLevel: Int? = null,
    val isActive: Boolean = true,
    val imageUrl: String? = null,
    val warehouseId: UUID? = null,
    val totalStock: Int? = null
)