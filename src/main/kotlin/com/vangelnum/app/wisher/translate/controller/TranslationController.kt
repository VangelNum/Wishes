package com.vangelnum.app.wisher.translate.controller

import com.vangelnum.app.wisher.translate.service.TranslationService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@Tag(name = "Переводчик")
@RequestMapping("/api/v1/translate/")
@RestController
class TranslationController(
    private val translationService: TranslationService
) {
    @GetMapping
    fun translateText(
        @RequestParam("text") text: String,
        @RequestParam("langpair") langpair: String = "ru|en"
    ): ResponseEntity<String> {
        val translatedText = translationService.translate(text, langpair)
        return ResponseEntity.ok(translatedText)
    }
}