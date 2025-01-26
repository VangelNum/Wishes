package com.vangelnum.app.wisher.wishkey.conroller

import com.vangelnum.app.wisher.wishkey.dto.WishKeyDto
import com.vangelnum.app.wisher.core.utils.getCurrentUserEmail
import com.vangelnum.app.wisher.wishkey.service.WishKeyService
import com.vangelnum.app.wisher.wishkeylogs.entity.KeyViewLog
import com.vangelnum.app.wisher.wishkeylogs.service.KeyViewLogService
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
    private val wishKeyService: WishKeyService,
    private val keyViewLogService: KeyViewLogService
) {
    @Operation(summary = "Генерация ключа")
    @PostMapping("/generate")
    fun generateWishKey(): ResponseEntity<WishKeyDto> {
        val email = getCurrentUserEmail()
        val existingWishKey = wishKeyService.getWishKeyForCurrentUser(email)
        return if (existingWishKey != null) {
            ResponseEntity.ok(WishKeyDto(existingWishKey.key))
        } else {
            val wishKey = wishKeyService.generateWishKey(email)
            ResponseEntity(WishKeyDto(wishKey.key), HttpStatus.CREATED)
        }
    }

    @Operation(summary = "Получение ключа текущего пользователя")
    @GetMapping("/my")
    fun getCurrentUserWishKey(): ResponseEntity<WishKeyDto> {
        val email = getCurrentUserEmail()
        val wishKey = wishKeyService.getWishKeyForCurrentUser(email)
        return wishKey?.let { ResponseEntity.ok(WishKeyDto(it.key)) }
            ?: ResponseEntity.notFound().build()
    }

    @Operation(summary = "Перегенерировать ключ")
    @PostMapping("/regenerate")
    fun regenerateWishKey(): ResponseEntity<WishKeyDto> {
        val email = getCurrentUserEmail()
        val newWishKey = wishKeyService.regenerateWishKey(email)
        return ResponseEntity.ok(WishKeyDto(newWishKey.key))
    }

    @Operation(summary = "Получение истории просмотров ключей текущего пользователя")
    @GetMapping("/key-view-logs/my")
    fun getKeyViewLogsForCurrentUser(): ResponseEntity<List<KeyViewLog>> {
        val email = getCurrentUserEmail()
        val logs = keyViewLogService.getKeyViewLogsForCurrentUser(email)
        return ResponseEntity.ok(logs)
    }
}