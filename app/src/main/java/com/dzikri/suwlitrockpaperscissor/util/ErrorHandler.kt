package com.dzikri.suwlitrockpaperscissor.util

import android.util.Log
import com.dzikri.suwlitrockpaperscissor.data.model.LoginResponse
import com.dzikri.suwlitrockpaperscissor.data.model.RegisterResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.google.gson.Gson
import org.hildan.krossbow.websocket.WebSocketConnectionException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {

    //Must do this because if result code not 200, retrofit treat it as failure/exception
    fun handleLoginError(result: Response<*>): LoginResponse {
        val gson = Gson()
        val errorBody = result.errorBody()?.string()
        val loginErrorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
        return loginErrorResponse
    }

    //Must do this because if result code not 200, retrofit treat it as failure/exception
    fun handleRegisterError(result: Response<*>): RegisterResponse {
        val gson = Gson()
        val errorBody = result.errorBody()?.string()
        val registerErrorResponse = gson.fromJson(errorBody, RegisterResponse::class.java)
        return registerErrorResponse
    }

    fun handleAuthEndpointHitError(e: Exception): ResultOf.Failure {
        val message = when (e) {
            is SocketTimeoutException -> "Connection timed out. Please try again."
            is UnknownHostException -> "No internet connection."
            is HttpException -> "Server error (${e.code()}). Try again later."
            is IOException -> "Network error. Please check your connection."
            else -> "Unexpected error occurred."
        }

        return  ResultOf.Failure(message, e)
    }

    fun handleWsConnectionError(e: Exception): ResultOf.Failure {
        var message = e.message ?: ""
        when {
            "after 10000ms" in message ->  {
                Log.d("WebSocket", "Error: Network Problem")
                message = "Our Network is Busy, Please Try Again Later"
            }
            "404" in message -> {
                Log.d("WebSocket", "Error: 404 Not Found — likely wrong URL or credentials.")
                message = "Invalid/expired token. Please log in again"
            }
            "403" in message -> {
                Log.d("WebSocket", "Error: 404 Not Found — likely wrong URL or credentials.")
                message = "Invalid/expired token. Please log in again"
            }
            "Couldn't" in message ->  {
                Log.d("WebSocket", "Error: No internet Connection")
                message = "No Internet Connection"
            }
            else -> Log.d("WebSocket", "Unknown error: ${e::class.simpleName} - $message")
        }

        return  ResultOf.Failure(message, e)
    }
}