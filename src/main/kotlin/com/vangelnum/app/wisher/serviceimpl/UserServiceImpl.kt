package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.core.enums.Role
import com.vangelnum.app.wisher.core.validator.UserValidator
import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest
import com.vangelnum.app.wisher.repository.UserRepository
import com.vangelnum.app.wisher.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userValidator: UserValidator
) : UserService {

    override fun registerUser(registrationRequest: RegistrationRequest): User {
        val checkStatus = userValidator.checkUser(registrationRequest)
        if (!checkStatus.isSuccess) {
            throw IllegalArgumentException(checkStatus.message)
        }
        val newUser = User(
            name = registrationRequest.name,
            password = passwordEncoder.encode(registrationRequest.password),
            email = registrationRequest.email,
            role = Role.USER,
            avatarUrl = registrationRequest.avatarUrl,
            coins = 200
        )
        return userRepository.save(newUser)
    }

    override fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElse(null)
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    override fun updateUser(id: Long, updateProfileRequest: UpdateProfileRequest): User? {
        val checkStatus = userValidator.checkUser(updateProfileRequest, id)
        if (!checkStatus.isSuccess) {
            throw IllegalArgumentException(checkStatus.message)
        }
        return userRepository.findById(id).map { existingUser ->
            val updatedUser = existingUser.copy(
                name = updateProfileRequest.name ?: existingUser.name,
                avatarUrl = updateProfileRequest.avatarUrl ?: existingUser.avatarUrl,
                email = updateProfileRequest.email ?: existingUser.email
            )
            userRepository.save(updatedUser)
        }.orElse(null)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}