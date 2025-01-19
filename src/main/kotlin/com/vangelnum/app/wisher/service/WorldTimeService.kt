package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.model.DateInfo


interface WorldTimeService {
    suspend fun getCurrentDate(timezone: String): DateInfo
}