package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.GameStartingStatus
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.repository.GameRepository
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.ErrorHandler
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hildan.krossbow.websocket.WebSocketConnectionException
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(private val gameRepository: GameRepository,private val userRepository: UserRepository): ViewModel(){

    lateinit var userID: String
    lateinit var token: String
    private var gameStartingJob: Job? = null
    private var _gameStartingStatus: MutableStateFlow<ResultOf<GameStartingStatus>> = MutableStateFlow(ResultOf.Started)
    val gameStartingStatus: StateFlow<ResultOf<GameStartingStatus>> = _gameStartingStatus.asStateFlow()



   fun createRoom() {
        viewModelScope.launch {
            _gameStartingStatus.value = ResultOf.Loading
            userID = userRepository.currentUserId.first()
            token = userRepository.currentToken.first()
            try{
                gameRepository.setupWsConnection(userID,token)
                subscribeToGameStartingStatus()
                gameRepository.createRoom(userID)
            } catch (exception: Exception) {
                val errResult = ErrorHandler.handleWsConnectionError(exception)
                _gameStartingStatus.value = errResult

            }
        }
    }

    private suspend fun subscribeToGameStartingStatus() {
        val flow = gameRepository.subscribeToGameStartingStatus(userID, token)
        val gson = Gson()

        val collectionStarted = CompletableDeferred<Unit>()

        gameStartingJob = viewModelScope.launch {
            Log.d("gameviewmodel", "starting gamestartingjob")
            collectionStarted.complete(Unit) // Notify collection has started

            flow.collect { msg ->
                Log.d("WsListener", "Received: $msg")
                val gameStartMessage = gson.fromJson(msg, GameStartingStatus::class.java)
                _gameStartingStatus.value = ResultOf.Success(gameStartMessage)
            }
        }

        collectionStarted.await() // Wait until collection start
    }


    fun clearJob() {
        Log.d("tag", "clearing")
        gameStartingJob?.cancel()
        runBlocking {
            try {
                gameRepository.session.disconnect()
            } catch (e: Exception) {
                Log.e("tag", "Error during disconnect", e)
            }
        }
    }









}