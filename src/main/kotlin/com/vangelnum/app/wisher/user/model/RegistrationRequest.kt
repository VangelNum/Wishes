package com.vangelnum.app.wisher.user.model

data class RegistrationRequest(
    val name: String,
    val password: String,
    val email: String
)