package com.vangelnum.app.wisher.model

data class UpdateProfileRequest(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val avatarUrl: String? = null
)