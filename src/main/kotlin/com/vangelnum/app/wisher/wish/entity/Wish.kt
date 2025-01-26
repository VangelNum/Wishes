package com.vangelnum.app.wisher.wish.entity

import com.vangelnum.app.wisher.user.entity.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "wishes")
data class Wish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val text: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val wishDate: LocalDate,

    @Column
    val image: String? = null,

    @Column(nullable = false)
    val openDate: LocalDate,

    @Column(nullable = true)
    val maxViewers: Int? = null,

    @Column(nullable = false)
    val isBlurred: Boolean = false,

    @Column(nullable = false)
    val cost: Int
)