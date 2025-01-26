package com.vangelnum.app.wisher.holiday.service

import com.vangelnum.app.wisher.holiday.model.Holiday

interface HolidayService {
    suspend fun getHolidays(date: String): List<Holiday>
}