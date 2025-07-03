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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(private val gameRepository: GameRepository,private val userRepository: UserRepository): ViewModel(){

    lateinit var userID: String
    lateinit var token: String
    private var gameInitJob: Job? = null
    private var _gameInitStatus: MutableStateFlow<ResultOf<GameStartingStatus>> = MutableStateFlow(ResultOf.Started)
    val gameInitStatus: StateFlow<ResultOf<GameStartingStatus>> = _gameInitStatus.asStateFlow()
    private var _gameStartingStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val gameStartingStatus: StateFlow<Boolean> = _gameStartingStatus.asStateFlow()

    private var _timerCount: MutableStateFlow<Int> = MutableStateFlow(5)
    val timerCount: StateFlow<Int> = _timerCount.asStateFlow()

   fun createRoom() {
        viewModelScope.launch {
            _gameInitStatus.value = ResultOf.Loading
            userID = userRepository.currentUserId.first()
            token = userRepository.currentToken.first()
            try{
                gameRepository.setupWsConnection(userID,token)
                subscribeToGameStartingStatus()
                gameRepository.createRoom(userID)
            } catch (exception: Exception) {
                Log.d("error while connecting ws",exception.toString())
                val errResult = ErrorHandler.handleWsConnectionError(exception)
                _gameInitStatus.value = errResult
                clearJob()
                //TODO SAAT EXCEPTION TIDAK ADA INTERNET MASIH ADA MESSAGE "A resource failed to call close."
            }
        }
    }

    fun joinRoom(roomId: String) {
        viewModelScope.launch {
            _gameInitStatus.value = ResultOf.Loading
            userID = userRepository.currentUserId.first()
            token = userRepository.currentToken.first()
            try{
                gameRepository.setupWsConnection(userID,token)
                subscribeToGameStartingStatus()
                gameRepository.joinRoom(userID,roomId)
            } catch (exception: Exception) {
                Log.d("error while connecting ws",exception.toString())
                val errResult = ErrorHandler.handleWsConnectionError(exception)
                _gameInitStatus.value = errResult
                clearJob()
            }
        }
    }


    /*
        Wajib Suspend (harus await), jangan munculkan launch coroutine baru karena
        subscribe harus sudah berjalan terlebih dahulu sebelum mengirim request
        membuat room agar response dari WS BE bisa ditangkap sesegera mungkin.
    */

    private suspend fun subscribeToGameStartingStatus() {
        val flow = gameRepository.subscribeToGameStartingStatus(userID, token)
        val gson = Gson()

        val collectionStarted = CompletableDeferred<Unit>()

        gameInitJob = viewModelScope.launch {
            collectionStarted.complete(Unit) // Notify collection has started

            flow.collect { msg ->
                Log.d("WsListener", "Received: $msg")
                val gameStartMessage = gson.fromJson(msg, GameStartingStatus::class.java)
                _gameInitStatus.value = ResultOf.Success(gameStartMessage)
            }
        }

        collectionStarted.await() // Wait until collection start
    }

    fun setInitStatusToStarted() {
        _gameInitStatus.value = ResultOf.Started
    }

    fun retry() {
        setInitStatusToStarted()
        createRoom()
    }

    fun gameStartCountDown() {
        viewModelScope.launch {
            while(_timerCount.value > 0) {
                delay(1000)
                _timerCount.value = _timerCount.value - 1
            }
            _gameInitStatus.value = ResultOf.Started
        }
    }

    fun startGame() {
        gameStartCountDown()
        //TODO
    }


    fun clearJob() {
        gameInitJob?.cancel()
        runBlocking {
            try {
                gameRepository.session?.disconnect()
            } catch (e: Exception) {
                Log.e("tag", "Error during disconnect", e)
            }
        }
    }









}