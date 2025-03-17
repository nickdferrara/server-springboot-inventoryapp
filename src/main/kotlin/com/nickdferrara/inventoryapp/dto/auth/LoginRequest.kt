package com.nickdferrara.inventoryapp.dto.auth

data class LoginRequest(
    val username: String,
    val password: String
)