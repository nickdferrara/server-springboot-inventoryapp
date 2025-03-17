package com.nickdferrara.inventoryapp.models

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "warehouses")
class Warehouse (
    var name: String = "",
    var code: String = "",
    var address: String = "",
    var city: String = "",
    var state: String? = null,
    var country: String = "",
    var zipCode: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var isActive: Boolean = true,
    
    @OneToMany(mappedBy = "warehouse", cascade = [CascadeType.ALL], orphanRemoval = true)
    var locations: MutableList<Location> = mutableListOf(),
    
    @OneToMany(mappedBy = "warehouse")
    var products: MutableList<Product> = mutableListOf(),
    
    @Id @UuidGenerator
    var id: UUID? = null,
)