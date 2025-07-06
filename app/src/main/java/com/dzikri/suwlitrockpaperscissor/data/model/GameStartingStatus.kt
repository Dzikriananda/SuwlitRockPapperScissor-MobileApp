package com.dzikri.suwlitrockpaperscissor.data.model

data class GameStartingStatus (
    val gameStart: Boolean,
    val userId: List<String>,
    val playersUsername: Map<String, String>,
    val roomId: String,
)