package com.vangelnum.app.wisher.repository

import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.entity.Wish
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WishRepository : JpaRepository<Wish, Long> {
    fun findByUser(user: User): List<Wish>
    fun findByUserAndId(user: User, id: Long): Optional<Wish> // Changed wishId to id
}