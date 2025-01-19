package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.service.UploadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Загрузка на сервер")
@RestController
@RequestMapping("/api/v1/upload")
class UploadController(private val uploadService: UploadService) {

    @Operation(summary = "Изображение")
    @PostMapping("/image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun uploadImage(@RequestParam("image") image: MultipartFile): ResponseEntity<String> {
        return try {
            val imageUrl = uploadService.uploadImage(image)
            ResponseEntity.ok(imageUrl)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("Failed to upload image")
        }
    }
}