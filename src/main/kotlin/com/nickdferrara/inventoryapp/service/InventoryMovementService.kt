package com.nickdferrara.inventoryapp.service

import com.nickdferrara.inventoryapp.dto.InventoryMovementDTO
import com.nickdferrara.inventoryapp.models.InventoryMovement
import com.nickdferrara.inventoryapp.repository.InventoryItemRepository
import com.nickdferrara.inventoryapp.repository.InventoryMovementRepository
import com.nickdferrara.inventoryapp.repository.LocationRepository
import com.nickdferrara.inventoryapp.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class InventoryMovementService(
    private val inventoryMovementRepository: InventoryMovementRepository,
    private val inventoryItemRepository: InventoryItemRepository,
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository
) {

    fun getAllMovements(): List<InventoryMovementDTO> {
        return inventoryMovementRepository.findAll().map { it.toDTO() }
    }

    fun getMovementById(id: UUID): InventoryMovementDTO? {
        return inventoryMovementRepository.findByIdOrNull(id)?.toDTO()
    }

    fun getMovementsByType(type: String): List<InventoryMovementDTO> {
        return inventoryMovementRepository.findByType(type).map { it.toDTO() }
    }

    fun getMovementsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<InventoryMovementDTO> {
        return inventoryMovementRepository.findByTimestampBetween(startDate, endDate).map { it.toDTO() }
    }

    fun getMovementsByReferenceNumber(referenceNumber: String): List<InventoryMovementDTO> {
        return inventoryMovementRepository.findByReferenceNumber(referenceNumber).map { it.toDTO() }
    }

    @Transactional
    fun recordMovement(movementDTO: InventoryMovementDTO): InventoryMovementDTO? {
        val inventoryItem = inventoryItemRepository.findByIdOrNull(movementDTO.inventoryItemId) ?: return null
        
        // Validate locations when applicable
        val sourceLocation = movementDTO.sourceLocationId?.let { 
            locationRepository.findByIdOrNull(it) 
        }
        val destinationLocation = movementDTO.destinationLocationId?.let { 
            locationRepository.findByIdOrNull(it) 
        }
        
        // Optional user who created the movement
        val createdBy = movementDTO.createdById?.let { 
            userRepository.findByIdOrNull(it) 
        }
        
        val movement = InventoryMovement(
            type = movementDTO.type,
            quantity = movementDTO.quantity,
            reason = movementDTO.reason,
            referenceNumber = movementDTO.referenceNumber,
            timestamp = movementDTO.timestamp,
            notes = movementDTO.notes,
            inventoryItem = inventoryItem,
            sourceLocation = sourceLocation,
            destinationLocation = destinationLocation,
            createdBy = createdBy
        )
        
        // Update inventory levels based on movement type
        when (movement.type) {
            "Inbound" -> {
                inventoryItem.quantity += movement.quantity
                if (destinationLocation != null) {
                    inventoryItem.location = destinationLocation
                }
            }
            "Outbound" -> {
                inventoryItem.quantity -= movement.quantity
                if (inventoryItem.quantity < 0) {
                    throw IllegalStateException("Cannot remove more items than available")
                }
            }
            "Transfer" -> {
                if (sourceLocation == null || destinationLocation == null) {
                    throw IllegalArgumentException("Source and destination locations are required for transfers")
                }
                if (inventoryItem.location?.id != sourceLocation.id) {
                    throw IllegalStateException("Item is not currently at the source location")
                }
                inventoryItem.location = destinationLocation
            }
            "Adjustment" -> {
                // For inventory adjustments, the quantity is the new total (not a delta)
                inventoryItem.quantity = movement.quantity
            }
        }
        
        // Save inventory item changes
        inventoryItemRepository.save(inventoryItem)
        
        // Save and return the movement
        return inventoryMovementRepository.save(movement).toDTO()
    }

    private fun InventoryMovement.toDTO(): InventoryMovementDTO {
        return InventoryMovementDTO(
            id = id,
            type = type,
            inventoryItemId = inventoryItem?.id ?: throw IllegalStateException("Movement must have an inventory item"),
            productName = inventoryItem?.product?.name,
            quantity = quantity,
            sourceLocationId = sourceLocation?.id,
            sourceLocationCode = sourceLocation?.code,
            destinationLocationId = destinationLocation?.id,
            destinationLocationCode = destinationLocation?.code,
            reason = reason,
            referenceNumber = referenceNumber,
            timestamp = timestamp,
            notes = notes,
            createdById = createdBy?.id,
            createdByUsername = createdBy?.username
        )
    }
}