package com.vangelnum.app.wisher.user.conroller

import com.vangelnum.app.wisher.core.utils.getCurrentUserEmail
import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.user.model.DailyLoginBonusResponse
import com.vangelnum.app.wisher.user.model.RegistrationRequest
import com.vangelnum.app.wisher.user.model.RemainingBonusTime
import com.vangelnum.app.wisher.user.model.ResendVerificationCodeRequest
import com.vangelnum.app.wisher.user.model.UpdateAvatarRequest
import com.vangelnum.app.wisher.user.model.UpdateProfileRequest
import com.vangelnum.app.wisher.user.model.VerificationRequest
import com.vangelnum.app.wisher.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Пользователь")
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    fun registerUser(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<String> {
        val message = userService.registerUser(registrationRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(message)
    }

    @Operation(summary = "Верификация email пользователя")
    @PostMapping("/verify-email")
    fun verifyEmail(@RequestBody verificationRequest: VerificationRequest): ResponseEntity<User> {
        val user = userService.verifyEmail(verificationRequest.email, verificationRequest.verificationCode)
        return ResponseEntity.ok(user)
    }

    @Operation(summary = "Переотправка кода верификации email")
    @PostMapping("/resend-verification-code")
    fun resendVerificationCode(@RequestBody resendVerificationCodeRequest: ResendVerificationCodeRequest): ResponseEntity<String> {
        userService.resendVerificationCode(resendVerificationCodeRequest.email)
        return ResponseEntity.ok("Новый код верификации отправлен на ваш email")
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

    @Operation(summary = "Получение информации о ежедневном бонусе")
    @GetMapping("/daily-login-bonus-info")
    fun getDailyLoginBonusInfo(): ResponseEntity<DailyLoginBonusResponse> {
        val email = getCurrentUserEmail()
        val bonusInfo = userService.getDailyLoginBonusInfo(email)
        return ResponseEntity.ok(bonusInfo)
    }

    @Operation(summary = "Получение информации о оставшемся времени ожидания бонуса")
    @GetMapping("/daily-bonus/remaining-time")
    fun getRemainingBonusTime(): ResponseEntity<RemainingBonusTime> {
        val email = getCurrentUserEmail()
        val remainingTime = userService.getRemainingTimeToNextBonus(email)
        return ResponseEntity.ok(remainingTime)
    }

    @Operation(summary = "Запрос на получение ежедневного бонуса")
    @PostMapping("/daily-login-bonus")
    fun claimDailyLoginBonus(): ResponseEntity<DailyLoginBonusResponse> {
        val email = getCurrentUserEmail()
        val bonusResponse = userService.claimDailyLoginBonus(email)
        return ResponseEntity.ok(bonusResponse)
    }
}