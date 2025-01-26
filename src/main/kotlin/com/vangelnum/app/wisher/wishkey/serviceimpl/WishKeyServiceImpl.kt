package com.vangelnum.app.wisher.wishkey.serviceimpl

import com.vangelnum.app.wisher.wishkey.entity.WishKey
import com.vangelnum.app.wisher.user.repository.UserRepository
import com.vangelnum.app.wisher.wishkey.repository.WishKeyRepository
import com.vangelnum.app.wisher.wishkey.service.WishKeyService
import org.springframework.stereotype.Service
import java.util.*

@Service
class WishKeyServiceImpl(
    private val userRepository: UserRepository,
    private val wishKeyRepository: WishKeyRepository
) : WishKeyService {

    private fun getUserByEmail(email: String) =
        userRepository.findByEmail(email).orElseThrow { NoSuchElementException("User not found") }

    override fun generateWishKey(email: String): WishKey {
        val user = getUserByEmail(email)
        wishKeyRepository.findByUser(user).ifPresent {
            throw Error("Wish key already exists for user: $email")
        }
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
            .orElseThrow { NoSuchElementException("Wish key not found for the current user") }

        val newKey = UUID.randomUUID().toString().take(15)
        val updatedWishKey = existingWishKey.copy(key = newKey)
        return wishKeyRepository.save(updatedWishKey)
    }
}