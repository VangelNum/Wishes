package com.vangelnum.app.wisher.holiday.controller

import com.vangelnum.app.wisher.holiday.model.Holiday
import com.vangelnum.app.wisher.holiday.service.HolidayService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Праздники")
@RestController
@RequestMapping("/api/v1/holidays")
class HolidayController(private val holidayService: HolidayService) {

    @GetMapping
    @Operation(summary = "Получение списка праздников на дату в формате 2025-01-30")
    suspend fun getHolidays(@RequestParam("date") date: String): ResponseEntity<List<Holiday>> {
        return try {
            val holidays = holidayService.getHolidays(date)
            if (holidays.isEmpty()) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.ok(holidays)
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}