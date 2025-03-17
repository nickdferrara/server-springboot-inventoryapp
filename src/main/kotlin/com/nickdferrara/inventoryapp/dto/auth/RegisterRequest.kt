package com.nickdferrara.inventoryapp.dto.auth

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)