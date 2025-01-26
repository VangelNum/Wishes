package com.vangelnum.app.wisher.uploadfile.serviceimpl

import com.vangelnum.app.wisher.uploadfile.model.ImgbbResponse
import com.vangelnum.app.wisher.uploadfile.service.UploadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@Service
class UploadServiceImpl(private val restTemplate: RestTemplate) : UploadService {

    private val imgbbApiKey = "f90248ad8f4b1e262a5e8e7603645cc1"
    private val imgbbApiUrl = "https://api.imgbb.com/1/upload"

    override suspend fun uploadImage(image: MultipartFile): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
        }

        val body = LinkedMultiValueMap<String, Any>().apply {
            add("key", imgbbApiKey)
            add("image", object : ByteArrayResource(image.bytes) {
                override fun getFilename(): String {
                    return image.originalFilename ?: "image.png"
                }
            })
        }

        val requestEntity = HttpEntity(body, headers)

        return try {
            val response = withContext(Dispatchers.IO) {
                restTemplate.postForEntity(imgbbApiUrl, requestEntity, ImgbbResponse::class.java)
            }
            response.body?.data?.url ?: throw RuntimeException("Failed to get image URL from Imgbb response")
        } catch (e: Exception) {
            throw RuntimeException("Error uploading image to Imgbb: ${e.message}")
        }
    }
}