package com.dzikri.suwlitrockpaperscissor.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.data.model.MatchResult
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.SplashViewModel
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.EmptyList
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.ErrorWidget
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.HistoryList
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.HistoryLoadingWidget
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import kotlinx.coroutines.delay

@Composable
fun TutorialScreen(navController: NavController,innerPaddingValues: PaddingValues) {

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = innerPaddingValues.calculateTopPadding()).fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "How To Play?",
                style = TextStyle(
                    fontSize = 44.sp,
                    fontFamily = lilitaOneFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .background(Color.White).fillMaxHeight().fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = tutorialAnnotated(),
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        color = Color.Black
                    )
                }

            }
        }
    }
}

@Composable
private fun tutorialAnnotated() = buildAnnotatedString {
    fun b(s: String) = withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(s) }

    append("• The game is divided into "); b("Matches"); append(", and each Match contains "); b("5 Moves"); append(".\n\n")

    append("• At the start of every Move, you have "); b("5 seconds"); append(" to choose your hand:\n")
    append("  ✊ Rock — ✋ Paper — ✌ Scissors\n\n")

    append("• If you don’t make a choice in time, the system will "); b("automatically pick a random move"); append(" for you.\n\n")

    append("• After 5 Moves are finished, the player with the "); b("highest number of wins"); append(" is the winner of that "); b("Match"); append(".\n\n")

    append("• The Game continues with more Matches, and the overall "); b("Game winner"); append(" is the one who wins the "); b("most Matches"); append(".")
}