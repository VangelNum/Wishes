package com.vangelnum.app.wisher.wish.serviceimpl

import com.vangelnum.app.wisher.user.repository.UserRepository
import com.vangelnum.app.wisher.wish.entity.Wish
import com.vangelnum.app.wisher.wish.model.WishCreationRequest
import com.vangelnum.app.wisher.wish.model.WishDateResponse
import com.vangelnum.app.wisher.wish.model.WishResponse
import com.vangelnum.app.wisher.wish.model.WishUpdateRequest
import com.vangelnum.app.wisher.wish.repository.WishRepository
import com.vangelnum.app.wisher.wish.service.WishService
import com.vangelnum.app.wisher.wishkey.repository.WishKeyRepository
import com.vangelnum.app.wisher.wishkeylogs.service.KeyViewLogService
import com.vangelnum.app.wisher.wishlogs.entity.ViewLog
import com.vangelnum.app.wisher.wishlogs.repository.ViewLogRepository
import com.vangelnum.app.wisher.wishlogs.service.ViewLogService
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WishServiceImpl(
    private val wishRepository: WishRepository,
    private val userRepository: UserRepository,
    private val wishKeyRepository: WishKeyRepository,
    private val viewLogService: ViewLogService,
    private val viewLogRepository: ViewLogRepository,
    private val keyViewLogService: KeyViewLogService
) : WishService {

    private fun getUserByEmail(email: String) =
        userRepository.findByEmail(email).orElseThrow { NoSuchElementException("Пользователь не найден") }

    private fun getWishById(id: Long) =
        wishRepository.findById(id).orElseThrow { NoSuchElementException("Поздравление не найдено") }

    private fun checkWishOwnership(wish: Wish, userEmail: String) {
        val user = getUserByEmail(userEmail)
        if (wish.user.id != user.id) {
            throw AccessDeniedException("Вы не являетесь создателем пожелания")
        }
    }

    override fun createWish(wishCreationRequest: WishCreationRequest, email: String): Wish {
        val user = getUserByEmail(email)
        if (user.coins < wishCreationRequest.cost) {
            throw IllegalArgumentException("Не хватает монет для создания пожелания")
        }
        user.coins -= wishCreationRequest.cost
        user.wishesCreatedCount += 1

        userRepository.save(user)
        val wish = Wish(
            text = wishCreationRequest.text,
            user = user,
            wishDate = LocalDate.parse(wishCreationRequest.wishDate),
            image = wishCreationRequest.image,
            openDate = LocalDate.parse(wishCreationRequest.openDate),
            maxViewers = wishCreationRequest.maxViewers,
            isBlurred = wishCreationRequest.isBlurred,
            cost = wishCreationRequest.cost
        )
        return wishRepository.save(wish)
    }

    override fun getWishDatesByKey(key: String, viewerEmail: String): List<WishDateResponse> {
        val wishKey = wishKeyRepository.findByKey(key).orElseThrow { NoSuchElementException("Ключ не найден") }
        val owner = wishKey.user
        val viewer = getUserByEmail(viewerEmail)
        if (owner.id != viewer.id) {
            keyViewLogService.createKeyViewLogForCurrentUser(key, viewerEmail)
        }
        return wishRepository.findAll().filter { it.user.id == owner.id }
            .map { WishDateResponse(it.id!!, it.wishDate.toString(), it.openDate.toString()) }
    }

    override fun getWishByKeyAndId(key: String, wishId: Int, viewerEmail: String): Wish {
        val wishKey = wishKeyRepository.findByKey(key).orElseThrow { NoSuchElementException("Ключ не найден") }
        val owner = wishKey.user
        val viewer = getUserByEmail(viewerEmail)

        val wish = wishRepository.findByUserAndId(owner, wishId.toLong())
            .orElseThrow { NoSuchElementException("Поздравление не найдено") }

        if (wish.openDate > LocalDate.now()) {
            throw IllegalStateException("Вы сможете посмотреть поздравление начиная с ${wish.openDate}")
        }

        wish.maxViewers?.let { max ->
            val currentViews = viewLogRepository.countByWishId(wish.id!!)
            val alreadyViewed = viewLogRepository.existsByWishIdAndViewerId(wish.id!!, viewer.id!!)

            if (max > 0 && currentViews >= max && owner.id != viewer.id && !alreadyViewed) {
                throw AccessDeniedException("Достигнуто ограничение на просмотры")
            }
            if (owner.id != viewer.id) {
                viewLogService.createViewLog(viewer, owner, wish)
            }
        } ?: run {
            if (owner.id != viewer.id) {
                viewLogService.createViewLog(viewer, owner, wish)
            }
        }
        return wish
    }

    override fun getLastWishByKey(key: String, viewerEmail: String): Wish {
        val wishKey = wishKeyRepository.findByKey(key).orElseThrow { NoSuchElementException("Ключ не найден") }
        val owner = wishKey.user
        val viewer = getUserByEmail(viewerEmail)

        val wishes = wishRepository.findByUser(owner)
            .filter { it.openDate <= LocalDate.now() }

        val lastWish = wishes.maxByOrNull { it.id ?: 0 }
            ?: throw NoSuchElementException("У пользователя с данным ключом нет доступных пожеланий")

        lastWish.maxViewers?.let { max ->
            val currentViews = viewLogRepository.countByWishId(lastWish.id!!)
            val alreadyViewed = viewLogRepository.existsByWishIdAndViewerId(lastWish.id!!, viewer.id!!)

            if (max > 0 && currentViews >= max && owner.id != viewer.id && !alreadyViewed) {
                throw AccessDeniedException("Достигнуто ограничение на просмотры")
            }
            if (owner.id != viewer.id) {
                viewLogService.createViewLog(viewer, owner, lastWish)
            }
        } ?: run {
            if (owner.id != viewer.id) {
                viewLogService.createViewLog(viewer, owner, lastWish)
            }
        }
        return lastWish
    }

    override fun getUserWishesCount(email: String): Int {
        val user = getUserByEmail(email)
        return user.wishesCreatedCount
    }

    override fun getViewLogsForWish(wishId: Long, userEmail: String): List<ViewLog> {
        val wish = getWishById(wishId)
        checkWishOwnership(wish, userEmail)
        return viewLogRepository.findByWishId(wishId)
    }

    override fun deleteWish(id: Long, userEmail: String) {
        val wish = getWishById(id)
        checkWishOwnership(wish, userEmail)
        val viewLogs = viewLogRepository.findByWishId(id)
        viewLogRepository.deleteAll(viewLogs)
        wishRepository.delete(wish)
    }

    override fun getUserWishes(email: String): List<WishResponse> {
        val user = getUserByEmail(email)
        return wishRepository.findByUser(user)
            .map {
                WishResponse(
                    id = it.id!!,
                    text = it.text,
                    wishDate = it.wishDate.toString(),
                    image = it.image,
                    openDate = it.openDate.toString(),
                    maxViewers = it.maxViewers,
                    isBlurred = it.isBlurred,
                    cost = it.cost
                )
            }
    }

    override fun updateWish(id: Long, wishUpdateRequest: WishUpdateRequest, email: String): Wish {
        val existingWish = getWishById(id)
        checkWishOwnership(existingWish, email)

        val updatedWish = existingWish.copy(
            text = wishUpdateRequest.text ?: existingWish.text,
            wishDate = wishUpdateRequest.wishDate ?: existingWish.wishDate,
            image = wishUpdateRequest.image ?: existingWish.image,
            openDate = wishUpdateRequest.openDate ?: existingWish.openDate,
            maxViewers = wishUpdateRequest.maxViewers ?: existingWish.maxViewers,
            isBlurred = wishUpdateRequest.isBlurred ?: existingWish.isBlurred,
            cost = wishUpdateRequest.cost ?: existingWish.cost
        )
        return wishRepository.save(updatedWish)
    }
}