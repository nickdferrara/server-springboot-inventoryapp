package com.nickdferrara.inventoryapp.service

import com.nickdferrara.inventoryapp.dto.LocationDTO
import com.nickdferrara.inventoryapp.models.Location
import com.nickdferrara.inventoryapp.repository.LocationRepository
import com.nickdferrara.inventoryapp.repository.WarehouseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class LocationService(
    private val locationRepository: LocationRepository,
    private val warehouseRepository: WarehouseRepository
) {

    fun getAllLocations(): List<LocationDTO> {
        return locationRepository.findAll().map { it.toDTO() }
    }

    fun getActiveLocations(): List<LocationDTO> {
        return locationRepository.findByIsActiveTrue().map { it.toDTO() }
    }

    fun getLocationById(id: UUID): LocationDTO? {
        return locationRepository.findByIdOrNull(id)?.toDTO()
    }

    fun getLocationsByWarehouse(warehouseId: UUID): List<LocationDTO> {
        val warehouse = warehouseRepository.findByIdOrNull(warehouseId) ?: return emptyList()
        return locationRepository.findByWarehouse(warehouse).map { it.toDTO() }
    }

    fun getLocationByCode(code: String): LocationDTO? {
        return locationRepository.findByCodeIgnoreCase(code).orElse(null)?.toDTO()
    }

    @Transactional
    fun createLocation(locationDTO: LocationDTO): LocationDTO? {
        val warehouse = warehouseRepository.findByIdOrNull(locationDTO.warehouseId) ?: return null

        val location = Location(
            name = locationDTO.name,
            code = locationDTO.code,
            type = locationDTO.type,
            area = locationDTO.area,
            aisle = locationDTO.aisle,
            rack = locationDTO.rack,
            shelf = locationDTO.shelf,
            bin = locationDTO.bin,
            isActive = locationDTO.isActive,
            warehouse = warehouse
        )

        return locationRepository.save(location).toDTO()
    }

    @Transactional
    fun updateLocation(id: UUID, locationDTO: LocationDTO): LocationDTO? {
        val existingLocation = locationRepository.findByIdOrNull(id) ?: return null
        val warehouse = warehouseRepository.findByIdOrNull(locationDTO.warehouseId) ?: return null

        with(existingLocation) {
            name = locationDTO.name
            code = locationDTO.code
            type = locationDTO.type
            area = locationDTO.area
            aisle = locationDTO.aisle
            rack = locationDTO.rack
            shelf = locationDTO.shelf
            bin = locationDTO.bin
            isActive = locationDTO.isActive
            this.warehouse = warehouse
        }

        return locationRepository.save(existingLocation).toDTO()
    }

    @Transactional
    fun deleteLocation(id: UUID): Boolean {
        return if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    private fun Location.toDTO(): LocationDTO {
        return LocationDTO(
            id = id,
            name = name,
            code = code,
            type = type,
            area = area,
            aisle = aisle,
            rack = rack,
            shelf = shelf,
            bin = bin,
            isActive = isActive,
            warehouseId = warehouse?.id ?: throw IllegalStateException("Location must have a warehouse"),
            warehouseName = warehouse?.name
        )
    }
}