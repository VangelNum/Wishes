package com.vangelnum.app.wisher.wishkeylogs.repository

import com.vangelnum.app.wisher.wishkeylogs.entity.KeyViewLog
import com.vangelnum.app.wisher.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface KeyViewLogRepository : JpaRepository<KeyViewLog, Long> {
    fun findByViewer(viewer: User): List<KeyViewLog>
}