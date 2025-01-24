package com.vangelnum.app.wisher.service


interface EmailService {
    fun sendVerificationEmail(to: String, verificationCode: String)
}