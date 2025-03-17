package com.nickdferrara.inventoryapp.service

import com.nickdferrara.inventoryapp.dto.InventoryItemDTO
import com.nickdferrara.inventoryapp.models.InventoryItem
import com.nickdferrara.inventoryapp.repository.InventoryItemRepository
import com.nickdferrara.inventoryapp.repository.LocationRepository
import com.nickdferrara.inventoryapp.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class InventoryItemService(
    private val inventoryItemRepository: InventoryItemRepository,
    private val productRepository: ProductRepository,
    private val locationRepository: LocationRepository
) {

    fun getAllInventoryItems(): List<InventoryItemDTO> {
        return inventoryItemRepository.findAll().map { it.toDTO() }
    }

    fun getInventoryItemById(id: UUID): InventoryItemDTO? {
        return inventoryItemRepository.findByIdOrNull(id)?.toDTO()
    }

    fun getInventoryItemsByProductId(productId: UUID): List<InventoryItemDTO> {
        val product = productRepository.findByIdOrNull(productId) ?: return emptyList()
        return inventoryItemRepository.findByProduct(product).map { it.toDTO() }
    }

    fun getInventoryItemsByLocationId(locationId: UUID): List<InventoryItemDTO> {
        val location = locationRepository.findByIdOrNull(locationId) ?: return emptyList()
        return inventoryItemRepository.findByLocation(location).map { it.toDTO() }
    }

    fun getInventoryItemsByStatus(status: String): List<InventoryItemDTO> {
        return inventoryItemRepository.findByStatus(status).map { it.toDTO() }
    }

    @Transactional
    fun createInventoryItem(inventoryItemDTO: InventoryItemDTO): InventoryItemDTO? {
        val product = productRepository.findByIdOrNull(inventoryItemDTO.productId) ?: return null
        val location = locationRepository.findByIdOrNull(inventoryItemDTO.locationId) ?: return null

        val inventoryItem = InventoryItem(
            quantity = inventoryItemDTO.quantity,
            status = inventoryItemDTO.status,
            lotNumber = inventoryItemDTO.lotNumber,
            serialNumber = inventoryItemDTO.serialNumber,
            expiryDate = inventoryItemDTO.expiryDate,
            lastCheckedDate = inventoryItemDTO.lastCheckedDate,
            notes = inventoryItemDTO.notes,
            product = product,
            location = location
        )

        return inventoryItemRepository.save(inventoryItem).toDTO()
    }

    @Transactional
    fun updateInventoryItem(id: UUID, inventoryItemDTO: InventoryItemDTO): InventoryItemDTO? {
        val existingItem = inventoryItemRepository.findByIdOrNull(id) ?: return null
        val product = productRepository.findByIdOrNull(inventoryItemDTO.productId) ?: return null
        val location = locationRepository.findByIdOrNull(inventoryItemDTO.locationId) ?: return null

        with(existingItem) {
            quantity = inventoryItemDTO.quantity
            status = inventoryItemDTO.status
            lotNumber = inventoryItemDTO.lotNumber
            serialNumber = inventoryItemDTO.serialNumber
            expiryDate = inventoryItemDTO.expiryDate
            lastCheckedDate = inventoryItemDTO.lastCheckedDate
            notes = inventoryItemDTO.notes
            this.product = product
            this.location = location
        }

        return inventoryItemRepository.save(existingItem).toDTO()
    }

    @Transactional
    fun adjustInventoryQuantity(id: UUID, newQuantity: Int): InventoryItemDTO? {
        val inventoryItem = inventoryItemRepository.findByIdOrNull(id) ?: return null
        inventoryItem.quantity = newQuantity
        return inventoryItemRepository.save(inventoryItem).toDTO()
    }

    @Transactional
    fun deleteInventoryItem(id: UUID): Boolean {
        return if (inventoryItemRepository.existsById(id)) {
            inventoryItemRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    private fun InventoryItem.toDTO(): InventoryItemDTO {
        return InventoryItemDTO(
            id = id,
            productId = product?.id ?: throw IllegalStateException("Inventory item must have a product"),
            productName = product?.name,
            productSku = product?.sku,
            locationId = location?.id ?: throw IllegalStateException("Inventory item must have a location"),
            locationCode = location?.code,
            quantity = quantity,
            status = status,
            lotNumber = lotNumber,
            serialNumber = serialNumber,
            expiryDate = expiryDate,
            lastCheckedDate = lastCheckedDate,
            notes = notes
        )
    }
}