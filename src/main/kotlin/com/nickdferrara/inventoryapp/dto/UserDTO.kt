package com.nickdferrara.inventoryapp.dto

import java.time.LocalDateTime
import java.util.*

data class UserDTO(
    val id: UUID? = null,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean = true,
    val lastLogin: LocalDateTime? = null,
    val roles: Set<String> = emptySet()
)