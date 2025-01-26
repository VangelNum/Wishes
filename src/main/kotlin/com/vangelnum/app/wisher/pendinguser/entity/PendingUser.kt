package com.vangelnum.app.wisher.pendinguser.entity

import com.vangelnum.app.wisher.core.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "pending_users")
data class PendingUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val password: String,
    val email: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
    var verificationCode: String?
)