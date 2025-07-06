package com.dzikri.suwlitrockpaperscissor.data.model

import com.dzikri.suwlitrockpaperscissor.data.enums.Move

class GameState(
    var myUsername: String = "",
    var myMove: Move? = null,
    var myScore: Int = 0,
    var myRoundScore: Int = 0,
    var enemyUsername: String = "",
    var enemyMove: Move? = null,
    var enemyScore: Int = 0,
    var enemyRoundScore: Int = 0,
)
