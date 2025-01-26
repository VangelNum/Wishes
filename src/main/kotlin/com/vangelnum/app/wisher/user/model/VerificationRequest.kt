package com.vangelnum.app.wisher.user.model

data class VerificationRequest(
    val email: String,
    val verificationCode: String
)