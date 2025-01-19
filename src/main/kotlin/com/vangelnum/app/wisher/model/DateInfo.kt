package com.vangelnum.app.wisher.model

data class DateInfo(
    val day: Int,
    val formatted: String,
    val hour: Int,
    val minute: Int,
    val month: Int,
    val timestamp: Long,
    val timezone: String,
    val weekDay: Int,
    val year: Int
)