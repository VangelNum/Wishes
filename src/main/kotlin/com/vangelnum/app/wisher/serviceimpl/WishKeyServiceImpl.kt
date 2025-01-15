package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.entity.WishKey
import com.vangelnum.app.wisher.repository.UserRepository
import com.vangelnum.app.wisher.repository.WishKeyRepository
import com.vangelnum.app.wisher.service.WishKeyService
import org.springframework.stereotype.Service
import java.util.*

@Service
class WishKeyServiceImpl(
    private val userRepository: UserRepository,
    private val wishKeyRepository: WishKeyRepository
) : WishKeyService {

    private fun getUserByEmail(email: String) =
        userRepository.findByEmail(email).orElseThrow { Exception("User not found") }

    override fun generateWishKey(email: String): WishKey {
        val user = getUserByEmail(email)
        val key = UUID.randomUUID().toString().take(15)
        val wishKey = WishKey(
            key = key,
            user = user
        )
        return wishKeyRepository.save(wishKey)
    }

    override fun getWishKeyForCurrentUser(email: String): WishKey? {
        val user = getUserByEmail(email)
        return wishKeyRepository.findByUser(user).orElse(null)
    }

    override fun regenerateWishKey(email: String): WishKey {
        val user = getUserByEmail(email)
        val existingWishKey = wishKeyRepository.findByUser(user)
            .orElseThrow { Exception("Wish key not found for the current user") } // Or handle this case differently, e.g., generate a new one if it doesn't exist

        val newKey = UUID.randomUUID().toString()
        val updatedWishKey = existingWishKey.copy(key = newKey)
        return wishKeyRepository.save(updatedWishKey)
    }
}