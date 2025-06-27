package com.dzikri.suwlitrockpaperscissor.data.repository

import com.dzikri.suwlitrockpaperscissor.data.network.WebSocketInstance
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

    lateinit var client: StompClient
    lateinit var session: StompSession

    suspend fun createRoom(roomId: String, userId: String) {
        client = webSocketInstance.client
        session = client.connect(webSocketInstance.url)
        session.sendText(destination = "/app/join-room", body = mapOf<String, String>(
                "roomId" to roomId,
                "userId" to userId
            ).toString()
        )
    }

    suspend fun subscribeToGameStartingStatus(): Flow<String> {
        val subscription: Flow<String> = session.subscribeText("/topic/game-starting-status")
        return subscription

    }


}