package com.dzikri.suwlitrockpaperscissor.data.repository

import android.util.Log
import com.dzikri.suwlitrockpaperscissor.data.enums.Move
import com.dzikri.suwlitrockpaperscissor.data.model.RankData
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.model.response.IsRoomExistResponse
import com.dzikri.suwlitrockpaperscissor.data.model.response.MatchHistoryResponse
import com.dzikri.suwlitrockpaperscissor.data.model.response.ResponseWrapper
import com.dzikri.suwlitrockpaperscissor.data.network.GameApiInterface
import com.dzikri.suwlitrockpaperscissor.data.network.WebSocketInstance
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GameRepository @Inject constructor(
    private val webSocketInstance: WebSocketInstance,
    private val gameApiClient: GameApiInterface
) {

    var client: StompClient? = null
    var session: StompSession? = null

    suspend fun setupWsConnection(userId: String,token: String) {
        Log.d("wsconnect","Setup ws connection")
        client = webSocketInstance.client
        Log.d("wsconnect","client instantianted")
        session = client?.connect(webSocketInstance.WsUrl(userId,token))
        Log.d("wsconnect","session instantianted")
    }


    suspend fun createRoom(userId: String) {
        Log.d("game repo","creating room")
        val payload = mapOf<String, String>("userId" to userId)
        val body = Gson().toJson(payload).toString()
        session?.sendText(destination = "/app/join-room", body = body).toString()
    }

    suspend fun joinRoom(userId: String,roomId: String) {
        Log.d("game repo","join room")
        val payload = mapOf<String, String>("userId" to userId,"roomId" to roomId)
        val body = Gson().toJson(payload).toString()
        session?.sendText(destination = "/app/join-room", body = body).toString()
    }

    suspend fun subscribeToGameStartingStatus(userId: String,token: String): Flow<String> {
        Log.d("log","subscribe to game starting status with userID ${userId}")
        val subscription: Flow<String> = session!!.subscribeText("/user/${userId}/queue/game-status")
        return subscription
    }

    suspend fun checkIfRoomExist(roomId: String): Response<IsRoomExistResponse> {
        return gameApiClient.fetchRoomExistStatus(roomId)
    }

    suspend fun sendMove(userId: String, roomId: String, move: Move) {
        val payload = mapOf<String, String>("userId" to userId,"roomId" to roomId,"move" to move.toString())
        val body = Gson().toJson(payload).toString()
        session?.sendText(destination = "/app/room",body = body)
    }

    suspend fun subscribeToGameState(userId: String): Flow<String> {
        val subscription: Flow<String> = session!!.subscribeText("/user/${userId}/room/game-status")
        return subscription
    }

    suspend fun fetchMatchHistory(userId: String): ResultOf<MatchHistoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                val res = gameApiClient.fetchMatchHistory(userId)
                ResultOf.Success<MatchHistoryResponse>(res.body()!!)
            } catch (e: Exception) {
                ResultOf.Failure(e.message, e)
            }
        }

    suspend fun fetchTop100Players(): ResultOf<ResponseWrapper<List<RankData>>> =
        withContext(Dispatchers.IO) {
            try {
                val res = gameApiClient.fetchTop100Players()
                ResultOf.Success<ResponseWrapper<List<RankData>>>(res.body()!!)
            } catch (e: Exception) {
                ResultOf.Failure(e.message, e)
            }
        }

    suspend fun fetchUserRankPosition(): ResultOf<ResponseWrapper<RankData>> =
        withContext(Dispatchers.IO) {
            try {
                val res = gameApiClient.fetchUserRankPosition()
                ResultOf.Success<ResponseWrapper<RankData>>(res.body()!!)
            } catch (e: Exception) {
                ResultOf.Failure(e.message, e)
            }
        }



//    suspend fun simulateErrorCreateRoom(userId: String,token: String) {
//        client = webSocketInstance.client
//        session = client.connect(webSocketInstance.WsUrl(userId,token))
//        val payload = mapOf<String, String>("userId" to userId)
//        val body = Gson().toJson(payload).toString()
//        session.sendText(destination = "/app/join-room", body = body).toString()
//
//    }



}