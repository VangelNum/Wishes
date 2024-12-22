package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest
import com.vangelnum.app.wisher.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
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
    fun registerUser(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<User> {
        val createdUser = userService.registerUser(registrationRequest)
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @Operation(summary = "Информация о текущем пользователе")
    @GetMapping("/me")
    fun getCurrentUserInfo(): ResponseEntity<User> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        val user = userService.getUserByEmail(email)
        return if (user != null) {
            ResponseEntity(user, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Получение списка пользователей")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity(userService.getAllUsers(), HttpStatus.OK)
    }

    @Operation(summary = "Получение пользователя по ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User?> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity(user, HttpStatus.OK) else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(summary = "Обновление данных пользователя")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updateProfileRequest: UpdateProfileRequest): ResponseEntity<User?> {
        val updatedUser = userService.updateUser(id, updateProfileRequest)
        return if (updatedUser != null) ResponseEntity(
            updatedUser,
            HttpStatus.OK
        ) else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(summary = "Удаление пользователя по ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: Long) = userService.deleteUser(id)
}
