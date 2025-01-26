package com.vangelnum.app.wisher.wishkey.entity

import com.vangelnum.app.wisher.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "wish_keys")
data class WishKey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val key: String,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)