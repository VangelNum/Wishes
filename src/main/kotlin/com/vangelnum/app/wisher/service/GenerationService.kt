package com.vangelnum.app.wisher.service

interface GenerationService {
    suspend fun generateImage(
        prompt: String,
        model: String?,
        seed: Int?,
        width: Int?,
        height: Int?,
        nologo: Boolean?,
        private: Boolean?,
        enhance: Boolean?,
        safe: Boolean?
    ): ByteArray

    suspend fun getListOfModels(): List<String>
}