package com.dzikri.suwlitrockpaperscissor.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage

@Composable
fun GameScreen(innerPaddingValues: PaddingValues,) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (modifier = Modifier
            .padding(innerPaddingValues)
        ){}

    }
}