package com.nickdferrara.inventoryapp.models

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "locations")
class Location (
    var name: String = "",
    var code: String = "",
    var type: String = "", // e.g., "Shelf", "Bin", "Rack", etc.
    var area: String? = null,
    var aisle: String? = null,
    var rack: String? = null,
    var shelf: String? = null,
    var bin: String? = null,
    var isActive: Boolean = true,
    
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    var warehouse: Warehouse? = null,
    
    @OneToMany(mappedBy = "location", cascade = [CascadeType.ALL], orphanRemoval = true)
    var inventoryItems: MutableList<InventoryItem> = mutableListOf(),
    
    @Id @UuidGenerator
    var id: UUID? = null,
)