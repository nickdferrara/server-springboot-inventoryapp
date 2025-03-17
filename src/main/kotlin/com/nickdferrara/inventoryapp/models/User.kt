package com.nickdferrara.inventoryapp.models

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
class User (
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var isActive: Boolean = true,
    var lastLogin: LocalDateTime? = null,
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf(),
    
    @OneToMany(mappedBy = "createdBy")
    var inventoryMovements: MutableList<InventoryMovement> = mutableListOf(),
    
    @Id @UuidGenerator
    var id: UUID? = null,
)