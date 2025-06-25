package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.StringHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _roomIdInput = MutableStateFlow(InputFieldState())
    val roomIdInput: StateFlow<InputFieldState> = _roomIdInput.asStateFlow()

    private val _isJoiningRoom = MutableStateFlow(false)
    val isJoiningRoom: StateFlow<Boolean> = _isJoiningRoom.asStateFlow()

    fun onRoomIdInputChange(newValue: String) {
        if(newValue.length <= 6) {
            _roomIdInput.value = InputFieldState(text = newValue)
        }
    }

    fun createNewRoom() {
        if (!_isJoiningRoom.value) {
            viewModelScope.launch {
                _isJoiningRoom.value = true
                delay(3000)
                _isJoiningRoom.value = false
            }
        }
    }



    fun joinRoom(roomId: String) {
        _isJoiningRoom.value = true
    }

}