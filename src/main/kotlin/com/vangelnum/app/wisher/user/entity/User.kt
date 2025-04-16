package com.vangelnum.app.wisher.user.entity

import com.vangelnum.app.wisher.core.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column
    var avatarUrl: String? = null,

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER,

    @Column
    var coins: Int,

    var verificationCode: String? = null,

    @Column
    var isEmailVerified: Boolean = false,

    @Column
    val registrationTime: LocalDateTime = LocalDateTime.now(),

    @Column
    var lastLoginTime: LocalDateTime? = null,

    @Column(nullable = false)
    var wishesCreatedCount: Int = 0,

    @Column
    var dailyLoginBonusStreak: Int = 0,

    @Column
    var lastDailyLoginBonusTime: LocalDateTime? = null
)