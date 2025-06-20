package com.dzikri.suwlitrockpaperscissor.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        LogoImage(modifier = Modifier.align(Alignment.Center).size(250.dp))
        Button(onClick = {
            navController.navigate(route = Screen.Login.route)
        }) {
            Text("Next Screen")
        }
    }
}

@Composable
fun BackgroundImage(modifier: Modifier) {
    val image = painterResource(id = R.drawable.background)
    Image(
        painter = image,
        contentDescription = "background",
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Composable
fun LogoImage(modifier: Modifier) {
    val image = painterResource(id = R.drawable.logo)
    Image(
        painter = image,
        contentDescription = "background",
        modifier = modifier,
    )
}
