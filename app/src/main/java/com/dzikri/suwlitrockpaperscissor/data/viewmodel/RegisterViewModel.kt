package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.RegisterResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.ErrorHandler
import com.dzikri.suwlitrockpaperscissor.util.StringHelper
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _usernameInput = MutableStateFlow(InputFieldState())
    val usernameInput: StateFlow<InputFieldState> = _usernameInput.asStateFlow()

    private val _emailInput = MutableStateFlow(InputFieldState())
    val emailInput: StateFlow<InputFieldState> = _emailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow(InputFieldState())
    val passwordInput: StateFlow<InputFieldState> = _passwordInput.asStateFlow()

    private val _registerResponse = MutableStateFlow<ResultOf<RegisterResponse>>(ResultOf.Started)
    val registerResponse: StateFlow<ResultOf<RegisterResponse>> = _registerResponse.asStateFlow()

    fun onEmailChange(newValue: String) {
        _emailInput.value = StringHelper.validateEmail(newValue)
    }

    fun onUsernameChange(newValue: String) {
        _usernameInput.value = StringHelper.validateUsername(newValue)
    }

    fun onPasswordChange(newValue: String) {
        _passwordInput.value = StringHelper.validatePassword(newValue)
    }

    fun validateField() {
        val currentEmailField = _emailInput.value.text
        _emailInput.value = StringHelper.validateEmail(currentEmailField)
        val currentPasswordField = _passwordInput.value.text
        _passwordInput.value = StringHelper.validatePassword(currentPasswordField)
        val currentUsernameField = _usernameInput.value.text
        _passwordInput.value = StringHelper.validatePassword(currentUsernameField)
    }


    fun validateFields(): Boolean {
        onUsernameChange(_usernameInput.value.text)
        onEmailChange(_emailInput.value.text)
        onPasswordChange(_passwordInput.value.text)

        return !(_usernameInput.value.isError || _emailInput.value.isError || _passwordInput.value.isError)
    }

    fun register() {
        if (!validateFields()) return

        viewModelScope.launch {
            val body = mapOf(
                "username" to _usernameInput.value.text,
                "email" to _emailInput.value.text,
                "password" to _passwordInput.value.text
            )

            _registerResponse.value = ResultOf.Loading
            try {
                val result = repository.register(body)
                if (result.code() == 201) {
                    _registerResponse.value = ResultOf.Success(result.body()!!)
                } else {
                    val errorBody = ErrorHandler.handleRegisterError(result)
                    _registerResponse.value = ResultOf.Failure(errorBody.message, null)
                }
            } catch (e: Exception) {
                _registerResponse.value = ResultOf.Failure(e.message, e)
            }
        }
    }

    fun dismissAlertDialog() {
        _registerResponse.value = ResultOf.Started
    }
}
