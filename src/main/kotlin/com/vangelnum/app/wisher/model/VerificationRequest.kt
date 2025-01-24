package com.vangelnum.app.wisher.model

data class VerificationRequest(
    val email: String,
    val verificationCode: String
)