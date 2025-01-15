package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.core.utils.getCurrentUserEmail
import com.vangelnum.app.wisher.entity.WishKey
import com.vangelnum.app.wisher.service.WishKeyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Ключи для пожеланий")
@RestController
@RequestMapping("/api/v1/wish-key")
class WishKeyController(
    private val wishKeyService: WishKeyService
) {
    @Operation(summary = "Генерация ключа")
    @PostMapping("/generate")
    fun generateWishKey(): ResponseEntity<WishKey> {
        val email = getCurrentUserEmail()
        val wishKey = wishKeyService.generateWishKey(email)
        return ResponseEntity(wishKey, HttpStatus.OK)
    }

    @Operation(summary = "Получение ключа текущего пользователя")
    @GetMapping("/my")
    fun getCurrentUserWishKey(): ResponseEntity<WishKey> {
        val email = getCurrentUserEmail()
        val wishKey = wishKeyService.getWishKeyForCurrentUser(email)
        return ResponseEntity.ok(wishKey)
    }

    @Operation(summary = "Перегенерировать ключ")
    @PostMapping("/regenerate")
    fun regenerateWishKey(): ResponseEntity<WishKey> {
        val email = getCurrentUserEmail()
        val newWishKey = wishKeyService.regenerateWishKey(email)
        return ResponseEntity.ok(newWishKey)
    }
}