package com.vangelnum.app.wisher.serviceimpl

import com.vangelnum.app.wisher.entity.ViewLog
import com.vangelnum.app.wisher.entity.Wish
import com.vangelnum.app.wisher.model.WishCreationRequest
import com.vangelnum.app.wisher.model.WishDateResponse
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
        userRepository.findByEmail(email).orElseThrow { Exception("Пользователь не найден") }

    private fun getWishById(id: Long) =
        wishRepository.findById(id).orElseThrow { Exception("Поздравление не найдено") }

    private fun checkWishOwnership(wish: Wish, userEmail: String) {
        val user = getUserByEmail(userEmail)
        if (wish.user.id != user.id) {
            throw AccessDeniedException("Вы не являетесь создателем пожелания")
        }
    }

    @Transactional
    override fun createWish(wishCreationRequest: WishCreationRequest, email: String): Wish {
        val user = getUserByEmail(email)
        if (user.coins < wishCreationRequest.cost) {
            throw Exception("Не хватает монет для создания пожелания")
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
            isBlurred = wishCreationRequest.isBlurred,
            cost = wishCreationRequest.cost
        )
        return wishRepository.save(wish)
    }

    override fun getWishDatesByKey(key: String, viewerEmail: String): List<WishDateResponse> {
        val wishKey = wishKeyRepository.findByKey(key).orElseThrow { Exception("Ключ не найден") }
        val owner = wishKey.user
        return wishRepository.findAll().filter { it.user.id == owner.id }
            .map { WishDateResponse(it.wishDate.toString(), it.openDate.toString()) }
    }

    override fun getWishByKeyAndId(key: String, wishId: Int, viewerEmail: String): Wish {
        val wishKey = wishKeyRepository.findByKey(key).orElseThrow { Exception("Ключ не найден") }
        val owner = wishKey.user
        val viewer = getUserByEmail(viewerEmail)

        val wish = wishRepository.findByUserAndId(owner, wishId.toLong())
            .orElseThrow { Exception("Поздравление не найдено") }

        if (wish.openDate > LocalDate.now()) {
            throw IllegalStateException("Вы сможете посмотреть поздравление начиная с ${wish.openDate}")
        }

        wish.maxViewers?.let { max ->
            if (max > 0) {
                val currentViews = viewLogRepository.countByWishId(wish.id!!)
                if (currentViews >= max && owner.id != viewer.id) {
                    throw AccessDeniedException("Достигнуто ограничение на просмотры")
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
        return wish
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