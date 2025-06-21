package com.dzikri.suwlitrockpaperscissor.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzikri.suwlitrockpaperscissor.data.network.ApiInterface
import com.dzikri.suwlitrockpaperscissor.data.network.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(99)
    val uiState: StateFlow<Int> = _uiState.asStateFlow()

    fun login() {
        val apiService = RetrofitInstance.getInstance().create<ApiInterface>(ApiInterface::class.java)
        viewModelScope.launch {
            val body = mapOf(
                "email" to "perrelbrown12@yahoossso.com",
                "password" to "12827281919jhh"
            )
            val result = apiService.login(body)
            if (result != null)
                Log.d("log: ", result.body().toString())
            }
        }
}
