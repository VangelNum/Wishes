package com.vangelnum.app.wisher.worldtime.service

import com.vangelnum.app.wisher.worldtime.model.DateInfo


interface WorldTimeService {
    suspend fun getCurrentDate(timezone: String): DateInfo
}