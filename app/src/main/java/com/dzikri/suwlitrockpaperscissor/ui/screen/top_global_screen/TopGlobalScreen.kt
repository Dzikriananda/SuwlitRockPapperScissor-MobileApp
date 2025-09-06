package com.dzikri.suwlitrockpaperscissor.ui.screen.top_global_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.data.model.MatchResult
import com.dzikri.suwlitrockpaperscissor.data.model.RankData
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.EmptyList
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.ErrorWidget
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.HistoryItem
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.HistoryList
import com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen.HistoryLoadingWidget
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily


@Composable
fun TopGlobalScreen(navController: NavController,innerPaddingValues: PaddingValues) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<List<RankData>>("data") ?: emptyList()
    val username = navController.previousBackStackEntry?.savedStateHandle?.get<String>("username") ?: ""
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text(
                text = "100 Top Players",
                style = TextStyle(fontSize = 44.sp, fontFamily = lilitaOneFamily, color = Color.White),
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier.clip(RoundedCornerShape(15.dp,15.dp,0.dp,0.dp)).background(Color.White).fillMaxHeight().fillMaxWidth()
            ) {
                LazyColumn (
                    modifier = Modifier.padding(horizontal = 10.dp)
                ){
                    items(data) {
                            matchData -> TopGlobalScreenItem(matchData,username)
                    } 

                }

            }
        }
    }
}


@Composable
fun TopGlobalScreenItem(data: RankData,thisPlayerUsername: String) {
    val profile = painterResource(id = R.drawable.dummy_avatar)
    val isThisPlayer = ((data.username) == thisPlayerUsername)

    var topThreeImageId: Int? = null
    when(data.rank) {
        1 -> topThreeImageId = R.drawable.rank_1
        2 -> topThreeImageId = R.drawable.rank_2
        3 -> topThreeImageId = R.drawable.rank_3
        else -> null
    }

    Box(
        modifier = Modifier.padding(vertical = 3.dp).fillMaxWidth().border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(15.dp)

        ).then(
            if(isThisPlayer)
                Modifier.clip(RoundedCornerShape(15.dp)).
                background(Color(0XFFD27623))
            else Modifier
        )
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
        ){
            if (topThreeImageId != null) {
                Image(
                    painter = painterResource(id = topThreeImageId!!),
                    contentDescription = "dummy avatar2",
                    modifier = Modifier.size(50.dp),
                )
            } else {
                Text(data.rank.toString(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = lilitaOneFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isThisPlayer) Color.White else Color.Black
                    ),
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier.width(10.dp))
            Image(
                painter = profile,
                contentDescription = "dummy avatar",
                modifier = Modifier.size(50.dp),
            )
            Box(modifier = Modifier.width(10.dp))
            Text(data.username, style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            ),
                modifier = Modifier.weight(1f)
            )
            Text(data.score.toString(), style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
                ,
                modifier = if (isThisPlayer) Modifier.padding(end = 5.dp) else Modifier
            )

        }
    }





}