package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.model.DateInfo
import com.vangelnum.app.wisher.service.WorldTimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class WorldTimeServiceImpl(
    private val restTemplate: RestTemplate,
) : WorldTimeService {
    private val baseUrl = "https://tools.aimylogic.com/api/"

    override suspend fun getCurrentDate(timezone: String): DateInfo {
        val url = "$baseUrl/now?tz=$timezone"
        return try {
            withContext(Dispatchers.IO) {
                restTemplate.getForObject(url, DateInfo::class.java)
            } ?: throw RuntimeException("Failed to retrieve date info")
        } catch (e: RestClientException) {
            throw e
        }
    }
}
