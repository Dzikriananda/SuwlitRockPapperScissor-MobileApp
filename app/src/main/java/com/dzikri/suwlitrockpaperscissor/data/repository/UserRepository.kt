package com.dzikri.suwlitrockpaperscissor.data.repository

import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.network.UserApiInterface
import retrofit2.Response
import javax.inject.Inject;

class UserRepository @Inject constructor(
    private val apiService: UserApiInterface
) {
    suspend fun login(body: Map<String, String>): Response<LoginResponse> {
        return apiService.login(body)
    }

}
