package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.service.EmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    @Value("\${MAIL_USERNAME}") private val mailUsername: String
): EmailService {

    override fun sendVerificationEmail(to: String, verificationCode: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Подтверждение регистрации на Wishes App"
        message.text = """
                Здравствуйте!
    
                Благодарим вас за регистрацию в Wishes App.
                Для подтверждения вашего email, пожалуйста, введите следующий код подтверждения:
    
                Код подтверждения: $verificationCode
    
                С уважением,
                Создатель приложения Wishes.
            """.trimIndent()
        message.from = mailUsername

        mailSender.send(message)
    }
}