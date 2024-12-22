package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.repository.ViewLogRepository
import com.vangelnum.app.wisher.service.ViewLogService
import org.springframework.stereotype.Service

@Service
class ViewLogServiceImpl(
    private val viewLogRepository: ViewLogRepository
): ViewLogService {
    override fun createViewLog(viewer: User, wishOwner: User, wish: Wish): ViewLog {
        val viewLog = ViewLog(
            viewer = viewer,
            wishOwner = wishOwner,
            wish = wish
        )
        return viewLogRepository.save(viewLog)
    }
}