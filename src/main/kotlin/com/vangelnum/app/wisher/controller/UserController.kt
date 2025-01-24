package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.core.utils.getCurrentUserEmail
import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateAvatarRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest
import com.vangelnum.app.wisher.model.VerificationRequest
import com.vangelnum.app.wisher.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "Пользователь")
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    fun registerUser(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<User> {
        val createdUser = userService.registerUser(registrationRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @Operation(summary = "Верификация email пользователя")
    @PostMapping("/verify-email")
    fun verifyEmail(@RequestBody verificationRequest: VerificationRequest): ResponseEntity<String> {
        val isVerified = userService.verifyEmail(verificationRequest.email, verificationRequest.verificationCode)
        return if (isVerified) {
            ResponseEntity.ok("Email успешно подтвержден")
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный код подтверждения или email")
        }
    }


    @Operation(summary = "Переотправка кода верификации email")
    @PostMapping("/resend-verification-code")
    fun resendVerificationCode(@RequestBody verificationRequest: VerificationRequest): ResponseEntity<String> {
        val isResent = userService.resendVerificationCode(verificationRequest.email)
        return if (isResent) {
            ResponseEntity.ok("Новый код верификации отправлен на ваш email")
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось отправить новый код верификации. Проверьте email или попробуйте позже.")
        }
    }

    @Operation(summary = "Информация о текущем пользователе")
    @GetMapping("/me")
    fun getCurrentUserInfo(): ResponseEntity<User> {
        val email = getCurrentUserEmail()
        return userService.getUserByEmail(email).let { ResponseEntity.ok(it) }
    }

    @Operation(summary = "Получение списка пользователей")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @Operation(summary = "Получение пользователя по ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        return userService.getUserById(id).let { ResponseEntity.ok(it) }
    }

    @Operation(summary = "Обновление данных пользователя")
    @PutMapping
    fun updateUser(
        @RequestBody updateProfileRequest: UpdateProfileRequest
    ): ResponseEntity<User> {
        val updatedUser = userService.updateUser(updateProfileRequest)
        return ResponseEntity.ok(updatedUser)
    }

    @Operation(summary = "Обновление аватара пользователя")
    @PutMapping("/avatar")
    fun updateAvatar(@RequestBody updateAvatarRequest: UpdateAvatarRequest): ResponseEntity<User> {
        val updatedUser = userService.updateUserAvatar(updateAvatarRequest.avatarUrl)
        return ResponseEntity.ok(updatedUser)
    }

    @Operation(summary = "Удаление пользователя по ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}