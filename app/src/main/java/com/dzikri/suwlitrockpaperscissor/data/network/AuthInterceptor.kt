package com.dzikri.suwlitrockpaperscissor.data.network

import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userRepository: UserRepository
) : Interceptor {

    private val excludedPaths = listOf(
        "/auth/login",
        "/auth/register"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        val path = request.url.encodedPath

        if (excludedPaths.none { path.endsWith(it) }) {
            runBlocking {
                userRepository.currentToken.first().let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
