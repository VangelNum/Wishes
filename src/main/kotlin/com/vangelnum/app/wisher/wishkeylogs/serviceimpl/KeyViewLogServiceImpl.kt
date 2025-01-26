package com.vangelnum.app.wisher.wishkeylogs.serviceimpl

import com.vangelnum.app.wisher.wishkeylogs.entity.KeyViewLog
import com.vangelnum.app.wisher.wishkeylogs.repository.KeyViewLogRepository
import com.vangelnum.app.wisher.user.repository.UserRepository
import com.vangelnum.app.wisher.wishkeylogs.service.KeyViewLogService
import org.springframework.stereotype.Service

@Service
class KeyViewLogServiceImpl(
    private val keyViewLogRepository: KeyViewLogRepository,
    private val userRepository: UserRepository
) : KeyViewLogService {

    private fun getUserByEmail(email: String) = userRepository.findByEmail(email).orElseThrow { NoSuchElementException("Пользователь не найден") }

    override fun createKeyViewLogForCurrentUser(key: String, viewerEmail: String): KeyViewLog {
        val viewer = getUserByEmail(viewerEmail)
        val keyViewLog = KeyViewLog(key = key, viewer = viewer)
        return keyViewLogRepository.save(keyViewLog)
    }

    override fun getKeyViewLogsForCurrentUser(viewerEmail: String): List<KeyViewLog> {
        val viewer = getUserByEmail(viewerEmail)
        return keyViewLogRepository.findByViewer(viewer)
    }
}