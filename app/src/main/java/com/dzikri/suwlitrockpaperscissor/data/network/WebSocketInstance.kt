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
    val url = "ws://43.173.1.87:8081/ws-game"
    val client: StompClient = StompClient(OkHttpWebSocketClient())


//    @OptIn(DelicateCoroutinesApi::class)
//    fun connect() {
//        GlobalScope.launch {
//            val client = StompClient(OkHttpWebSocketClient())
//            val session: StompSession = client.connect(url) // optional login/passcode can be provided here
//
//            // Send text messages using this convenience function
//            session.sendText(destination = "/app/join-room", body = mapOf<String, String>(
//                "roomId" to "sasa",
//                "userId" to "12345"
//            ).toString()
//            )
//
//            val subscription: Flow<String> = session.subscribeText("/topic/game-starting-status")
//            val collectorJob = launch {
//                subscription.collect { msg ->
//                    println("Received: $msg")
//                }
//            }
//            delay(100000)
//            collectorJob.cancel()
//            session.disconnect()
//        }
//    }


}