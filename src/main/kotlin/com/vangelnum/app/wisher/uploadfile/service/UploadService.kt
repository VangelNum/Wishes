package com.vangelnum.app.wisher.uploadfile.service

import org.springframework.web.multipart.MultipartFile

interface UploadService {
    suspend fun uploadImage(image: MultipartFile): String
}