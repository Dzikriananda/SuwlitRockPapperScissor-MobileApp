package com.dzikri.suwlitrockpaperscissor.data.model.response

data class ResponseWrapper<T>(
    val status: String,
    val message: String,
    val data: T
)