package com.dzikri.suwlitrockpaperscissor.data.model.response

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginData?,
)

data class LoginData(
    val userId: String,
    val username: String,
    val email: String,
    val token: String,
)