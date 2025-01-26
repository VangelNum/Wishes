package com.vangelnum.app.wisher.wish.model

data class WishCreationRequest(
    val text: String,
    val wishDate: String,
    val openDate: String,
    val image: String? = null,
    val maxViewers: Int? = null,
    val isBlurred: Boolean,
    val cost: Int
)