package com.example.dap.data.repository

import com.example.dap.data.local.dao.UserDao
import com.example.dap.data.local.entity.User

class AuthRepository(private val userDao: UserDao) {
    suspend fun signup(user: User): Result<Unit> {
        return try {
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                Result.failure(Exception("Email already registered"))
            } else {
                userDao.insertUser(user)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = userDao.login(email, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
