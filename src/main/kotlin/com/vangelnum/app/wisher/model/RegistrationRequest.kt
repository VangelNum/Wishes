package com.vangelnum.app.wisher.model

data class RegistrationRequest(
    val name: String,
    val password: String,
    val email: String
)