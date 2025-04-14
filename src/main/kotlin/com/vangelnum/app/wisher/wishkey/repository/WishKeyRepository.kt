package com.vangelnum.app.wisher.wishkey.repository

import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.wishkey.entity.WishKey
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface WishKeyRepository : JpaRepository<WishKey, Long> {
    fun findByKey(key: String): Optional<WishKey>
    fun findByUser(user: User): Optional<WishKey>
    fun deleteByUserId(userId: Long)
}