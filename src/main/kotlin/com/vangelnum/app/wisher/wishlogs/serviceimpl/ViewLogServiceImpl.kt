package com.vangelnum.app.wisher.wishlogs.serviceimpl

import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.wishlogs.entity.ViewLog
import com.vangelnum.app.wisher.wish.entity.Wish
import com.vangelnum.app.wisher.wishlogs.repository.ViewLogRepository
import com.vangelnum.app.wisher.wishlogs.service.ViewLogService
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