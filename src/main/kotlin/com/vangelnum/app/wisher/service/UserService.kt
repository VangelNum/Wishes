package com.vangelnum.app.wisher.service

import com.vangelnum.app.wisher.entity.User
import com.vangelnum.app.wisher.model.RegistrationRequest
import com.vangelnum.app.wisher.model.UpdateProfileRequest

interface UserService {
    fun registerUser(registrationRequest: RegistrationRequest): User
    fun getUserByEmail(email: String): User?
    fun getAllUsers(): List<User>
    fun getUserById(id: Long): User?
    fun updateUser(updateProfileRequest: UpdateProfileRequest): User?
    fun deleteUser(id: Long)
}