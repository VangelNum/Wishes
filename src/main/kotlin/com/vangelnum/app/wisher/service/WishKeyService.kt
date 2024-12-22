package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.entity.WishKey

interface WishKeyService {
    fun generateWishKey(email: String): WishKey
    fun getWishKeyForCurrentUser(email: String): WishKey?
    fun regenerateWishKey(email: String): WishKey
}