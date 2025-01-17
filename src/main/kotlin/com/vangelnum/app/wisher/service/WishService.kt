package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.model.WishCreationRequest
import com.vangelnum.app.wisher.model.WishDateResponse

interface WishService {
    fun createWish(wishCreationRequest: WishCreationRequest, email: String): Wish
    fun getWishDatesByKey(key: String, viewerEmail: String): List<WishDateResponse>
    fun getWishByKeyAndId(key: String, wishId: Int, viewerEmail: String): Wish
    fun getViewLogsForWish(wishId: Long, userEmail: String): List<ViewLog>
    fun deleteWish(id: Long, userEmail: String)
    fun getUserWishes(email: String): List<Wish>
}