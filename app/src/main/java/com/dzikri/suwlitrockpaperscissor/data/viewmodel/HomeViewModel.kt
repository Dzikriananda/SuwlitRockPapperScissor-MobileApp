package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.model.RankData
import com.dzikri.suwlitrockpaperscissor.data.model.response.IsRoomExistResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.model.response.ResponseWrapper
import com.dzikri.suwlitrockpaperscissor.data.network.WebSocketInstance
import com.dzikri.suwlitrockpaperscissor.data.repository.GameRepository
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.StringHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val userRepository: UserRepository,
    val gameRepository: GameRepository
) : ViewModel() {
    private val _roomIdInput = MutableStateFlow(InputFieldState())
    val roomIdInput: StateFlow<InputFieldState> = _roomIdInput.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

//    private val _top100Players = MutableStateFlow<ResultOf<ResponseWrapper<List<RankData>>>>(ResultOf.Started)
//
//    private val _userRankPosition = MutableStateFlow<ResultOf<ResponseWrapper<RankData>>>(ResultOf.Started)

    private var _top100Players: ResultOf<ResponseWrapper<List<RankData>>> = ResultOf.Started

    private var _userRankPosition: ResultOf<ResponseWrapper<RankData>> = ResultOf.Started


    private val _isRoomExist: MutableStateFlow<ResultOf<IsRoomExistResponse>> = MutableStateFlow(
        ResultOf.Started)
    val isRoomExist: StateFlow<ResultOf<IsRoomExistResponse>> = _isRoomExist.asStateFlow()

    private val _leaderboardsRanks = MutableStateFlow<ResultOf<List<RankData>>>(ResultOf.Started)
    val leaderboardsRanks: StateFlow<ResultOf<List<RankData>>> = _leaderboardsRanks.asStateFlow()

    init {
        fetchUsername()
        fetchLeaderBoardsData()
    }

    suspend fun fetchUserRankPosition() {
        _userRankPosition = ResultOf.Loading
        val result = gameRepository.fetchUserRankPosition()
        _userRankPosition = result
    }

    suspend fun fetchTop100Players() {
        _userRankPosition = ResultOf.Loading
        val result = gameRepository.fetchTop100Players()
        _top100Players = result
    }

    fun fetchLeaderBoardsData() {
        viewModelScope.launch {
            _leaderboardsRanks.value = ResultOf.Loading
            val userRankJob = launch {
                fetchUserRankPosition()
            }

            val top100PlayersJob = launch {
                fetchTop100Players()
            }
            userRankJob.join()
            top100PlayersJob.join()

            if(_userRankPosition is ResultOf.Success && _top100Players is ResultOf.Success) {
                val userRank = (_userRankPosition as ResultOf.Success<ResponseWrapper<RankData>>).value.data.rank
                val top100PlayersData = ResultOf.Success((_top100Players as ResultOf.Success<ResponseWrapper<List<RankData>>>))
                if( userRank <= 4 && userRank != 0 ) {
                    _leaderboardsRanks.value = ResultOf.Success(top100PlayersData.value.value.data.take(4))
                } else {
                    var tempList = top100PlayersData.value.value.data.take(3).toMutableList()
                    val userRank = (_userRankPosition as ResultOf.Success<ResponseWrapper<RankData>>).value.data
                    tempList.add(userRank)
                    _leaderboardsRanks.value = ResultOf.Success(tempList)
                }
            } else {
                _leaderboardsRanks.value = ResultOf.Failure("An Error Has Occured, Please Try Again",Exception())
            }
        }
    }




    fun onRoomIdInputChange(newValue: String) {
        if(newValue.length <= 6) {
            _roomIdInput.value = StringHelper.validateRoomId(newValue)
        }
    }

    fun fetchUsername() {
        viewModelScope.launch {
            _username.value = userRepository.currentUsername.first()
        }
    }

    suspend fun logOut() {
        userRepository.deleteUserSession()
    }

    fun checkIfRoomExist() {
        val tempValue = _roomIdInput.value.text
        _roomIdInput.value = StringHelper.validateRoomId(tempValue)
        if(!_roomIdInput.value.isError) {
            viewModelScope.launch {
                _isRoomExist.value = ResultOf.Loading
                try{
                    val isRoomExistResponse = gameRepository.checkIfRoomExist(_roomIdInput.value.text)
                    if(isRoomExistResponse.body()!!.exist) {
                        _isRoomExist.value = ResultOf.Success(isRoomExistResponse.body()!!)
                    } else {
                        _isRoomExist.value = ResultOf.Started
                        _roomIdInput.value = InputFieldState(_roomIdInput.value.text,true,"Room Not Found")
                    }
                } catch (e: Exception) {
                    Log.d("Error"," Error while checkIfRoomExist ${e.message}")
                    _isRoomExist.value = ResultOf.Failure("An Error Has Occured, Please Try Again",e)
                    _roomIdInput.value = InputFieldState(_roomIdInput.value.text,true,"An Error Has Occured, Please Try Again")
                }
            }
        }
    }
//
//    fun joinRoom() {
//        _isJoiningRoom.value = true
//    }

    fun resetIsRoomExist() {
        _isRoomExist.value = ResultOf.Started
    }




}