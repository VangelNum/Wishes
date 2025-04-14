package com.vangelnum.app.wisher.wish.repository

import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.wish.entity.Wish
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface WishRepository : JpaRepository<Wish, Long> {
    fun findByUser(user: User): List<Wish>
    fun findByUserAndId(user: User, id: Long): Optional<Wish>
    fun deleteByUserId(userId: Long): Long
}