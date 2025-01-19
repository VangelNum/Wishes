package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.model.Holiday

interface HolidayService {
    suspend fun getHolidays(date: String): List<Holiday>
}