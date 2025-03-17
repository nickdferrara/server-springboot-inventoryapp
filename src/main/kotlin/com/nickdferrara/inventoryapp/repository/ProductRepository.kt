package com.nickdferrara.inventoryapp.repository

import com.nickdferrara.inventoryapp.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {
    fun findBySkuContainingIgnoreCase(sku: String): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
    fun findByBrandContainingIgnoreCase(brand: String): List<Product>
    fun findByCategoryContainingIgnoreCase(category: String): List<Product>
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(p.sku) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(p.barcode) LIKE LOWER(concat('%', ?1,'%'))")
    fun search(keyword: String): List<Product>
    
    @Query("SELECT p FROM Product p JOIN p.inventoryItems i GROUP BY p HAVING SUM(i.quantity) <= p.minStockLevel")
    fun findLowStockProducts(): List<Product>
}