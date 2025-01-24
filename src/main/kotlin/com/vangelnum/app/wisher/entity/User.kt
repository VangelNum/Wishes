package com.vangelnum.app.wisher.entity

import com.vangelnum.app.wisher.core.enums.Role
import jakarta.persistence.*


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
    var isEmailVerified: Boolean = false
)