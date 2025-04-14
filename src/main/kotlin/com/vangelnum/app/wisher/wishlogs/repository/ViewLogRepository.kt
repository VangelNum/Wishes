package com.vangelnum.app.wisher.wishlogs.repository

import com.vangelnum.app.wisher.wishlogs.entity.ViewLog
import org.springframework.data.jpa.repository.JpaRepository

interface ViewLogRepository : JpaRepository<ViewLog, Long> {
    fun countByWishId(wishId: Long): Long
    fun findByWishId(wishId: Long): List<ViewLog>
    fun existsByWishIdAndViewerId(wishId: Long, viewerId: Long): Boolean
    fun deleteByViewerId(viewerId: Long): Long
    fun deleteByWishOwnerId(wishOwnerId: Long): Long
}