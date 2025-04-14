package com.vangelnum.app.wisher.wishkeylogs.entity

import com.vangelnum.app.wisher.user.entity.User
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
@Table(name = "key_view_logs")
data class KeyViewLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val key: String,

    @ManyToOne
    @JoinColumn(name = "viewer_id", nullable = false)
    val viewer: User,

    @Column(nullable = false)
    val viewedAt: LocalDateTime = LocalDateTime.now()
)