package com.dzikri.suwlitrockpaperscissor.util

import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.RegisterResponse
import com.google.gson.Gson
import retrofit2.Response

object ErrorHandler {
    fun handleLoginError(result: Response<*>): LoginResponse {
        val gson = Gson()
        val errorBody = result.errorBody()?.string()
        val loginErrorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
        return loginErrorResponse
    }

    fun handleRegisterError(result: Response<*>): RegisterResponse {
        val gson = Gson()
        val errorBody = result.errorBody()?.string()
        val registerErrorResponse = gson.fromJson(errorBody, RegisterResponse::class.java)
        return registerErrorResponse
    }
}