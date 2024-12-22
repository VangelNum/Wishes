package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.model.WishCreationRequest
import com.vangelnum.app.wisher.service.WishService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Пожелания")
@RestController
@RequestMapping("/api/v1/wish")
class WishController(
    private val wishService: WishService,
) {
    @Operation(summary = "Создание пожелания")
    @PostMapping
    fun createWish(@RequestBody wishCreationRequest: WishCreationRequest): ResponseEntity<Wish> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        val createdWish = wishService.createWish(wishCreationRequest, email)
        return ResponseEntity(createdWish, HttpStatus.CREATED)
    }

    @Operation(summary = "Получение пожеланий пользователя по ключу")
    @GetMapping("/{key}")
    fun getWishesByKey(@PathVariable key: String): ResponseEntity<List<Wish>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        val wishes = wishService.getWishesByKey(key, email)
        return ResponseEntity(wishes, HttpStatus.OK)
    }

    @Operation(summary = "Получение истории просмотров для пожелания")
    @GetMapping("/{wishId}/view-logs")
    fun getViewLogsForWish(@PathVariable wishId: Long): ResponseEntity<List<ViewLog>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        val viewLogs = wishService.getViewLogsForWish(wishId, email)
        return ResponseEntity.ok(viewLogs)
    }

    @Operation(summary = "Удаление пожелания")
    @DeleteMapping("/{id}")
    fun deleteWish(@PathVariable id: Long): ResponseEntity<Void> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        wishService.deleteWish(id, email)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Получение списка своих пожеланий")
    @GetMapping("/my")
    fun getCurrentUserWishes(): ResponseEntity<List<Wish>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        val wishes = wishService.getUserWishes(email)
        return ResponseEntity.ok(wishes)
    }
}