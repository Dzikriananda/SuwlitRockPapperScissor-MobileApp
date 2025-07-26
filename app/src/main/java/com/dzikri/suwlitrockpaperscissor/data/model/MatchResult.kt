package com.dzikri.suwlitrockpaperscissor.data.model

import com.dzikri.suwlitrockpaperscissor.data.enums.MatchResultEnum
import com.dzikri.suwlitrockpaperscissor.data.model.response.RawMatchResult

class MatchResult(val rawMatchResult: RawMatchResult,val myId: String) {
    var myUserName: String?
    var myRoundScore: Int?
    var myPfPUrl: String? = null
    var enemyUserName: String?
    var enemyRoundScore: Int?
    var enemyPfpUrl: String? = null
    var matchResult: MatchResultEnum? = null

    init {
        if (myId == rawMatchResult.player1Id) {
            myUserName = "You"
            myRoundScore = rawMatchResult.roundsPlayer1
            enemyUserName = rawMatchResult.player2Username
            enemyRoundScore = rawMatchResult.roundsPlayer2
            matchResult= checkWinner(myRoundScore!!,enemyRoundScore!!)
        } else {
            myUserName = "You"
            myRoundScore = rawMatchResult.roundsPlayer2
            enemyUserName = rawMatchResult.player1Username
            enemyRoundScore = rawMatchResult.roundsPlayer1
            matchResult= checkWinner(myRoundScore!!,enemyRoundScore!!)
        }
    }

    fun checkWinner(myRoundScore: Int,enemyRoundScore: Int): MatchResultEnum {
        if(myRoundScore > enemyRoundScore){
            return MatchResultEnum.Victory
        } else if(myRoundScore < enemyRoundScore){
            return MatchResultEnum.Lose
        } else {
            return MatchResultEnum.Draw
        }

    }



}