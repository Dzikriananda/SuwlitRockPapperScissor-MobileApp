package com.dzikri.suwlitrockpaperscissor.data.network

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendEmptyMsg
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.WebSocketClient
import org.hildan.krossbow.websocket.builtin.builtIn
import kotlinx.coroutines.delay
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient

class WebSocketInstance {
    val baseUrl = "ws://43.173.1.87:8081/ws-game"
    val client: StompClient = StompClient(OkHttpWebSocketClient())

    fun WsUrl(userId: String, token: String): String {
        return "$baseUrl?userId=$userId&token=$token"
    }
}