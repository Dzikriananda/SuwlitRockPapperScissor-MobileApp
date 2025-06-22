package com.dzikri.suwlitrockpaperscissor.util

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
}