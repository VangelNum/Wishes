package com.vangelnum.app.wisher.wish.conroller

import com.vangelnum.app.wisher.core.utils.getCurrentUserEmail
import com.vangelnum.app.wisher.wish.entity.Wish
import com.vangelnum.app.wisher.wish.model.WishCreationRequest
import com.vangelnum.app.wisher.wish.model.WishDateResponse
import com.vangelnum.app.wisher.wish.model.WishResponse
import com.vangelnum.app.wisher.wish.model.WishUpdateRequest
import com.vangelnum.app.wisher.wish.service.WishService
import com.vangelnum.app.wisher.wishlogs.entity.ViewLog
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Пожелания")
@RestController
@RequestMapping("/api/v1/wish")
class WishController(
    private val wishService: WishService
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
    ): ResponseEntity<Wish> {
        val email = getCurrentUserEmail()
        val wish = wishService.getWishByKeyAndId(key, wishId, email)
        return ResponseEntity.ok(wish)
    }

    @Operation(summary = "Получение последнего пожелания пользователя по ключу")
    @GetMapping("/{key}/last")
    fun getLastWishByKey(@PathVariable key: String): ResponseEntity<Wish> {
        val email = getCurrentUserEmail()
        val lastWish = wishService.getLastWishByKey(key, email)
        return ResponseEntity.ok(lastWish)
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

    @Operation(summary = "Изменение пожелания")
    @PutMapping("/{id}")
    fun updateWish(@PathVariable id: Long, @RequestBody wishUpdateRequest: WishUpdateRequest): ResponseEntity<Wish> {
        val email = getCurrentUserEmail()
        val updatedWish = wishService.updateWish(id, wishUpdateRequest, email)
        return ResponseEntity.ok(updatedWish)
    }
}