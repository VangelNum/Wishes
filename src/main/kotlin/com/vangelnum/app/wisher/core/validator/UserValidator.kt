package com.vangelnum.app.wisher.core.validator

import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest
import com.vangelnum.app.wisher.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.stream.Stream

@Component
class UserValidator(
    private val userRepository: UserRepository
) {
    companion object {
        private val EMAIL_VALIDATION_REGEX = Regex("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$")
        private val AVATAR_URL_VALIDATION_REGEX =
            Regex("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\$")
    }

    fun checkUser(registrationRequest: RegistrationRequest): Status {
        return Stream.of(
            checkName(registrationRequest.name),
            checkPassword(registrationRequest.password),
            checkEmail(registrationRequest.email),
            registrationRequest.avatarUrl?.let { checkAvatarUrl(it) } ?: Status(true)
        )
            .filter { !it.isSuccess }
            .findFirst()
            .orElse(Status(true, "Данные валидны"))
    }

    fun checkUser(updateProfileRequest: UpdateProfileRequest, id: Long): Status {
        return Stream.of(
            updateProfileRequest.name?.let { checkName(it) } ?: Status(true),
            updateProfileRequest.email?.let { checkEmail(it, id) } ?: Status(true),
            updateProfileRequest.avatarUrl?.let { checkAvatarUrl(it) } ?: Status(true)
        )
            .filter { !it.isSuccess }
            .findFirst()
            .orElse(Status(true, "Данные валидны"))
    }

    private fun checkName(name: String): Status {
        if (!StringUtils.hasText(name)) {
            return Status(false, "Имя не может быть пустым")
        }
        if (name.length < 2 || name.length > 30) {
            return Status(false, "Имя должно содержать не менее 2 и не более 30 символов")
        }
        return Status(true)
    }

    private fun checkPassword(password: String): Status {
        if (!StringUtils.hasText(password)) {
            return Status(false, "Пароль не может быть пустым")
        }
        if (password.length < 8 || password.length > 30) {
            return Status(false, "Пароль должен содержать не менее 8 и не более 30 символов")
        }
        return Status(true)
    }

    private fun checkEmail(email: String, id: Long): Status {
        if (!StringUtils.hasText(email)) {
            return Status(false, "Почта не может быть пустой")
        }
        if (!EMAIL_VALIDATION_REGEX.matches(email)) {
            return Status(false, "Почта имеет неверный формат")
        }
        if (userRepository.findByEmail(email).isPresent && userRepository.findByEmail(email).get().id != id) {
            return Status(false, "Пользователь с таким e-mail уже существует")
        }
        return Status(true)
    }

    private fun checkEmail(email: String): Status {
        if (!StringUtils.hasText(email)) {
            return Status(false, "Почта не может быть пустой")
        }
        if (!EMAIL_VALIDATION_REGEX.matches(email)) {
            return Status(false, "Почта имеет неверный формат")
        }
        if (userRepository.findByEmail(email).isPresent) {
            return Status(false, "Пользователь с таким e-mail уже существует")
        }
        return Status(true)
    }

    private fun checkAvatarUrl(avatarUrl: String): Status {
        if (!AVATAR_URL_VALIDATION_REGEX.matches(avatarUrl)) {
            return Status(false, "Не верный формат ссылки")
        }
        return Status(true)
    }
}