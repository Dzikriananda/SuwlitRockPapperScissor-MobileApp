package com.dzikri.suwlitrockpaperscissor.data.model

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginData?,
)

data class LoginData(
    val token: String,
)