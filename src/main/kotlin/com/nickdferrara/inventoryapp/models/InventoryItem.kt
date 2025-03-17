package com.nickdferrara.inventoryapp.models

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "inventory_items")
class InventoryItem (
    var quantity: Int = 0,
    var status: String = "Available", // Available, Reserved, Damaged, etc.
    var lotNumber: String? = null,
    var serialNumber: String? = null,
    var expiryDate: LocalDateTime? = null,
    var lastCheckedDate: LocalDateTime = LocalDateTime.now(),
    var notes: String? = null,
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null,
    
    @ManyToOne
    @JoinColumn(name = "location_id")
    var location: Location? = null,
    
    @OneToMany(mappedBy = "inventoryItem", cascade = [CascadeType.ALL])
    var movements: MutableList<InventoryMovement> = mutableListOf(),
    
    @Id @UuidGenerator
    var id: UUID? = null,
)