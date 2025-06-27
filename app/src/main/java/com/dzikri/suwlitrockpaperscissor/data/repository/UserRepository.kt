package com.dzikri.suwlitrockpaperscissor.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
    private val dataStore: DataStore<Preferences>
) {
    suspend fun login(body: Map<String, String>): Response<LoginResponse> {
        return apiService.login(body)
    }

    suspend fun register(body: Map<String, String>): Response<RegisterResponse> {
        return apiService.register(body)
    }

    private companion object {
        val TOKEN = stringPreferencesKey("token")
        val EMAIL = stringPreferencesKey("email")
        val USERNAME = stringPreferencesKey("username")
        val USERID = stringPreferencesKey("userid")
    }

    suspend fun saveUserSession(token: String, email: String, username: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[EMAIL] = email
            preferences[USERNAME] = username
            preferences[USERID] = userId
        }
    }
    val currentToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[TOKEN] ?: "Unknown"
    }

    val currentUsername: Flow<String> = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: "Unknown"
    }
    val currentUserId: Flow<String> = dataStore.data.map { preferences ->
        preferences[USERID] ?: "Unknown"
    }
    val currentEmail: Flow<String> = dataStore.data.map { preferences ->
        preferences[EMAIL] ?: "Unknown"
    }




//    val currentToken: Flow<String> =
//        dataStore.data.map { preferences ->
//            preferences[TOKEN] ?: "Unknown"
//        }
//
//    suspend fun saveToken(token: String) {
//        dataStore.edit { preferences ->
//            preferences[TOKEN] = token
//        }
//    }

}
