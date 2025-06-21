package com.dzikri.suwlitrockpaperscissor.data.model

sealed class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    object Loading : ResultOf<Nothing>()
    object Started : ResultOf<Nothing>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): ResultOf<Nothing>()
}