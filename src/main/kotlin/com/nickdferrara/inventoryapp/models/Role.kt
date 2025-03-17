package com.nickdferrara.inventoryapp.models

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "roles")
class Role (
    var name: String = "",
    var description: String? = null,
    
    @ManyToMany(mappedBy = "roles")
    var users: MutableSet<User> = mutableSetOf(),
    
    @Id @UuidGenerator
    var id: UUID? = null,
)