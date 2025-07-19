package com.dzikri.suwlitrockpaperscissor.util

import android.util.Log
import com.dzikri.suwlitrockpaperscissor.data.enums.Move
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState

object StringHelper {
    fun validateEmail(value:String): InputFieldState {
        lateinit var inputFieldState: InputFieldState
        if(value.length == 0) {
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Email must be not empty"
            )
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Invalid email format"
            )
        } else {
            inputFieldState = InputFieldState(
                text = value,
                isError = false,
                errorMessage = ""
            )
        }
        return inputFieldState
    }

    fun validatePassword(value:String): InputFieldState {
        lateinit var inputFieldState: InputFieldState
        if(value.length == 0){
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Password must be not empty"
            )
        } else if(value.length < 8) {
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Password must be longer than 8 characters"
            )
        } else {
            inputFieldState = InputFieldState(
                text = value,
                isError = false,
                errorMessage = ""
            )
        }
        return inputFieldState
    }

    fun validateUsername(value:String): InputFieldState {
        lateinit var inputFieldState: InputFieldState
        if(value.length == 0){
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Username must be not empty"
            )
        } else if(value.length < 8) {
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Username must be longer than 8 characters"
            )
        } else {
            inputFieldState = InputFieldState(
                text = value,
                isError = false,
                errorMessage = ""
            )
        }
        return inputFieldState
    }

    fun validateRoomId(value: String): InputFieldState {
        lateinit var inputFieldState: InputFieldState
        if(value.length == 0){
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Room Id must be not empty"
            )
        } else if(value.length < 6) {
            inputFieldState = InputFieldState(
                text = value,
                isError = true,
                errorMessage = "Room Id must be 6 characters"
            )
        } else {
            inputFieldState = InputFieldState(
                text = value,
                isError = false,
                errorMessage = ""
            )
        }
        return inputFieldState
    }

    fun parseMove(moveStr: String?): Move? {
        Log.d("parse move",moveStr.toString())
        return try {
            moveStr?.lowercase()?.replaceFirstChar { it.uppercase() }?.let {
                Move.valueOf(it)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }


}