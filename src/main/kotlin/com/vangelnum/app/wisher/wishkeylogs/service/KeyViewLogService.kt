package com.vangelnum.app.wisher.wishkeylogs.service

import com.vangelnum.app.wisher.wishkeylogs.entity.KeyViewLog

interface KeyViewLogService {
    fun createKeyViewLogForCurrentUser(key: String, viewerEmail: String): KeyViewLog
    fun getKeyViewLogsForCurrentUser(viewerEmail: String): List<KeyViewLog>
}