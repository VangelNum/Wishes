package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.service.GenerationService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Генерация")
@RestController
@RequestMapping("/api/v1/generate")
class ImageGenerationController(private val generationService: GenerationService) {

    @GetMapping("/image/{prompt}")
    suspend fun generateImage(
        @PathVariable prompt: String,
        @RequestParam(required = false) model: String?,
        @RequestParam(required = false) seed: Int?,
        @RequestParam(required = false) width: Int?,
        @RequestParam(required = false) height: Int?,
        @RequestParam(required = false) nologo: Boolean?,
        @RequestParam(required = false) private: Boolean?,
        @RequestParam(required = false) enhance: Boolean?,
        @RequestParam(required = false) safe: Boolean?
    ): ResponseEntity<ByteArrayResource> {
        val imageBytes = generationService.generateImage(prompt, model, seed, width, height, nologo, private, enhance, safe)
        val resource = ByteArrayResource(imageBytes)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(resource)
    }

    @GetMapping("/image/models")
    suspend fun getListOfModels(): ResponseEntity<List<String>> {
        val models = generationService.getListOfModels()
        return ResponseEntity.ok(models)
    }
}