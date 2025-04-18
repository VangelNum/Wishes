package com.vangelnum.app.wisher.user.model

import java.time.LocalDateTime

data class AdRewardResponse(
    val coinsAwarded: Int,
    val message: String,
    val nextAdRewardAvailableTime: LocalDateTime? = null
)