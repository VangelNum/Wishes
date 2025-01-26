package com.vangelnum.app.wisher.wishlogs.service

import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.wishlogs.entity.ViewLog
import com.vangelnum.app.wisher.wish.entity.Wish

interface ViewLogService {
    fun createViewLog(viewer: User, wishOwner: User, wish: Wish): ViewLog
}