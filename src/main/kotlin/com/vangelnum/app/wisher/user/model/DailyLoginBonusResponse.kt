package com.vangelnum.app.wisher.user.model

data class DailyLoginBonusResponse(
    val coinsAwarded: Int,
    val currentStreak: Int,
    val nextBonusCoins: Int
)