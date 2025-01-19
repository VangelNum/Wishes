package com.vangelnum.app.wisher.controller

import com.vangelnum.app.wisher.model.DateInfo
import com.vangelnum.app.wisher.service.WorldTimeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Время")
@RestController
@RequestMapping("/api/v1/world-time")
class WorldTimeController(private val worldTimeService: WorldTimeService) {
    @GetMapping("/now")
    @Operation(summary = "Получение текущего времени")
    suspend fun getCurrentDate(@RequestParam("tz") timezone: String?): DateInfo {
        return worldTimeService.getCurrentDate(timezone ?: "Europe/Moscow")
    }
}