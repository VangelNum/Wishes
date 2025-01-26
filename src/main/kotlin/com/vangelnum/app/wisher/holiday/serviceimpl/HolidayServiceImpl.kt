package com.vangelnum.app.wisher.holiday.serviceimpl

import com.vangelnum.app.wisher.holiday.model.Holiday
import com.vangelnum.app.wisher.holiday.service.HolidayService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HolidayServiceImpl : HolidayService {

    private val logger = LoggerFactory.getLogger(HolidayServiceImpl::class.java)

    override suspend fun getHolidays(date: String): List<Holiday> {
        return withContext(Dispatchers.IO) {
            try {
                val parts = date.split("-")
                val day = parts[1].removePrefix("0")
                val monthNumber = parts[2].removePrefix("0")
                val url = "https://www.calend.ru/holidays/$day-$monthNumber/"

                logger.info("Fetching holidays from: $url")
                val response: String = Jsoup.connect(url).get().html()

                val doc: Document = Jsoup.parse(response)
                mapHolidays(doc)
            } catch (e: Exception) {
                logger.error("Error fetching holidays for date $date", e)
                emptyList()
            }
        }
    }

    private fun mapHolidays(document: Document): List<Holiday> {
        val holidaysList = mutableListOf<Holiday>()
        val holidayNameElements = document.select(".block.datesList .block.holidays ul.itemsNet li .caption span.title a")
        holidayNameElements.forEach { element ->
            holidaysList.add(Holiday(element.text()))
        }
        return holidaysList
    }
}
