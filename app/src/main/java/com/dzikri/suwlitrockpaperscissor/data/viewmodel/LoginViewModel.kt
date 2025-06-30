package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.model.LoginData
import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.model.UserPreferences
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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

    fun resetLoginState() {
        _usernameOrEmailInput.value = InputFieldState()
        _passwordInput.value = InputFieldState()
        _loginResponse.value = ResultOf.Started
    }

    fun onUsernameOrEmailChange(newValue: String) {
        _usernameOrEmailInput.value = StringHelper.validateEmail(newValue)
    }

    fun onPasswordChange(newValue: String) {
        _passwordInput.value = StringHelper.validatePassword(newValue)
    }

    fun validateField() {
        val currentEmailField = _usernameOrEmailInput.value.text
        _usernameOrEmailInput.value = StringHelper.validateEmail(currentEmailField)
        val currentPasswordField = _passwordInput.value.text
        _passwordInput.value = StringHelper.validatePassword(currentPasswordField)
    }

    fun dismissAlertDialog() {
        _loginResponse.value = ResultOf.Started
    }

    fun login() {
        validateField()
        if(!_usernameOrEmailInput.value.isError && !_passwordInput.value.isError){
            viewModelScope.launch {
                val email = _usernameOrEmailInput.value.text
                val password = _passwordInput.value.text
                _loginResponse.value = ResultOf.Loading
                val body = mapOf(
                    "email" to email,
                    "password" to password
                )
                try {
                    val result = repository.login(body)
                    Log.d("login in LoginViewModel", "body: ${result.body()}")
                    if (result.code() == 200) {
                        val token = result.body()!!.data!!.token
                        val email = result.body()!!.data!!.email
                        val userId = result.body()!!.data!!.userId
                        val userName = result.body()!!.data!!.username
                        repository.saveUserSession(token,email,userName,userId)
                        _loginResponse.value = ResultOf.Success(result.body()!!)
                    } else {
                        val errorBody = ErrorHandler.handleLoginError(result)
                        Log.d("error at login in LoginViewModel", "body: ${errorBody}")
                        if (result.code() == 404) {
                            _loginResponse.value = ResultOf.Failure(errorBody.message, null)
                        } else if (result.code() == 400) {
                            _loginResponse.value = ResultOf.Failure(errorBody.message, null)
                        } else {
                            _loginResponse.value = ResultOf.Failure("Sign in failed, Try again later", null)
                        }
                    }
                } catch (e: Exception) {
                    _loginResponse.value = ErrorHandler.handleAuthEndpointHitError(e)
                }
            }
        }
    }
}

