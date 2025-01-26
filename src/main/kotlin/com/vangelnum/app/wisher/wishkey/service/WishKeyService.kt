package com.vangelnum.app.wisher.wishkey.service

import com.vangelnum.app.wisher.wishkey.entity.WishKey

interface WishKeyService {
    fun generateWishKey(email: String): WishKey
    fun getWishKeyForCurrentUser(email: String): WishKey?
    fun regenerateWishKey(email: String): WishKey
}