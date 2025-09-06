package com.dzikri.suwlitrockpaperscissor.ui.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash_screen")
    object Login: Screen("login_screen")
    object Register: Screen("register_screen")
    object Home: Screen("home_screen")
    object Game: Screen("game_screen")
    object History: Screen("history_screen")
    object TopGlobal: Screen("top_global_screen")
    object Tutorial: Screen("tutorial_screen")



}