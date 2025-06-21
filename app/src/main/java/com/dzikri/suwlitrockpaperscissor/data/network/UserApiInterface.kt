package com.dzikri.suwlitrockpaperscissor.data.network

import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiInterface {
    @POST("api/auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>
}