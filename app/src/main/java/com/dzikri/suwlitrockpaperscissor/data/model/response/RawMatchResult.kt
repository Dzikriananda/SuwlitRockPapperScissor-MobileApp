package com.dzikri.suwlitrockpaperscissor.data.model.response

data class RawMatchResult(
    val player1Id: String,
    val player2Id: String,
    val winnerId: String,
    val scorePlayer1: Int,
    val scorePlayer2: Int,
    val roundsPlayer1: Int,
    val roundsPlayer2: Int,
    val player1Username: String,
    val player2Username: String
)