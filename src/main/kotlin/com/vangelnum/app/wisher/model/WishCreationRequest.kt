package com.vangelnum.app.wisher.model

data class WishCreationRequest(
    val text: String,
    val wishDate: String,
    val openDate: String,
    val image: String? = null,
    val maxViewers: Int? = null,
    val isBlurred: Boolean? = null,
    val cost: Int
)