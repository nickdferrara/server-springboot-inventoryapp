package com.nickdferrara.inventoryapp.service

import com.nickdferrara.inventoryapp.dto.ProductDTO
import com.nickdferrara.inventoryapp.models.Product
import com.nickdferrara.inventoryapp.repository.InventoryItemRepository
import com.nickdferrara.inventoryapp.repository.ProductRepository
import com.nickdferrara.inventoryapp.repository.WarehouseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val warehouseRepository: WarehouseRepository,
    private val inventoryItemRepository: InventoryItemRepository
) {

    fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll().map { it.toDTO() }
    }

    fun getProductById(id: UUID): ProductDTO? {
        return productRepository.findByIdOrNull(id)?.toDTO()
    }

    fun searchProducts(keyword: String): List<ProductDTO> {
        return productRepository.search(keyword).map { it.toDTO() }
    }

    fun getLowStockProducts(): List<ProductDTO> {
        return productRepository.findLowStockProducts().map { it.toDTO() }
    }

    @Transactional
    fun createProduct(productDTO: ProductDTO): ProductDTO {
        val product = Product(
            name = productDTO.name,
            description = productDTO.description,
            sku = productDTO.sku,
            barcode = productDTO.barcode,
            price = productDTO.price,
            costPrice = productDTO.costPrice,
            weight = productDTO.weight,
            dimensions = productDTO.dimensions,
            category = productDTO.category,
            brand = productDTO.brand,
            supplier = productDTO.supplier,
            minStockLevel = productDTO.minStockLevel,
            maxStockLevel = productDTO.maxStockLevel,
            isActive = productDTO.isActive,
            imageUrl = productDTO.imageUrl
        )

        productDTO.warehouseId?.let { warehouseId ->
            warehouseRepository.findByIdOrNull(warehouseId)?.let { warehouse ->
                product.warehouse = warehouse
            }
        }

        return productRepository.save(product).toDTO()
    }

    @Transactional
    fun updateProduct(id: UUID, productDTO: ProductDTO): ProductDTO? {
        val existingProduct = productRepository.findByIdOrNull(id) ?: return null

        with(existingProduct) {
            name = productDTO.name
            description = productDTO.description
            sku = productDTO.sku
            barcode = productDTO.barcode
            price = productDTO.price
            costPrice = productDTO.costPrice
            weight = productDTO.weight
            dimensions = productDTO.dimensions
            category = productDTO.category
            brand = productDTO.brand
            supplier = productDTO.supplier
            minStockLevel = productDTO.minStockLevel
            maxStockLevel = productDTO.maxStockLevel
            isActive = productDTO.isActive
            imageUrl = productDTO.imageUrl
        }

        productDTO.warehouseId?.let { warehouseId ->
            existingProduct.warehouse = warehouseRepository.findByIdOrNull(warehouseId)
        }

        return productRepository.save(existingProduct).toDTO()
    }

    @Transactional
    fun deleteProduct(id: UUID): Boolean {
        return if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    private fun Product.toDTO(): ProductDTO {
        val totalStock = inventoryItemRepository.getTotalQuantityByProduct(this) ?: 0
        return ProductDTO(
            id = id,
            name = name,
            description = description,
            sku = sku,
            barcode = barcode,
            price = price,
            costPrice = costPrice,
            weight = weight,
            dimensions = dimensions,
            category = category,
            brand = brand,
            supplier = supplier,
            minStockLevel = minStockLevel,
            maxStockLevel = maxStockLevel,
            isActive = isActive,
            imageUrl = imageUrl,
            warehouseId = warehouse?.id,
            totalStock = totalStock
        )
    }
}