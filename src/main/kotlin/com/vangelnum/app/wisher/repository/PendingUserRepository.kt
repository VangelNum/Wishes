package com.vangelnum.app.wisher.repository

import com.vangelnum.app.wisher.entity.PendingUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PendingUserRepository : JpaRepository<PendingUser, Long> {
    fun findByEmail(email: String): Optional<PendingUser>
}