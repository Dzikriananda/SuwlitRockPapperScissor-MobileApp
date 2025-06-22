package com.dzikri.suwlitrockpaperscissor.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.dzikri.suwlitrockpaperscissor.R

@Composable
fun BackgroundImage(modifier: Modifier) {
    val image = painterResource(id = R.drawable.background)
    Image(
        painter = image,
        contentDescription = "background",
        contentScale = ContentScale.Companion.Crop,
        modifier = modifier,
    )
}