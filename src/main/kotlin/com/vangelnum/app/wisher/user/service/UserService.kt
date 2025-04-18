package com.vangelnum.app.wisher.user.service

import com.vangelnum.app.wisher.user.entity.User
import com.vangelnum.app.wisher.user.model.AdRewardResponse
import com.vangelnum.app.wisher.user.model.DailyBonusStateResponse
import com.vangelnum.app.wisher.user.model.DailyLoginBonusResponse
import com.vangelnum.app.wisher.user.model.RegistrationRequest
import com.vangelnum.app.wisher.user.model.UpdateProfileRequest

interface UserService {
    fun registerUser(registrationRequest: RegistrationRequest): String
    fun updateUserAvatar(avatar: String): User
    fun getUserByEmail(email: String): User
    fun getAllUsers(): List<User>
    fun getUserById(id: Long): User
    fun updateUser(updateProfileRequest: UpdateProfileRequest): User
    fun deleteUser(id: Long)
    fun verifyEmail(email: String, verificationCode: String): User
    fun resendVerificationCode(email: String)
    fun claimDailyLoginBonus(email: String): DailyLoginBonusResponse
    fun getDailyBonusState(email: String): DailyBonusStateResponse
    fun claimAdReward(email: String): AdRewardResponse
    fun getAdCooldownTime(email: String): Long
}