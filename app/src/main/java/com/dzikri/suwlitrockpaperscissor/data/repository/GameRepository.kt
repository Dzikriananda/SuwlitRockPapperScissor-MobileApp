package com.dzikri.suwlitrockpaperscissor.data.repository

import android.util.Log
import com.dzikri.suwlitrockpaperscissor.data.network.WebSocketInstance
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GameRepository @Inject constructor(private val webSocketInstance: WebSocketInstance) {

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



    suspend fun joinRoom(roomId: String, userId: String,token: String) {
        client = webSocketInstance.client
        session = client?.connect(webSocketInstance.WsUrl(userId,token))
        session?.sendText(destination = "/app/join-room", body = mapOf<String, String>(
            "roomId" to roomId,
            "userId" to userId
        ).toString()
        )
    }

    suspend fun subscribeToGameStartingStatus(userId: String,token: String): Flow<String> {
        val subscription: Flow<String> = session!!.subscribeText("/user/${userId}/queue/game-status")
        return subscription
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