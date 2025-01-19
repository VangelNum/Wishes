package com.vangelnum.app.wisher.service

import org.springframework.web.multipart.MultipartFile

interface UploadService {
    suspend fun uploadImage(image: MultipartFile): String
}