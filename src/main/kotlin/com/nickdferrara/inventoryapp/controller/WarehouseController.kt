package com.nickdferrara.inventoryapp.controller

import com.nickdferrara.inventoryapp.dto.WarehouseDTO
import com.nickdferrara.inventoryapp.service.WarehouseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/warehouses")
class WarehouseController(private val warehouseService: WarehouseService) {

    @GetMapping
    fun getAllWarehouses(): ResponseEntity<List<WarehouseDTO>> {
        return ResponseEntity.ok(warehouseService.getAllWarehouses())
    }

    @GetMapping("/active")
    fun getActiveWarehouses(): ResponseEntity<List<WarehouseDTO>> {
        return ResponseEntity.ok(warehouseService.getActiveWarehouses())
    }

    @GetMapping("/{id}")
    fun getWarehouseById(@PathVariable id: UUID): ResponseEntity<WarehouseDTO> {
        val warehouse = warehouseService.getWarehouseById(id)
        return if (warehouse != null) {
            ResponseEntity.ok(warehouse)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/code/{code}")
    fun getWarehouseByCode(@PathVariable code: String): ResponseEntity<WarehouseDTO> {
        val warehouse = warehouseService.getWarehouseByCode(code)
        return if (warehouse != null) {
            ResponseEntity.ok(warehouse)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createWarehouse(@RequestBody warehouseDTO: WarehouseDTO): ResponseEntity<WarehouseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.createWarehouse(warehouseDTO))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateWarehouse(
        @PathVariable id: UUID,
        @RequestBody warehouseDTO: WarehouseDTO
    ): ResponseEntity<WarehouseDTO> {
        val updatedWarehouse = warehouseService.updateWarehouse(id, warehouseDTO)
        return if (updatedWarehouse != null) {
            ResponseEntity.ok(updatedWarehouse)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteWarehouse(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (warehouseService.deleteWarehouse(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}