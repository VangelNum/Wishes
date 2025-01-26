package com.vangelnum.app.wisher.user.service


interface EmailService {
    fun sendVerificationEmail(to: String, verificationCode: String)
}