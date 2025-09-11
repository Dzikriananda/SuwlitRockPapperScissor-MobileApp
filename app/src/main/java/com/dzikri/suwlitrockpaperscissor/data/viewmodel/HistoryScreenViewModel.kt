package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.model.MatchResult
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.repository.GameRepository
import com.dzikri.suwlitrockpaperscissor.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    val userRepository: UserRepository,
    val gameRepository: GameRepository
): ViewModel() {

    private var _matchHistory: MutableStateFlow<ResultOf<List<MatchResult>>> = MutableStateFlow(ResultOf.Started)
    val matchHistory: StateFlow<ResultOf<List<MatchResult>>> = _matchHistory.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchMatchHistory() {
        viewModelScope.launch {
            _matchHistory.value = ResultOf.Loading
            val userId = userRepository.currentUserId.first()
            val data = gameRepository.fetchMatchHistory(userId)
            if(data is ResultOf.Success){
                val parsedMatchResult = data.value.map {
                    MatchResult(it, userId)
                }
                _matchHistory.value = ResultOf.Success(parsedMatchResult)
            } else if( data is ResultOf.Failure){
                Log.d("HistoryScreenViewModel", "failed to fetchHistory: ${data.message} type: ${data.throwable}")
                _matchHistory.value = ResultOf.Failure("Error while get data",null)
            }
            Log.d("HistoryScreenViewModel", "fetchMatchHistory: ${data.toString()}")
        }
    }
}