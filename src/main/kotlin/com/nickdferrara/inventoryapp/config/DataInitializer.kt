package com.nickdferrara.inventoryapp.config

import com.nickdferrara.inventoryapp.models.Role
import com.nickdferrara.inventoryapp.models.User
import com.nickdferrara.inventoryapp.repository.RoleRepository
import com.nickdferrara.inventoryapp.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer {

    @Bean
    fun initData(roleRepository: RoleRepository, userRepository: UserRepository, passwordEncoder: PasswordEncoder): CommandLineRunner {
        return CommandLineRunner { args ->
            // Create roles if they don't exist
            val adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet {
                val role = Role(name = "ROLE_ADMIN", description = "Administrator role with full access")
                roleRepository.save(role)
            }

            val managerRole = roleRepository.findByName("ROLE_INVENTORY_MANAGER").orElseGet {
                val role = Role(name = "ROLE_INVENTORY_MANAGER", description = "Manages inventory and products")
                roleRepository.save(role)
            }

            val warehouseRole = roleRepository.findByName("ROLE_WAREHOUSE_MANAGER").orElseGet {
                val role = Role(name = "ROLE_WAREHOUSE_MANAGER", description = "Manages warehouses and locations")
                roleRepository.save(role)
            }

            val staffRole = roleRepository.findByName("ROLE_WAREHOUSE_STAFF").orElseGet {
                val role = Role(name = "ROLE_WAREHOUSE_STAFF", description = "Regular warehouse staff")
                roleRepository.save(role)
            }

            val userRole = roleRepository.findByName("ROLE_USER").orElseGet {
                val role = Role(name = "ROLE_USER", description = "Regular user role")
                roleRepository.save(role)
            }

            // Create default admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                val adminUser = User(
                    username = "admin",
                    password = passwordEncoder.encode("admin123"),
                    email = "admin@inventory.com",
                    firstName = "Admin",
                    lastName = "User",
                    roles = mutableSetOf(adminRole, managerRole, warehouseRole, staffRole, userRole)
                )
                userRepository.save(adminUser)
            }

            // Create a manager user if it doesn't exist
            if (!userRepository.existsByUsername("manager")) {
                val managerUser = User(
                    username = "manager",
                    password = passwordEncoder.encode("manager123"),
                    email = "manager@inventory.com",
                    firstName = "Inventory",
                    lastName = "Manager",
                    roles = mutableSetOf(managerRole, staffRole, userRole)
                )
                userRepository.save(managerUser)
            }

            // Create a staff user if it doesn't exist
            if (!userRepository.existsByUsername("staff")) {
                val staffUser = User(
                    username = "staff",
                    password = passwordEncoder.encode("staff123"),
                    email = "staff@inventory.com",
                    firstName = "Staff",
                    lastName = "User",
                    roles = mutableSetOf(staffRole, userRole)
                )
                userRepository.save(staffUser)
            }
        }
    }
}