package com.vangelnum.app.wisher.user.model

data class UpdateProfileRequest(
    val name: String? = null,
    val email: String? = null,
    val avatarUrl: String? = null,
    val newPassword: String? = null,
    val currentPassword: String
)