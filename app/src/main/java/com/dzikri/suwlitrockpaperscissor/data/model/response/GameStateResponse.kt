package com.dzikri.suwlitrockpaperscissor.data.model.response

class

GameStateResponse(
    val playersUsername: Map<String, String> = emptyMap(),
    var playerLastMove: Map<String, String> = emptyMap(),
    var playersScore: Map<String, Int> = emptyMap(),
    var playersRoundScore: Map<String, Int> = emptyMap(),
    var totalMoves: Int = 0
)