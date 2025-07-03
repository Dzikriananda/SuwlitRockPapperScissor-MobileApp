package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.model.IsRoomExistResponse
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
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


    private val _isJoiningRoom = MutableStateFlow(false)
    val isJoiningRoom : StateFlow<Boolean> = _isJoiningRoom.asStateFlow()

    private val _isRoomExist: MutableStateFlow<ResultOf<IsRoomExistResponse>> = MutableStateFlow(
        ResultOf.Started)
    val isRoomExist: StateFlow<ResultOf<IsRoomExistResponse>> = _isRoomExist.asStateFlow()

    init {
        fetchUsername()
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

    fun joinRoom() {
        _isJoiningRoom.value = true
    }

    fun resetIsRoomExist() {
        _isRoomExist.value = ResultOf.Started
    }




}