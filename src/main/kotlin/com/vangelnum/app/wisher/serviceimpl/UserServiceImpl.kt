package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.core.enums.Role
import com.vangelnum.app.wisher.core.validator.UserValidator
import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest
import com.vangelnum.app.wisher.repository.UserRepository
import com.vangelnum.app.wisher.service.UserService
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

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
            avatarUrl = null,
            coins = 500
        )
        return userRepository.save(newUser)
    }

    override fun updateUserAvatar(avatar: String): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        val checkStatus = userValidator.checkAvatarUrl(avatar)
        if (!checkStatus.isSuccess) {
            throw IllegalArgumentException(checkStatus.message)
        }
        val existingUser = userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("Пользователь не найден") }
        val updatedUser = existingUser.copy(avatarUrl = avatar)
        return userRepository.save(updatedUser)
    }

    override fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("Пользователь с email $email не найден") }
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun getUserById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Пользователь с id $id не найден") }
    }

    override fun updateUser(updateProfileRequest: UpdateProfileRequest): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name

        updateProfileRequest.name?.let {
            if (!userValidator.checkName(it).isSuccess) {
                throw IllegalArgumentException(userValidator.checkName(it).message)
            }
        }
        updateProfileRequest.email?.let {
            if (!userValidator.checkEmailForUpdate(it, email).isSuccess) {
                throw IllegalArgumentException(userValidator.checkEmailForUpdate(it, email).message)
            }
        }
        updateProfileRequest.password?.let {
            if (!userValidator.checkPassword(it).isSuccess) {
                throw IllegalArgumentException(userValidator.checkPassword(it).message)
            }
        }

        val existingUser = userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("Пользователь не найден") }

        val updatedUser = existingUser.copy(
            name = updateProfileRequest.name ?: existingUser.name,
            avatarUrl = updateProfileRequest.avatarUrl ?: existingUser.avatarUrl,
            email = updateProfileRequest.email ?: existingUser.email,
            password = updateProfileRequest.password?.let { passwordEncoder.encode(it) } ?: existingUser.password
        )
        return userRepository.save(updatedUser)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}