package com.vangelnum.app.wisher.user.repository

import com.vangelnum.app.wisher.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}