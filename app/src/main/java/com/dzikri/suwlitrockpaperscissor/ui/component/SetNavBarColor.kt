package com.dzikri.suwlitrockpaperscissor.ui.component

import android.app.Activity
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.graphics.toArgb

@Composable
fun SetNavBarColor(color: Color) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = color.toArgb()
            //control icon visibility (light/dark)
            WindowInsetsControllerCompat(window, view).isAppearanceLightNavigationBars =
                color.luminance() > 0.5f // true = dark icons
            //control status bar icon visibility (light/dark)
            WindowInsetsControllerCompat(window,view).isAppearanceLightStatusBars = false
        }
    }
}
