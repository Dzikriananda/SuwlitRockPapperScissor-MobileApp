package com.dzikri.suwlitrockpaperscissor.ui.screen.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dzikri.suwlitrockpaperscissor.data.model.RankData
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.model.TopGlobalItem
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.HomeViewModel
import com.dzikri.suwlitrockpaperscissor.ui.theme.shimmerBackground

@Composable
fun TopGlobalComponent(viewModel: HomeViewModel,onNavigate: () -> Unit) {

    val thisPlayerUsername by viewModel.username.collectAsStateWithLifecycle()
    val topGlobalList = viewModel.leaderboardsRanks.collectAsStateWithLifecycle()

    Box(modifier = Modifier
        .padding(horizontal = 0.dp, vertical = 5.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Column{
            if(topGlobalList.value is ResultOf.Success) {
                val topGlobalDto = (topGlobalList.value as ResultOf.Success<List<RankData>>).value.map {
                    TopGlobalItem(it.rank,it.username,null,it.score)
                }
                topGlobalDto.forEach {
                        item -> TopGlobalInstance(item,thisPlayerUsername,onNavigate)
                }
//                (topGlobalList.value as ResultOf.Success<List<RankData>>).value.forEach {
//                        item -> TopGlobalInstance(item,thisPlayerUsername)
//                }
            } else {
                for(i in 0 until 4) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .shimmerBackground(RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}