package com.nickdferrara.inventoryapp.repository

import com.nickdferrara.inventoryapp.models.InventoryItem
import com.nickdferrara.inventoryapp.models.Location
import com.nickdferrara.inventoryapp.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface InventoryItemRepository : JpaRepository<InventoryItem, UUID> {
    fun findByProduct(product: Product): List<InventoryItem>
    fun findByLocation(location: Location): List<InventoryItem>
    fun findByProductAndLocation(product: Product, location: Location): List<InventoryItem>
    fun findByStatus(status: String): List<InventoryItem>
    fun findByExpiryDateBefore(date: LocalDateTime): List<InventoryItem>
    
    @Query("SELECT SUM(i.quantity) FROM InventoryItem i WHERE i.product = ?1")
    fun getTotalQuantityByProduct(product: Product): Int?
    
    @Query("SELECT SUM(i.quantity) FROM InventoryItem i WHERE i.product = ?1 AND i.location = ?2")
    fun getQuantityByProductAndLocation(product: Product, location: Location): Int?
}