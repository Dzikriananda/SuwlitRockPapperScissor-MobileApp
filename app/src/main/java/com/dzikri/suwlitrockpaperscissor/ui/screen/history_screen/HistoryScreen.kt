package com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.HistoryScreenViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import com.dzikri.suwlitrockpaperscissor.data.enums.MatchResultEnum
import com.dzikri.suwlitrockpaperscissor.data.model.MatchResult


@Composable
fun HistoryScreen(navController: NavController,innerPaddingValues: PaddingValues,viewModel: HistoryScreenViewModel = hiltViewModel()) {
    val matchHistoryState by viewModel.matchHistory.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchMatchHistory()
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "MATCH HISTORY",
                style = TextStyle(fontSize = 44.sp, fontFamily = lilitaOneFamily, color = Color.White),
                textAlign = TextAlign.Center,
            )
           Box(
               modifier = Modifier.clip(RoundedCornerShape(15.dp,15.dp,0.dp,0.dp)).background(Color.White).fillMaxHeight().fillMaxWidth()
           ) {
               when (matchHistoryState) {
                   is ResultOf.Started -> Text("Ready to load history")
                   is ResultOf.Loading -> CircularProgressIndicator()
                   is ResultOf.Success -> {
                       val data = (matchHistoryState as ResultOf.Success<List<MatchResult>>)
                       LazyColumn (
                           modifier = Modifier.padding(horizontal = 10.dp)
                       ){
                           items(data.value) {
                               matchData -> HistoryItem(matchData)                       }
                       }
                   }
                   is ResultOf.Failure -> Text("Error loading match history")
               }
           }



        }
    }

}


@Composable
fun HistoryItem(data: MatchResult) {
    val profile = painterResource(id = R.drawable.dummy_avatar)
    val matchRes = data.matchResult
    val resultText = matchRes.toString()
    val resultTextColor = when (matchRes) {
        MatchResultEnum.Victory -> Color.Green
        MatchResultEnum.Lose -> Color.Red
        MatchResultEnum.Draw -> Color.Gray
        else -> Color.White
    }
    Box(
        modifier = Modifier.padding(vertical = 3.dp).border(
            width = 1.dp,
            color = resultTextColor,
            shape = RoundedCornerShape(15.dp)
        )

    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = profile,
                contentDescription = "dummy avatar",
                modifier = Modifier.size(50.dp),
            )
            Box(modifier = Modifier.width(10.dp))
            Text("You", style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = lilitaOneFamily
            ),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 15.dp).width(70.dp)
            ){
                Text(resultText, style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = lilitaOneFamily,
                    color = resultTextColor
                ))
                Text("${data.myRoundScore} - ${data.enemyRoundScore}", style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = lilitaOneFamily
                ))
            }
            Image(
                painter = profile,
                contentDescription = "dummy avatar",
                modifier = Modifier.size(50.dp),
            )
            Box(modifier = Modifier.width(10.dp))
            Text(data.enemyUserName ?: "", style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = lilitaOneFamily,
            ),
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )

        }
    }
}
