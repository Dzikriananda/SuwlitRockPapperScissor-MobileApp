package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.BuildConfig
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.model.response.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.ErrorHandler
import com.dzikri.suwlitrockpaperscissor.util.StringHelper
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _isInSignInAttempt = MutableStateFlow(false)
    val isInSignInAttempt = _isInSignInAttempt.asStateFlow()

    private val _loginWithGoogleResponse = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Started)
    val loginWithGoogleResponse: StateFlow<ResultOf<LoginResponse>> = _loginWithGoogleResponse.asStateFlow()





    fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _loginWithGoogleResponse.value = ResultOf.Loading
            _isInSignInAttempt.value = true
            try{
                val credentialManager = CredentialManager.create(context)
                val googleIdOptions = GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.FIREBASE_AUTH_WEBCLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
                val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOptions).build()
                try {
                    val googleResult = credentialManager.getCredential(context, request)
                    val googleIdTokenCredential = googleResult.credential as GoogleIdTokenCredential
                    val idToken = googleIdTokenCredential.idToken
                    val body = mapOf(
                        "googleIdToken" to idToken
                    )
                    val result = repository.loginWithGoogle(body)

                    if (result.code() == 200) {
                        val token = result.body()!!.data!!.token
                        val email = result.body()!!.data!!.email
                        val userId = result.body()!!.data!!.userId
                        val userName = result.body()!!.data!!.username
                        repository.saveUserSession(token,email,userName,userId)
                        _loginWithGoogleResponse.value = ResultOf.Success(result.body()!!)
                    } else {
                        val errorBody = ErrorHandler.handleLoginError(result)
                        if (result.code() == 404) {
                            _loginWithGoogleResponse.value = ResultOf.Failure(errorBody.message, null)
                        } else if (result.code() == 400) {
                            _loginWithGoogleResponse.value = ResultOf.Failure(errorBody.message, null)
                        } else {
                            _loginWithGoogleResponse.value = ResultOf.Failure("Sign in failed, Try again later", null)
                        }
                    }
//                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                } catch (e: Exception){
                    if(e is androidx.credentials.exceptions.GetCredentialCancellationException) {
                        _loginWithGoogleResponse.value = ResultOf.Failure("Sign in cancelled", null)
                    } else {
                        _loginWithGoogleResponse.value = ResultOf.Failure("Sign in failed, Try again later", null)
                    }
                }
            } catch (e: Exception){
                _loginWithGoogleResponse.value = ResultOf.Failure("Sign in failed, Try again later", null)
                Log.d("loginwithgoogle error",e.message.toString())
            }
            _isInSignInAttempt.value = false
        }
    }

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
        _loginWithGoogleResponse.value = ResultOf.Started
    }

    fun login() {
        validateField()
        if(!_usernameOrEmailInput.value.isError && !_passwordInput.value.isError){
            viewModelScope.launch {
                val email = _usernameOrEmailInput.value.text
                val password = _passwordInput.value.text
                _loginResponse.value = ResultOf.Loading
                _isInSignInAttempt.value = true
                val body = mapOf(
                    "email" to email,
                    "password" to password
                )
                try {
                    val result = repository.login(body)
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
                _isInSignInAttempt.value = false
            }
        }
    }
}

