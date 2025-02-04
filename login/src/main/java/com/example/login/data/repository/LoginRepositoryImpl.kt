package com.example.login.data.repository

import com.example.login.domain.repository.LoginRepository
import kotlinx.coroutines.delay

class LoginRepositoryImpl: LoginRepository {

    override suspend fun login(username: String, password: String): Result<Unit> {
        // Simulate a network delay
        delay(500)
       return when{
           username  != "admin" -> Result.failure(Exception("username is incorrect"))
           password != "pass" -> Result.failure(Exception("password is incorrect"))
           else -> Result.success(Unit)
       }
    }

}