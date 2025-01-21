package com.vangelnum.app.wisher.model

data class WishResponse(
    val id: Long,
    val text: String,
    val wishDate: String,
    val image: String?,
    val openDate: String,
    val maxViewers: Int?,
    val isBlurred: Boolean,
    val cost: Int
)