package com.dzikri.suwlitrockpaperscissor.ui.screen.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dzikri.suwlitrockpaperscissor.data.model.TopGlobalItem
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.HomeViewModel

@Composable
fun TopGlobalComponent(viewModel: HomeViewModel) {

    val rank1 = TopGlobalItem(1,"Dreamybull12",null,900)
    val rank2 = TopGlobalItem(2,"BagastonGastanyo",null,600)
    val rank3 = TopGlobalItem(3,"Ambatukhan",null,400)
    val rank22 = TopGlobalItem(22,"perrelbrown",null,150)
    val thisPlayerUsername by viewModel.username.collectAsStateWithLifecycle()

    val rankList = listOf<TopGlobalItem>(rank1,rank2,rank3,rank22)

    Box(modifier = Modifier
        .padding(horizontal = 0.dp, vertical = 5.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Column{
            rankList.forEach {
                    item -> TopGlobalInstance(item,thisPlayerUsername)
            }
        }
    }
}