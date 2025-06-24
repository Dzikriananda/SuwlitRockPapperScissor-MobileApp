package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import androidx.lifecycle.ViewModel
import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import com.dzikri.suwlitrockpaperscissor.util.StringHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _roomIdInput = MutableStateFlow(InputFieldState())
    val roomIdInput: StateFlow<InputFieldState> = _roomIdInput.asStateFlow()

    fun onRoomIdInputChange(newValue: String) {
        if(newValue.length <= 6) {
            _roomIdInput.value = InputFieldState(text = newValue)
        }
    }

}