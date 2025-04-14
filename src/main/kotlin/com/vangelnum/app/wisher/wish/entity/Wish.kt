package com.vangelnum.app.wisher.wish.entity

import com.vangelnum.app.wisher.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "wishes")
data class Wish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    val text: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val wishDate: LocalDate,

    @Column(columnDefinition = "TEXT")
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