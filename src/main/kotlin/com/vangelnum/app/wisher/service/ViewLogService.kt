package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish

interface ViewLogService {
    fun createViewLog(viewer: User, wishOwner: User, wish: Wish): ViewLog
}