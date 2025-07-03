package com.dzikri.suwlitrockpaperscissor.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val EMAIL = stringPreferencesKey("email")
        private val USERNAME = stringPreferencesKey("username")
        private val USERID = stringPreferencesKey("userid")
    }

    suspend fun saveUserSession(token: String, email: String, username: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[EMAIL] = email
            preferences[USERNAME] = username
            preferences[USERID] = userId
        }
    }

    suspend fun deleteUserSession() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN)
            preferences.remove(EMAIL)
            preferences.remove(USERNAME)
            preferences.remove(USERID)
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
}
