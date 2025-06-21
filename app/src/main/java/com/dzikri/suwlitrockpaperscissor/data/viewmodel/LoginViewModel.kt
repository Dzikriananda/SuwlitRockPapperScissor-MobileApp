package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _usernameOrEmailInput = MutableStateFlow(InputFieldState())
    val usernameOrEmailInput: StateFlow<InputFieldState> = _usernameOrEmailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow(InputFieldState())
    val passwordInput: StateFlow<InputFieldState> = _passwordInput.asStateFlow()

    private val _loginResponse = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Started)
    val loginResponse: StateFlow<ResultOf<LoginResponse>> = _loginResponse.asStateFlow()

    fun onUsernameOrEmailChange(newValue: String) {
        val isValid = newValue.contains("@")
        _usernameOrEmailInput.value = InputFieldState(
            text = newValue,
            isError = !isValid,
            errorMessage = if (!isValid) "Invalid email format" else ""
        )
    }

    fun onPasswordChange(newValue: String) {
        val isValid = newValue.length >= 6
        _passwordInput.value = InputFieldState(
            text = newValue,
            isError = !isValid,
            errorMessage = if (!isValid) "Password must be at least 6 characters" else ""
        )
    }

    fun dismissAlertDialog() {
        _loginResponse.value = ResultOf.Started
    }

    fun login() {
        viewModelScope.launch {
            val email = _usernameOrEmailInput.value.text
            val password = _passwordInput.value.text

            // Optional: pre-check before making request
            if (_usernameOrEmailInput.value.isError || _passwordInput.value.isError) {
                return@launch
            }

            _loginResponse.value = ResultOf.Loading

            val body = mapOf(
                "email" to email,
                "password" to password
            )

            try {
                val result = repository.login(body)
                if (result.code() == 201) {
                    _loginResponse.value = ResultOf.Success(result.body()!!)
                } else {
                    _loginResponse.value = ResultOf.Failure(result.message(), null)
                }
            } catch (e: Exception) {
                _loginResponse.value = ResultOf.Failure(e.message, e)
            }
        }
    }

    fun simulateErrorLogin() {
        viewModelScope.launch {
            _loginResponse.value = ResultOf.Loading
            delay(4000)
            _loginResponse.value = ResultOf.Failure("Something went wrong", null)
        }
    }
}

