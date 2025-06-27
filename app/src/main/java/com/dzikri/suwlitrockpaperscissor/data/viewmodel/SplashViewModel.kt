package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@HiltViewModel
class SplashViewModel @Inject constructor(val userRepository: UserRepository): ViewModel(){

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            userRepository.currentToken.collect { token ->
                Log.d("SplashViewModel", "Token: $token")
                _isLoggedIn.value = token != "Unknown" && token.isNotBlank()
            }
        }
    }



}