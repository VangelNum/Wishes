package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.core.utils.getCurrentUserEmail
import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.model.WishCreationRequest
import com.vangelnum.app.wisher.model.WishDateResponse
import com.vangelnum.app.wisher.model.WishResponse
import com.vangelnum.app.wisher.model.WishUpdateRequest
import com.vangelnum.app.wisher.service.WishService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalStateException

@Tag(name = "Пожелания")
@RestController
@RequestMapping("/api/v1/wish")
class WishController(
    private val wishService: WishService,
) {
    @Operation(summary = "Создание пожелания")
    @PostMapping
    fun createWish(@RequestBody wishCreationRequest: WishCreationRequest): ResponseEntity<Wish> {
        val email = getCurrentUserEmail()
        val createdWish = wishService.createWish(wishCreationRequest, email)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWish)
    }

    @Operation(summary = "Получение дат поздравлений пользователя по ключу")
    @GetMapping("/{key}")
    fun getWishDatesByKey(@PathVariable key: String): ResponseEntity<List<WishDateResponse>> {
        val email = getCurrentUserEmail()
        val wishDates = wishService.getWishDatesByKey(key, email)
        return ResponseEntity.ok(wishDates)
    }

    @Operation(summary = "Получение пожелания по ключу и id")
    @GetMapping("/{key}/{wishId}")
    fun getWishByKeyAndId(
        @PathVariable key: String,
        @PathVariable wishId: Int
    ): ResponseEntity<*> {
        val email = getCurrentUserEmail()
        return try {
            val wish = wishService.getWishByKeyAndId(key, wishId, email)
            ResponseEntity.ok(wish)
        } catch (e: IllegalStateException) {
            throw e
        }
    }

    @Operation(summary = "Получение истории просмотров для пожелания")
    @GetMapping("/{wishId}/view-logs")
    fun getViewLogsForWish(@PathVariable wishId: Long): ResponseEntity<List<ViewLog>> {
        val email = getCurrentUserEmail()
        val viewLogs = wishService.getViewLogsForWish(wishId, email)
        return ResponseEntity.ok(viewLogs)
    }

    @Operation(summary = "Удаление пожелания")
    @DeleteMapping("/{id}")
    fun deleteWish(@PathVariable id: Long): ResponseEntity<Void> {
        val email = getCurrentUserEmail()
        wishService.deleteWish(id, email)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Получение списка своих пожеланий")
    @GetMapping("/my")
    fun getCurrentUserWishes(): ResponseEntity<List<WishResponse>> {
        val email = getCurrentUserEmail()
        val wishes = wishService.getUserWishes(email)
        return ResponseEntity.ok(wishes)
    }

    @Operation(summary = "Обновление пожелания")
    @PutMapping("/{id}")
    fun updateWish(@PathVariable id: Long, @RequestBody wishUpdateRequest: WishUpdateRequest): ResponseEntity<Wish> {
        val email = getCurrentUserEmail()
        val updatedWish = wishService.updateWish(id, wishUpdateRequest, email)
        return ResponseEntity.ok(updatedWish)
    }
}