package com.vangelnum.app.wisher.repository

import com.vangelnum.app.wisher.entity.ViewLog
import org.springframework.data.jpa.repository.JpaRepository

interface ViewLogRepository : JpaRepository<ViewLog, Long> {
    fun countByWishId(wishId: Long): Int
    fun findByWishId(wishId: Long): List<ViewLog>
}
