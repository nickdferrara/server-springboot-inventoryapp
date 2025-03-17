package com.nickdferrara.inventoryapp.controller

import com.nickdferrara.inventoryapp.dto.UserDTO
import com.nickdferrara.inventoryapp.dto.auth.AuthResponse
import com.nickdferrara.inventoryapp.dto.auth.LoginRequest
import com.nickdferrara.inventoryapp.dto.auth.RegisterRequest
import com.nickdferrara.inventoryapp.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserDTO> {
        return try {
            val userDTO = authService.register(request)
            ResponseEntity.ok(userDTO)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return try {
            val authResponse = authService.login(request)
            ResponseEntity.ok(authResponse)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}