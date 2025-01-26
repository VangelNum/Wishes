package com.vangelnum.app.wisher.wishlogs.entity

import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.wish.entity.Wish
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "view_logs")
data class ViewLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "viewer_id", nullable = false)
    val viewer: User,

    @ManyToOne
    @JoinColumn(name = "wish_owner_id", nullable = false)
    val wishOwner: User,

    @ManyToOne
    @JoinColumn(name = "wish_id", nullable = false)
    val wish: Wish,

    @Column(nullable = false)
    val viewTime: LocalDateTime = LocalDateTime.now()
)