package com.dzikri.suwlitrockpaperscissor.data.network

import com.dzikri.suwlitrockpaperscissor.data.model.response.IsRoomExistResponse
import com.dzikri.suwlitrockpaperscissor.data.model.response.MatchHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiInterface {

    @GET("/api/game/is-room-exist")
    suspend fun fetchRoomExistStatus(
        @Query("roomId") roomId: String
    ): Response<IsRoomExistResponse>

    @GET("/api/game/match-history")
    suspend fun fetchMatchHistory(
        @Query("userId") userId: String
    ): Response<MatchHistoryResponse>
}