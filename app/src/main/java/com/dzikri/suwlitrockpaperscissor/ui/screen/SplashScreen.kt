package com.dzikri.suwlitrockpaperscissor.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.SplashViewModel
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (navController: NavController,viewModel: SplashViewModel = hiltViewModel()) {
    val isLoggedIn = viewModel.isLoggedIn.collectAsState()


    LaunchedEffect(isLoggedIn.value) {
        viewModel.checkIfUserIsLoggedIn()
        delay(2000)
        if (isLoggedIn.value) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        LogoImage(modifier = Modifier.align(Alignment.Center).size(250.dp))

    }
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
