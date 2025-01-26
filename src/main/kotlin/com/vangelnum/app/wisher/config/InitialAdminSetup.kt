package com.vangelnum.app.wisher.config

import com.vangelnum.app.wisher.core.enums.Role
import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.user.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Configuration
class InitialAdminSetup(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${ADMIN_USERNAME}") private val adminUsername: String,
    @Value("\${ADMIN_PASSWORD}") private val adminPassword: String,
    @Value("\${ADMIN_EMAIL}") private val adminEmail: String
) {
    @PostConstruct
    fun createInitialAdmin() {
        if (userRepository.count() == 0L) {
            val adminUser = User(
                name = adminUsername,
                password = passwordEncoder.encode(adminPassword),
                email = adminEmail,
                role = Role.ADMIN,
                coins = 1000,
                isEmailVerified = true,
                verificationCode = null,
                registrationTime = LocalDateTime.now(),
                lastLoginTime = null
            )
            userRepository.save(adminUser)
        }
    }
}