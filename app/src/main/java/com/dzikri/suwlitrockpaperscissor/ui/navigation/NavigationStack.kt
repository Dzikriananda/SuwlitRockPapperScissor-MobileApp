package com.dzikri.suwlitrockpaperscissor

// NavigationStack.kt
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dzikri.suwlitrockpaperscissor.ui.component.SetNavBarColor
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen
import com.dzikri.suwlitrockpaperscissor.ui.screen.HomeScreen
import com.dzikri.suwlitrockpaperscissor.ui.screen.LoginScreen
import com.dzikri.suwlitrockpaperscissor.ui.screen.RegisterScreen
import com.dzikri.suwlitrockpaperscissor.ui.screen.SplashScreen

@Composable
fun NavigationStack(modifier: Modifier = Modifier,innerPaddingValues: PaddingValues) {
    val navController = rememberNavController()
    SetNavBarColor(Color.Transparent)
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier // ✅ apply innerPadding here
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController,innerPaddingValues = innerPaddingValues)
        }
    }
}
