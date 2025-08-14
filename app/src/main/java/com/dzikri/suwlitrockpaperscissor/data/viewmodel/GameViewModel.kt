package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.enums.Move
import com.dzikri.suwlitrockpaperscissor.data.enums.RoundStatus
import com.dzikri.suwlitrockpaperscissor.data.model.GameStartingStatus
import com.dzikri.suwlitrockpaperscissor.data.model.GameState
import com.dzikri.suwlitrockpaperscissor.data.model.response.GameStateResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.repository.GameRepository
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.ErrorHandler
import com.dzikri.suwlitrockpaperscissor.util.StringHelper
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
import okhttp3.internal.cacheGet
import javax.inject.Inject
import kotlin.random.Random


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

    private var _roomId = ""

    private var _gameStartTimerCount: MutableStateFlow<Int> = MutableStateFlow(5)
    val gameStartTimerCount: StateFlow<Int> = _gameStartTimerCount.asStateFlow()

    private var _roundTimerCount: MutableStateFlow<Int> = MutableStateFlow(5)
    val roundTimerCount: StateFlow<Int> = _roundTimerCount.asStateFlow()

    private var _gameStateResponse: MutableStateFlow<GameStateResponse> = MutableStateFlow(GameStateResponse())

    private var _gameState: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var _isBetweenRoundStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isBetweenRoundStatus: StateFlow<Boolean> = _isBetweenRoundStatus.asStateFlow()

    private var _isAnimationShowing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAnimationShowing: StateFlow<Boolean> = _isAnimationShowing.asStateFlow()



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

    fun setIsAnimationShowing(isShowing: Boolean) {
        _isAnimationShowing.value = isShowing
        Log.d("status animasi"," adalah : ${_isAnimationShowing.value}")
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
            Log.d("log","Collection Starting")
            collectionStarted.complete(Unit) // Notify collection has started

            flow.collect { msg ->
                Log.d("WsListener", "Received: $msg")
                val gameStartMessage = gson.fromJson(msg, GameStartingStatus::class.java)
                Log.d("result", gameStartMessage.toString())
                _gameInitStatus.value = ResultOf.Success(gameStartMessage)
                _roomId = gameStartMessage.roomId
            }
        }

        collectionStarted.await() // Wait until collection start
    }

    private suspend fun subscribeToGameStatus() {
        val flow = gameRepository.subscribeToGameState(userID)
        val gson = Gson()

        val collectionStarted = CompletableDeferred<Unit>()

        gameInitJob = viewModelScope.launch {
            collectionStarted.complete(Unit) // Notify collection has started

            flow.collect { msg ->
                Log.d("WsListener", "Received: $msg")
                val gameStateMessage = gson.fromJson(msg, GameStateResponse::class.java)
                Log.d("result", gameStateMessage.toString())
                setGameState(gameStateMessage)
            }
        }
        collectionStarted.await() // Wait until collection start
    }

    fun setGameState(gameStateResponse: GameStateResponse) {

        var playerScore: Int? = null
        var enemyScore: Int? = null
        var playerRoundsScore: Int? = null
        var enemyRoundsScore: Int? = null
        lateinit var enemyId: String
        gameStateResponse.playersScore.forEach {
            entry ->
            Log.d("setgametstate", "id: ${entry.key}")
            if(entry.key == userID) {
                    playerScore = entry.value
            } else {
                    enemyScore = entry.value
                    enemyId = entry.key
                    Log.d("setgametstate", "enemyId: $enemyId and ${entry.key}")
                }

        }
        gameStateResponse.playersRoundScore.forEach {
            entry ->
            if(entry.key == userID) {
                playerRoundsScore = entry.value
            } else {
                enemyRoundsScore = entry.value
            }
        }
        Log.d("setgametstate", gameStateResponse.playerLastMove.toString())
        val enemyMove = StringHelper.parseMove(gameStateResponse.playerLastMove.get(enemyId))
        val myMove = StringHelper.parseMove(gameStateResponse.playerLastMove.get(userID))
        Log.d("setgametstate my move", myMove.toString())
        Log.d("setgametstate enemy move", enemyMove.toString())

        val updatedState = _gameState.value.copy(
            enemyMove = enemyMove,
            enemyScore = enemyScore!!,
            enemyRoundScore = enemyRoundsScore!!,
            myScore = playerScore!!,
            myRoundScore = playerRoundsScore!!,
            myMove = myMove,
            totalMoves = gameStateResponse.totalMoves
        )
        _gameState.value = updatedState
    }

    fun setInitStatusToStarted() {
        _gameInitStatus.value = ResultOf.Started
    }

    fun retry() {
        setInitStatusToStarted()
        createRoom()
    }

    fun closeBetweenRoundDialog() {
        _isBetweenRoundStatus.value = false
    }

    suspend fun gameStartCountDown() {
        while (_gameStartTimerCount.value > 0) {
            delay(1000)
            _gameStartTimerCount.value = _gameStartTimerCount.value - 1
        }
        _gameInitStatus.value = ResultOf.Started
        _gameStartingStatus.value = true
    }


    //bug ini kepanggil2x pada saat yg hampir bersamaan
    //solusinya observe lansung dari play component saja jika terdapat response api
    suspend fun roundCountDown() {
        Log.d("countdown","count down dipanggil pada : ${System.currentTimeMillis()}")
        _roundStatus.value = RoundStatus.Started
        while(_roundTimerCount.value > 0) {
            delay(1000)
            _roundTimerCount.value = _roundTimerCount.value - 1
        }
        sendMove()
        _roundStatus.value = RoundStatus.Ended
        _roundTimerCount.value = 5
    }

    fun startGame() {
        viewModelScope.launch {
            subscribeToGameStatus()
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

    fun setMove(move: Move) {
        _gameState.value.myMove = move
    }

    fun sendMove() {
        viewModelScope.launch {
            if(_gameState.value.myMove == null){
                randomizeMove()
            }
            gameRepository.sendMove(userID,_roomId,_gameState.value.myMove!!)
        }
    }

    fun randomizeMove() {
        val list = listOf(1,2,3)
        val randomNum = list.random()
        lateinit var randomMove: Move
        when(randomNum){
            1 -> {
                randomMove = Move.Rock
            }
            2 -> {
                randomMove = Move.Paper
            }
            3 -> {
                randomMove = Move.Scissors
            }
        }
        _gameState.value.myMove = randomMove
    }









}