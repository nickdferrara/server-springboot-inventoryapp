package com.nickdferrara.inventoryapp.service

import com.nickdferrara.inventoryapp.dto.UserDTO
import com.nickdferrara.inventoryapp.dto.auth.AuthResponse
import com.nickdferrara.inventoryapp.dto.auth.LoginRequest
import com.nickdferrara.inventoryapp.dto.auth.RegisterRequest
import com.nickdferrara.inventoryapp.models.Role
import com.nickdferrara.inventoryapp.models.User
import com.nickdferrara.inventoryapp.repository.RoleRepository
import com.nickdferrara.inventoryapp.repository.UserRepository
import com.nickdferrara.inventoryapp.security.TokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService
) {

    @Transactional
    fun register(registerRequest: RegisterRequest): UserDTO {
        if (userRepository.existsByUsername(registerRequest.username)) {
            throw IllegalArgumentException("Username already exists")
        }

        if (userRepository.existsByEmail(registerRequest.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        // Get or create the USER role
        val userRole = roleRepository.findByName("ROLE_USER")
            .orElseGet {
                val newRole = Role(name = "ROLE_USER", description = "Regular user role")
                roleRepository.save(newRole)
            }

        val user = User(
            username = registerRequest.username,
            password = passwordEncoder.encode(registerRequest.password),
            email = registerRequest.email,
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            roles = mutableSetOf(userRole)
        )

        val savedUser = userRepository.save(user)
        return savedUser.toDTO()
    }

    @Transactional
    fun login(loginRequest: LoginRequest): AuthResponse {
        val user = userRepository.findByUsername(loginRequest.username)
            .orElseThrow { UsernameNotFoundException("User not found") }

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        // Update last login timestamp
        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)

        // Generate authorities from user roles
        val authorities = user.roles.map { role -> SimpleGrantedAuthority(role.name) }

        // Create authentication token
        val authentication = UsernamePasswordAuthenticationToken(
            user.username, null, authorities
        )

        // Generate JWT token
        val token = tokenService.generateToken(authentication)

        return AuthResponse(
            token = token,
            username = user.username,
            roles = user.roles.map { it.name }.toSet()
        )
    }

    private fun User.toDTO(): UserDTO {
        return UserDTO(
            id = id,
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            isActive = isActive,
            lastLogin = lastLogin,
            roles = roles.map { it.name }.toSet()
        )
    }
}