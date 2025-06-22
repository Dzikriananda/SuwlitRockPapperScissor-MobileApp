package com.dzikri.suwlitrockpaperscissor.util

import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.google.gson.Gson
import retrofit2.Response

object ErrorHandler {
    fun handleLoginError(result: Response<*>): LoginResponse {
        val gson = Gson()
        val errorBody = result.errorBody()?.string()
        val loginErrorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
        return loginErrorResponse
    }
}