package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.core.enums.Role
import com.vangelnum.app.wisher.core.validator.GlobalExceptionHandler
import com.vangelnum.app.wisher.core.validator.UserValidator
import com.vangelnum.app.wisher.entity.PendingUser
import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest
import com.vangelnum.app.wisher.repository.PendingUserRepository
import com.vangelnum.app.wisher.repository.UserRepository
import com.vangelnum.app.wisher.service.EmailService
import com.vangelnum.app.wisher.service.UserService
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.NoSuchElementException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val pendingUserRepository: PendingUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userValidator: UserValidator,
    private val emailService: EmailService
) : UserService {

    @Transactional
    override fun registerUser(registrationRequest: RegistrationRequest): String {
        val checkStatus = userValidator.checkUser(registrationRequest)
        if (!checkStatus.isSuccess) {
            throw IllegalArgumentException(checkStatus.message)
        }

        if (userRepository.findByEmail(registrationRequest.email).isPresent) {
            throw IllegalArgumentException(GlobalExceptionHandler.USER_ALREADY_EXISTS_MESSAGE)
        }

        val verificationCode = generateVerificationCode()

        val pendingUser = PendingUser(
            name = registrationRequest.name,
            password = passwordEncoder.encode(registrationRequest.password),
            email = registrationRequest.email,
            role = Role.USER,
            verificationCode = verificationCode
        )
        pendingUserRepository.save(pendingUser)

        try {
            emailService.sendVerificationEmail(registrationRequest.email, verificationCode)
        } catch (e: Exception) {
            pendingUserRepository.delete(pendingUser)
            throw IllegalStateException(GlobalExceptionHandler.EMAIL_SENDING_FAILED_MESSAGE)
        }

        return "Код отправлен. Проверьте вашу почту."
    }

    @Transactional
    override fun verifyEmail(email: String, verificationCode: String): User {
        val pendingUserOptional = pendingUserRepository.findByEmail(email)
        val pendingUser = pendingUserOptional.orElseThrow { NoSuchElementException("Не найден запрос на регистрацию или пользователь уже существует $email") }

        if (pendingUser.verificationCode == verificationCode) {
            val newUser = User(
                name = pendingUser.name,
                password = pendingUser.password,
                email = pendingUser.email,
                role = pendingUser.role,
                avatarUrl = null,
                coins = 500,
                isEmailVerified = true,
                verificationCode = null
            )
            val savedUser = userRepository.save(newUser)
            pendingUserRepository.delete(pendingUser)
            return savedUser
        } else {
            throw IllegalArgumentException(GlobalExceptionHandler.INVALID_VERIFICATION_CODE_MESSAGE)
        }
    }

    override fun resendVerificationCode(email: String) {
        val pendingUserOptional = pendingUserRepository.findByEmail(email) // Check in PendingUser first
        if (pendingUserOptional.isPresent) {
            val pendingUser = pendingUserOptional.get()
            val newVerificationCode = generateVerificationCode()
            pendingUser.verificationCode = newVerificationCode
            pendingUserRepository.save(pendingUser)
            emailService.sendVerificationEmail(email, newVerificationCode)
            return
        }

        val userOptional = userRepository.findByEmail(email)
        val user = userOptional.orElseThrow { NoSuchElementException("Запрос на регистрацию или пользователь с email $email не найден") }

        if (user.isEmailVerified) {
            throw IllegalArgumentException(GlobalExceptionHandler.EMAIL_ALREADY_VERIFIED_MESSAGE)
        }

        val newVerificationCode = generateVerificationCode()
        user.verificationCode = newVerificationCode
        userRepository.save(user)

        emailService.sendVerificationEmail(email, newVerificationCode)
        return
    }

    private fun generateVerificationCode(): String {
        val random = Random()
        return String.format("%06d", random.nextInt(1000000))
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