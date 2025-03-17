package com.nickdferrara.inventoryapp.service

import com.nickdferrara.inventoryapp.dto.WarehouseDTO
import com.nickdferrara.inventoryapp.models.Warehouse
import com.nickdferrara.inventoryapp.repository.WarehouseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class WarehouseService(private val warehouseRepository: WarehouseRepository) {

    fun getAllWarehouses(): List<WarehouseDTO> {
        return warehouseRepository.findAll().map { it.toDTO() }
    }

    fun getActiveWarehouses(): List<WarehouseDTO> {
        return warehouseRepository.findByIsActiveTrue().map { it.toDTO() }
    }

    fun getWarehouseById(id: UUID): WarehouseDTO? {
        return warehouseRepository.findByIdOrNull(id)?.toDTO()
    }

    fun getWarehouseByCode(code: String): WarehouseDTO? {
        return warehouseRepository.findByCodeIgnoreCase(code).orElse(null)?.toDTO()
    }

    @Transactional
    fun createWarehouse(warehouseDTO: WarehouseDTO): WarehouseDTO {
        val warehouse = Warehouse(
            name = warehouseDTO.name,
            code = warehouseDTO.code,
            address = warehouseDTO.address,
            city = warehouseDTO.city,
            state = warehouseDTO.state,
            country = warehouseDTO.country,
            zipCode = warehouseDTO.zipCode,
            phoneNumber = warehouseDTO.phoneNumber,
            email = warehouseDTO.email,
            isActive = warehouseDTO.isActive
        )

        return warehouseRepository.save(warehouse).toDTO()
    }

    @Transactional
    fun updateWarehouse(id: UUID, warehouseDTO: WarehouseDTO): WarehouseDTO? {
        val existingWarehouse = warehouseRepository.findByIdOrNull(id) ?: return null

        with(existingWarehouse) {
            name = warehouseDTO.name
            code = warehouseDTO.code
            address = warehouseDTO.address
            city = warehouseDTO.city
            state = warehouseDTO.state
            country = warehouseDTO.country
            zipCode = warehouseDTO.zipCode
            phoneNumber = warehouseDTO.phoneNumber
            email = warehouseDTO.email
            isActive = warehouseDTO.isActive
        }

        return warehouseRepository.save(existingWarehouse).toDTO()
    }

    @Transactional
    fun deleteWarehouse(id: UUID): Boolean {
        return if (warehouseRepository.existsById(id)) {
            warehouseRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    private fun Warehouse.toDTO(): WarehouseDTO {
        return WarehouseDTO(
            id = id,
            name = name,
            code = code,
            address = address,
            city = city,
            state = state,
            country = country,
            zipCode = zipCode,
            phoneNumber = phoneNumber,
            email = email,
            isActive = isActive,
            locationCount = locations.size
        )
    }
}