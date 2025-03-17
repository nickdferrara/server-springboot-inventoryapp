package com.nickdferrara.inventoryapp.controller

import com.nickdferrara.inventoryapp.dto.LocationDTO
import com.nickdferrara.inventoryapp.service.LocationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/locations")
class LocationController(private val locationService: LocationService) {

    @GetMapping
    fun getAllLocations(): ResponseEntity<List<LocationDTO>> {
        return ResponseEntity.ok(locationService.getAllLocations())
    }

    @GetMapping("/active")
    fun getActiveLocations(): ResponseEntity<List<LocationDTO>> {
        return ResponseEntity.ok(locationService.getActiveLocations())
    }

    @GetMapping("/{id}")
    fun getLocationById(@PathVariable id: UUID): ResponseEntity<LocationDTO> {
        val location = locationService.getLocationById(id)
        return if (location != null) {
            ResponseEntity.ok(location)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/warehouse/{warehouseId}")
    fun getLocationsByWarehouse(@PathVariable warehouseId: UUID): ResponseEntity<List<LocationDTO>> {
        return ResponseEntity.ok(locationService.getLocationsByWarehouse(warehouseId))
    }

    @GetMapping("/code/{code}")
    fun getLocationByCode(@PathVariable code: String): ResponseEntity<LocationDTO> {
        val location = locationService.getLocationByCode(code)
        return if (location != null) {
            ResponseEntity.ok(location)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    fun createLocation(@RequestBody locationDTO: LocationDTO): ResponseEntity<LocationDTO> {
        val createdLocation = locationService.createLocation(locationDTO)
        return if (createdLocation != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(createdLocation)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    fun updateLocation(
        @PathVariable id: UUID,
        @RequestBody locationDTO: LocationDTO
    ): ResponseEntity<LocationDTO> {
        val updatedLocation = locationService.updateLocation(id, locationDTO)
        return if (updatedLocation != null) {
            ResponseEntity.ok(updatedLocation)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteLocation(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (locationService.deleteLocation(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}