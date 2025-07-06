package com.dzikri.suwlitrockpaperscissor.data.model

class GameStateResponse(
    var playersUsername: Map<String, String> = emptyMap(),
    var playersMove: Map<String, String> = emptyMap(),
    var playersScore: Map<String, Int> = emptyMap(),
    var playersRoundScore: Map<String, Int> = emptyMap()
)