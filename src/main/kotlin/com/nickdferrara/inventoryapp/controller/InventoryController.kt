package com.nickdferrara.inventoryapp.controller

import com.nickdferrara.inventoryapp.dto.InventoryItemDTO
import com.nickdferrara.inventoryapp.dto.InventoryMovementDTO
import com.nickdferrara.inventoryapp.service.InventoryItemService
import com.nickdferrara.inventoryapp.service.InventoryMovementService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
    private val inventoryItemService: InventoryItemService,
    private val inventoryMovementService: InventoryMovementService
) {

    // ===== Inventory Items Endpoints =====
    
    @GetMapping("/items")
    fun getAllInventoryItems(): ResponseEntity<List<InventoryItemDTO>> {
        return ResponseEntity.ok(inventoryItemService.getAllInventoryItems())
    }

    @GetMapping("/items/{id}")
    fun getInventoryItemById(@PathVariable id: UUID): ResponseEntity<InventoryItemDTO> {
        val item = inventoryItemService.getInventoryItemById(id)
        return if (item != null) {
            ResponseEntity.ok(item)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/items/product/{productId}")
    fun getInventoryItemsByProduct(@PathVariable productId: UUID): ResponseEntity<List<InventoryItemDTO>> {
        return ResponseEntity.ok(inventoryItemService.getInventoryItemsByProductId(productId))
    }

    @GetMapping("/items/location/{locationId}")
    fun getInventoryItemsByLocation(@PathVariable locationId: UUID): ResponseEntity<List<InventoryItemDTO>> {
        return ResponseEntity.ok(inventoryItemService.getInventoryItemsByLocationId(locationId))
    }

    @GetMapping("/items/status/{status}")
    fun getInventoryItemsByStatus(@PathVariable status: String): ResponseEntity<List<InventoryItemDTO>> {
        return ResponseEntity.ok(inventoryItemService.getInventoryItemsByStatus(status))
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('WAREHOUSE_STAFF')")
    fun createInventoryItem(@RequestBody itemDTO: InventoryItemDTO): ResponseEntity<InventoryItemDTO> {
        val createdItem = inventoryItemService.createInventoryItem(itemDTO)
        return if (createdItem != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(createdItem)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('WAREHOUSE_STAFF')")
    fun updateInventoryItem(
        @PathVariable id: UUID,
        @RequestBody itemDTO: InventoryItemDTO
    ): ResponseEntity<InventoryItemDTO> {
        val updatedItem = inventoryItemService.updateInventoryItem(id, itemDTO)
        return if (updatedItem != null) {
            ResponseEntity.ok(updatedItem)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/items/{id}/quantity/{quantity}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('WAREHOUSE_STAFF')")
    fun adjustInventoryQuantity(
        @PathVariable id: UUID,
        @PathVariable quantity: Int
    ): ResponseEntity<InventoryItemDTO> {
        val updatedItem = inventoryItemService.adjustInventoryQuantity(id, quantity)
        return if (updatedItem != null) {
            ResponseEntity.ok(updatedItem)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    fun deleteInventoryItem(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (inventoryItemService.deleteInventoryItem(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // ===== Inventory Movements Endpoints =====
    
    @GetMapping("/movements")
    fun getAllMovements(): ResponseEntity<List<InventoryMovementDTO>> {
        return ResponseEntity.ok(inventoryMovementService.getAllMovements())
    }

    @GetMapping("/movements/{id}")
    fun getMovementById(@PathVariable id: UUID): ResponseEntity<InventoryMovementDTO> {
        val movement = inventoryMovementService.getMovementById(id)
        return if (movement != null) {
            ResponseEntity.ok(movement)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/movements/type/{type}")
    fun getMovementsByType(@PathVariable type: String): ResponseEntity<List<InventoryMovementDTO>> {
        return ResponseEntity.ok(inventoryMovementService.getMovementsByType(type))
    }

    @GetMapping("/movements/reference/{referenceNumber}")
    fun getMovementsByReferenceNumber(@PathVariable referenceNumber: String): ResponseEntity<List<InventoryMovementDTO>> {
        return ResponseEntity.ok(inventoryMovementService.getMovementsByReferenceNumber(referenceNumber))
    }

    @GetMapping("/movements/date-range")
    fun getMovementsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<InventoryMovementDTO>> {
        return ResponseEntity.ok(inventoryMovementService.getMovementsByDateRange(startDate, endDate))
    }

    @PostMapping("/movements")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('WAREHOUSE_STAFF')")
    fun recordMovement(@RequestBody movementDTO: InventoryMovementDTO): ResponseEntity<InventoryMovementDTO> {
        val createdMovement = inventoryMovementService.recordMovement(movementDTO)
        return if (createdMovement != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(createdMovement)
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}