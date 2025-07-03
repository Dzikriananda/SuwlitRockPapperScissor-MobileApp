package com.dzikri.suwlitrockpaperscissor.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dzikri.suwlitrockpaperscissor.data.SessionManager
import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.RegisterResponse
import com.dzikri.suwlitrockpaperscissor.data.network.UserApiInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject;
import javax.inject.Singleton

class UserRepository @Inject constructor(
    private val apiService: UserApiInterface,
    private val sessionManager: SessionManager
) {
    suspend fun login(body: Map<String, String>): Response<LoginResponse> {
        return apiService.login(body)
    }

    suspend fun register(body: Map<String, String>): Response<RegisterResponse> {
        return apiService.register(body)
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
