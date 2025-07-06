package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.enums.RoundStatus
import com.dzikri.suwlitrockpaperscissor.data.model.GameStartingStatus
import com.dzikri.suwlitrockpaperscissor.data.model.GameState
import com.dzikri.suwlitrockpaperscissor.data.model.GameStateResponse
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

    private var _roundStatus: MutableStateFlow<RoundStatus> = MutableStateFlow(RoundStatus.Idle)
    val roundStatus: StateFlow<RoundStatus> = _roundStatus.asStateFlow()


    private var _gameStartTimerCount: MutableStateFlow<Int> = MutableStateFlow(5)
    val gameStartTimerCount: StateFlow<Int> = _gameStartTimerCount.asStateFlow()

    private var _roundTimerCount: MutableStateFlow<Int> = MutableStateFlow(5)
    val roundTimerCount: StateFlow<Int> = _roundTimerCount.asStateFlow()

    private var _gameStateResponse: MutableStateFlow<GameStateResponse> = MutableStateFlow(GameStateResponse())

    private var _gameState: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

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
                Log.d("result", gameStartMessage.toString())

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

    suspend fun gameStartCountDown() {
        while (_gameStartTimerCount.value > 0) {
            delay(1000)
            _gameStartTimerCount.value = _gameStartTimerCount.value - 1
        }
        _gameInitStatus.value = ResultOf.Started
        _gameStartingStatus.value = true
    }


    suspend fun roundCountDown() {
        Log.d("tag","round countdown started")
        _roundStatus.value = RoundStatus.Started
        while(_roundTimerCount.value > 0) {
            Log.d("tag",_roundTimerCount.value.toString())
            delay(1000)
            _roundTimerCount.value = _roundTimerCount.value - 1
        }
        _roundStatus.value = RoundStatus.Ended
    }

    fun startGame() {
        viewModelScope.launch {
            initGameState()
            gameStartCountDown()
            roundCountDown()
        }
        //TODO
    }

    suspend fun initGameState() {
        val myId = userRepository.currentUserId.first()
        val allIds = (_gameInitStatus.value as ResultOf.Success).value.playersUsername.keys

        var enemyUserId: String? = null
        for (id in allIds) {
            if (id != myId) {
                enemyUserId = id
                break
            }
        }

        val enemyUsername =  (_gameInitStatus.value as ResultOf.Success).value.playersUsername.getValue(enemyUserId!!)
        val initGameState = GameState(
           myUsername = userRepository.currentUsername.first(),
            myScore = 0,
            myRoundScore = 0,
            myMove = null,
            enemyUsername = enemyUsername,
            enemyMove = null,
            enemyScore = 0,
            enemyRoundScore = 0
        )
        _gameState.value = initGameState
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