package com.vangelnum.app.wisher.repository

import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.entity.WishKey
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WishKeyRepository : JpaRepository<WishKey, Long> {
    fun findByKey(key: String): Optional<WishKey>
    fun findByUser(user: User): Optional<WishKey>
}