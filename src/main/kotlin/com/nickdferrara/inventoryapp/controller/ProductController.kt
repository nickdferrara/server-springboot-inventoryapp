package com.nickdferrara.inventoryapp.controller

import com.nickdferrara.inventoryapp.dto.ProductDTO
import com.nickdferrara.inventoryapp.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductDTO>> {
        return ResponseEntity.ok(productService.getAllProducts())
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<ProductDTO> {
        val product = productService.getProductById(id)
        return if (product != null) {
            ResponseEntity.ok(product)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search")
    fun searchProducts(@RequestParam keyword: String): ResponseEntity<List<ProductDTO>> {
        return ResponseEntity.ok(productService.searchProducts(keyword))
    }

    @GetMapping("/low-stock")
    fun getLowStockProducts(): ResponseEntity<List<ProductDTO>> {
        return ResponseEntity.ok(productService.getLowStockProducts())
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    fun createProduct(@RequestBody productDTO: ProductDTO): ResponseEntity<ProductDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    fun updateProduct(
        @PathVariable id: UUID,
        @RequestBody productDTO: ProductDTO
    ): ResponseEntity<ProductDTO> {
        val updatedProduct = productService.updateProduct(id, productDTO)
        return if (updatedProduct != null) {
            ResponseEntity.ok(updatedProduct)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (productService.deleteProduct(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}