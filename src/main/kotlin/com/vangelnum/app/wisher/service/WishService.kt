package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.model.WishCreationRequest

interface WishService {
    fun createWish(wishCreationRequest: WishCreationRequest, email: String): Wish
    fun getWishesByKey(key: String, viewerEmail: String): List<Wish>
    fun getViewLogsForWish(wishId: Long, userEmail: String): List<ViewLog>
    fun deleteWish(id: Long, userEmail: String)
    fun getUserWishes(email: String): List<Wish>
}