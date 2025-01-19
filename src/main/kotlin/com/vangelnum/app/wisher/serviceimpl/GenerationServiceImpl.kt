package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.service.GenerationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class GenerationServiceImpl(private val restTemplate: RestTemplate) : GenerationService {

    private val baseUrl = "https://image.pollinations.ai"

    override suspend fun generateImage(
        prompt: String,
        model: String?,
        seed: Int?,
        width: Int?,
        height: Int?,
        nologo: Boolean?,
        private: Boolean?,
        enhance: Boolean?,
        safe: Boolean?
    ): ByteArray {
        val uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .pathSegment("prompt", prompt)
            .apply {
                model?.let { queryParam("model", it) }
                seed?.let { queryParam("seed", it) }
                width?.let { queryParam("width", it) }
                height?.let { queryParam("height", it) }
                nologo?.let { queryParam("nologo", it) }
                private?.let { queryParam("private", it) }
                enhance?.let { queryParam("enhance", it) }
                safe?.let { queryParam("safe", it) }
            }
            .build()
            .toUri()

        val response = withContext(Dispatchers.IO) {
            restTemplate.getForEntity(uri, ByteArray::class.java)
        }

        if (response.statusCode == HttpStatus.OK && response.body != null) {
            return response.body!!
        } else {
            throw RuntimeException("Failed to fetch image. Status code: ${response.statusCode}")
        }
    }

    override suspend fun getListOfModels(): List<String> {
        return withContext(Dispatchers.IO) {
            restTemplate.getForObject("$baseUrl/models", Array<String>::class.java)
        }?.toList() ?: emptyList()
    }
}