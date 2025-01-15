package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.model.WishCreationRequest
import com.vangelnum.app.wisher.repository.UserRepository
import com.vangelnum.app.wisher.repository.ViewLogRepository
import com.vangelnum.app.wisher.repository.WishKeyRepository
import com.vangelnum.app.wisher.repository.WishRepository
import com.vangelnum.app.wisher.service.ViewLogService
import com.vangelnum.app.wisher.service.WishService
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WishServiceImpl(
    private val wishRepository: WishRepository,
    private val userRepository: UserRepository,
    private val wishKeyRepository: WishKeyRepository,
    private val viewLogService: ViewLogService,
    private val viewLogRepository: ViewLogRepository
) : WishService {

    private fun getUserByEmail(email: String) =
        userRepository.findByEmail(email).orElseThrow { Exception("User not found") }

    private fun getWishById(id: Long) =
        wishRepository.findById(id).orElseThrow { Exception("Wish not found") }

    private fun checkWishOwnership(wish: Wish, userEmail: String) {
        val user = getUserByEmail(userEmail)
        if (wish.user.id != user.id) {
            throw AccessDeniedException("You are not the owner of this wish.")
        }
    }

    @Transactional
    override fun createWish(wishCreationRequest: WishCreationRequest, email: String): Wish {
        val user = getUserByEmail(email)
        if (user.coins < wishCreationRequest.cost) {
            throw Exception("Not enough coins to create this wish.")
        }
        user.coins -= wishCreationRequest.cost
        userRepository.save(user)
        val wish = Wish(
            text = wishCreationRequest.text,
            user = user,
            wishDate = LocalDate.parse(wishCreationRequest.wishDate),
            image = wishCreationRequest.image,
            openDate = LocalDate.parse(wishCreationRequest.openDate),
            maxViewers = wishCreationRequest.maxViewers,
            isBlurred = wishCreationRequest.isBlurred ?: false,
            cost = wishCreationRequest.cost
        )
        return wishRepository.save(wish)
    }

    @Transactional
    override fun getWishesByKey(key: String, viewerEmail: String): List<Wish> {
        val wishKey = wishKeyRepository.findByKey(key).orElseThrow { Exception("Key not found") }
        val owner = wishKey.user
        val viewer = getUserByEmail(viewerEmail)
        val wishes = wishRepository.findAll().filter { it.user.id == owner.id && it.openDate <= LocalDate.now() }

        wishes.forEach { wish ->
            wish.maxViewers?.let { max ->
                if (max > 0) {
                    val currentViews = viewLogRepository.countByWishId(wish.id!!)
                    if (currentViews >= max && owner.id != viewer.id) {
                        throw AccessDeniedException("The wish has reached the maximum number of views.")
                    }
                    if (owner.id != viewer.id) {
                        viewLogService.createViewLog(viewer, owner, wish)
                    }
                } else {
                    if (owner.id != viewer.id) {
                        viewLogService.createViewLog(viewer, owner, wish)
                    }
                }
            } ?: run {
                if (owner.id != viewer.id) {
                    viewLogService.createViewLog(viewer, owner, wish)
                }
            }
        }

        return wishes
    }

    override fun getViewLogsForWish(wishId: Long, userEmail: String): List<ViewLog> {
        val wish = getWishById(wishId)
        checkWishOwnership(wish, userEmail)
        return viewLogRepository.findByWishId(wishId)
    }

    override fun deleteWish(id: Long, userEmail: String) {
        val wish = getWishById(id)
        checkWishOwnership(wish, userEmail)
        wishRepository.delete(wish)
    }

    override fun getUserWishes(email: String): List<Wish> {
        val user = getUserByEmail(email)
        return wishRepository.findByUser(user)
    }
}