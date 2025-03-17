package com.nickdferrara.inventoryapp.dto.auth

data class AuthResponse(
    val token: String,
    val username: String,
    val roles: Set<String>
)