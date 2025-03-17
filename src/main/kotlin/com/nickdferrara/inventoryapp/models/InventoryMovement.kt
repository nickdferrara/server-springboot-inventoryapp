package com.nickdferrara.inventoryapp.models

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "inventory_movements")
class InventoryMovement (
    var type: String = "", // "Inbound", "Outbound", "Transfer", "Adjustment"
    var quantity: Int = 0,
    var reason: String? = null,
    var referenceNumber: String? = null, // Order number, transfer ID, etc.
    var timestamp: LocalDateTime = LocalDateTime.now(),
    var notes: String? = null,
    
    @ManyToOne
    @JoinColumn(name = "inventory_item_id")
    var inventoryItem: InventoryItem? = null,
    
    @ManyToOne
    @JoinColumn(name = "source_location_id")
    var sourceLocation: Location? = null,
    
    @ManyToOne
    @JoinColumn(name = "destination_location_id")
    var destinationLocation: Location? = null,
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    var createdBy: User? = null,
    
    @Id @UuidGenerator
    var id: UUID? = null,
)