package com.dzikri.suwlitrockpaperscissor.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.dzikri.suwlitrockpaperscissor.data.enums.MatchResultEnum
import com.dzikri.suwlitrockpaperscissor.data.model.response.RawMatchResult
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class MatchResult(val rawMatchResult: RawMatchResult, val myId: String) {
    var myUserName: String?
    var myRoundScore: Int?
    var myPfPUrl: String? = null
    var enemyUserName: String?
    var enemyRoundScore: Int?
    var enemyPfpUrl: String? = null
    var matchResult: MatchResultEnum? = null
    var matchTime: String? = null
    var myScore: Int? = null
    var enemyScore: Int? = null


    init {
        if (myId == rawMatchResult.player1Id) {
            myUserName = "You"
            myRoundScore = rawMatchResult.roundsPlayer1
            myScore = rawMatchResult.scorePlayer1
            enemyUserName = rawMatchResult.player2Username
            enemyRoundScore = rawMatchResult.roundsPlayer2
            enemyScore = rawMatchResult.scorePlayer2
            matchResult= checkWinner(myRoundScore!!,enemyRoundScore!!)
        } else {
            myUserName = "You"
            myRoundScore = rawMatchResult.roundsPlayer2
            myScore = rawMatchResult.scorePlayer2
            enemyUserName = rawMatchResult.player1Username
            enemyRoundScore = rawMatchResult.roundsPlayer1
            enemyScore = rawMatchResult.scorePlayer1
            matchResult= checkWinner(myRoundScore!!,enemyRoundScore!!)
        }
        val time = ZonedDateTime.parse(rawMatchResult.matchTime).truncatedTo(ChronoUnit.MINUTES)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        matchTime = formatter.format(time)



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