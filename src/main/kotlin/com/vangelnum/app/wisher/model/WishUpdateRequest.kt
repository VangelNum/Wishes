package com.vangelnum.app.wisher.model

import java.time.LocalDate

data class WishUpdateRequest(
    val text: String?,
    val wishDate: LocalDate?,
    val image: String?,
    val openDate: LocalDate?,
    val maxViewers: Int?,
    val isBlurred: Boolean?,
    val cost: Int?
)