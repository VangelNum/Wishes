package com.vangelnum.app.wisher.user.model

data class DailyBonusStateResponse(
    val currentStreak: Int,
    val nextBonusCoins: Int,
    val remainingHours: Long,
    val remainingMinutes: Long
)