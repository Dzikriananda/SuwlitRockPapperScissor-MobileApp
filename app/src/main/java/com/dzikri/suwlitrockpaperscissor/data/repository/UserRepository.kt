package com.dzikri.suwlitrockpaperscissor.data.repository

import com.dzikri.suwlitrockpaperscissor.data.SessionManager
import com.dzikri.suwlitrockpaperscissor.data.model.response.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.response.RegisterResponse
import com.dzikri.suwlitrockpaperscissor.data.network.UserApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response
import javax.inject.Inject;

class UserRepository @Inject constructor(
    private val apiService: UserApiInterface,
    private val sessionManager: SessionManager
) {
    suspend fun login(body: Map<String, String>): Response<LoginResponse> = withContext(Dispatchers.IO) {
        apiService.login(body)
    }

    suspend fun loginWithGoogle(body: Map<String, String>): Response<LoginResponse> = withContext(Dispatchers.IO) {
        apiService.loginWithGoogle(body)
    }

    suspend fun register(body: Map<String, String>): Response<RegisterResponse> = withContext(Dispatchers.IO) {
        apiService.register(body)
    }

    suspend fun saveUserSession(token: String, email: String, username: String, userId: String) {
        sessionManager.saveUserSession(token, email, username, userId)
    }

    suspend fun deleteUserSession() {
        sessionManager.deleteUserSession()
    }

    val currentToken: Flow<String> get() = sessionManager.currentToken
    val currentUsername: Flow<String> get() = sessionManager.currentUsername
    val currentUserId: Flow<String> get() = sessionManager.currentUserId
    val currentEmail: Flow<String> get() = sessionManager.currentEmail




}
