package com.vangelnum.app.wisher.model

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val password: String,
    val avatarUrl: String? = null
)