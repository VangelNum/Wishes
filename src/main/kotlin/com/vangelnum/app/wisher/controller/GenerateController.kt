package com.vangelnum.app.wisher.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.*

@RestController
@Tag(name = "Генерация")
@RequestMapping("/api/v1/generate")
class GenerateController {

    private val baseUrl = "https://lalashechka-lora-sdxl.hf.space"
    private val restTemplate = RestTemplate()
    private val objectMapper = ObjectMapper()

    private fun selectLora(session: String, loraCaption: String): JsonNode {
        val url = "$baseUrl/gradio_api/queue/join?"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("origin", baseUrl)
            set("referer", "$baseUrl/")
            set(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
            )
        }
        val data = mapOf(
            "data" to emptyList<Any>(),
            "event_data" to mapOf(
                "index" to 61,
                "value" to mapOf(
                    "caption" to loraCaption,
                    "image" to mapOf(
                        "is_stream" to false,
                        "meta" to mapOf("_type" to "gradio.FileData"),
                        "mime_type" to "image/jpeg",
                        "orig_name" to "3822700.jpeg",
                        "path" to "https://huggingface.co/KappaNeuro/vintage-postage-stamps/resolve/main/2332770.jpeg",
                        "size" to null,
                        "url" to "https://huggingface.co/KappaNeuro/vintage-postage-stamps/resolve/main/2332770.jpeg"
                    )
                )
            ),
            "fn_index" to 0,
            "trigger_id" to 6,
            "session_hash" to session
        )
        val requestEntity = HttpEntity(data, headers)
        val response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
        if (response.statusCode != HttpStatus.OK) {
            throw RuntimeException("Error selecting LoRA: ${response.statusCode} - ${response.body}")
        }
        return objectMapper.readTree(response.body)
    }

    private fun waitForStatus(session: String): JsonNode? {
        val url = "$baseUrl/gradio_api/queue/data?session_hash=$session"
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.TEXT_EVENT_STREAM)
            set(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
            )
            set("referer", "$baseUrl/")
        }
        val requestEntity = HttpEntity<Any>(headers)
        val response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String::class.java)

        if (response.statusCode != HttpStatus.OK) {
            throw RuntimeException("Error waiting for status: ${response.statusCode} - ${response.body}")
        }

        response.body?.lines()?.forEach { line ->
            if (line.startsWith("data:")) {
                try {
                    val eventData = objectMapper.readTree(line.substring(5))
                    println("Статус: ${eventData.get("msg")?.asText()}")
                    if (eventData.get("msg")?.asText() == "process_completed") {
                        return eventData
                    }
                } catch (e: Exception) {
                    println("Не удалось декодировать JSON: $line")
                }
            }
        }
        return null
    }

    private fun generateImage(session: String, prompt: String): JsonNode {
        val url = "$baseUrl/gradio_api/queue/join?"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("origin", baseUrl)
            set("referer", "$baseUrl/")
            set(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
            )
        }
        val data = mapOf(
            "data" to listOf(null, prompt),
            "event_data" to null,
            "fn_index" to 1,
            "trigger_id" to 8,
            "session_hash" to session
        )
        val requestEntity = HttpEntity(data, headers)
        val response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
        if (response.statusCode != HttpStatus.OK) {
            throw RuntimeException("Error generating image: ${response.statusCode} - ${response.body}")
        }
        return objectMapper.readTree(response.body)
    }

    @GetMapping("/image")
    fun generateImageWithLora(@RequestParam prompt: String): ResponseEntity<String> {
        val sessionHash = UUID.randomUUID().toString()
        val loraCaption = "KappaNeuro/vintage-postage-stamps"
        println("Выбираем LoRA: $loraCaption")

        try {
            selectLora(sessionHash, loraCaption)
            println("Ожидание подтверждения выбора LoRA...")
            val selectStatus = waitForStatus(sessionHash)
            if (selectStatus?.get("success")?.asBoolean() == true) {
                println("LoRA успешно выбрана.")
            } else {
                println("Ошибка при выборе LoRA.")
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выборе LoRA.")
            }

            println("Отправляем запрос на генерацию: $prompt")
            generateImage(sessionHash, prompt)
            println("Ожидание завершения генерации...")
            val generationStatus = waitForStatus(sessionHash)

            return if (generationStatus?.get("success")?.asBoolean() == true) {
                val imageUrl = generationStatus.get("output")?.get("data")?.get(0)?.get("url")?.asText()
                println("URL изображения: $imageUrl")
                ResponseEntity.ok(imageUrl)
            } else {
                println("Ошибка при генерации изображения.")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при генерации изображения.")
            }

        } catch (e: Exception) {
            println("Произошла ошибка: ${e.message}")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка: ${e.message}")
        }
    }
}