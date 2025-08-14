package com.dzikri.suwlitrockpaperscissor.ui.screen.history_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import com.dzikri.suwlitrockpaperscissor.data.enums.MatchResultEnum
import com.dzikri.suwlitrockpaperscissor.data.model.MatchResult
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
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
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
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
                   is ResultOf.Loading -> HistoryLoadingWidget()
                   is ResultOf.Success -> {
                       val data = (matchHistoryState as ResultOf.Success<List<MatchResult>>)
                       if(data.value.isEmpty()) {
                           EmptyList()
                       } else {
                           HistoryList(data.value)
                       }
                   }
                   is ResultOf.Failure -> ErrorWidget(onClicked = {viewModel.fetchMatchHistory()})
               }
           }



        }
    }

}

@Composable
fun EmptyList() {
    val image = painterResource(R.drawable.empty_item)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
            ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = image,
                contentDescription = "empty item"
            )
        }
        Text(
            text = "Looks like you haven’t played yet",
            fontFamily = lilitaOneFamily,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF297DBA),
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryList(matchData: List<MatchResult>) {
    LazyColumn (
        modifier = Modifier.padding(horizontal = 10.dp)
    ){
        items(matchData) {
                matchData -> HistoryItem(matchData)                       }
    }
}


@Composable
fun HistoryLoadingWidget() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp)
        )
    }
}
@Composable
fun ErrorWidget(onClicked: () -> Unit) {
    var textWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val textWidthInDp = textWidth / density.density



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Error loading match history",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = lilitaOneFamily
                ),
                //Menghitung Width terlebih dahulu agar dipakai untuk button width
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    textWidth = coordinates.size.width
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onClicked()
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(textWidthInDp.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2395d2))
            ) {
                Text(
                    text = "Try Again",
                    fontFamily = lilitaOneFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 25.sp,
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(data: MatchResult) {
    val openAlertDialog = remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    val profile = painterResource(id = R.drawable.dummy_avatar)
    val matchRes = data.matchResult
    val resultText = matchRes.toString()
    val resultTextColor = when (matchRes) {
        MatchResultEnum.Victory -> Color.Green
        MatchResultEnum.Lose -> Color.Red
        MatchResultEnum.Draw -> Color.Gray
        else -> Color.White
    }
    val detailIcon = when (matchRes) {
        MatchResultEnum.Victory -> Icons.Filled.ThumbUp
        MatchResultEnum.Lose -> Icons.Filled.ThumbDown
        MatchResultEnum.Draw -> Icons.Filled.ThumbsUpDown
        else -> Icons.Filled.ThumbUp
    }

//    if(openAlertDialog.value) {
//        HistoryDetailDialog(
//            onDismissRequest = { openAlertDialog.value = false },
//            data
//        )
//    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            dragHandle = null,
            ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp).background(color = Color(0XFFe2f1df))
            ){
                Row (
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Icon(
                        imageVector = detailIcon as ImageVector,
                        contentDescription = "null",
                        modifier = Modifier.size(50.dp),
                        tint = resultTextColor
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        resultText,
                        style = TextStyle(
                            fontSize = 60.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = lilitaOneFamily,
                            color = resultTextColor
                        ),
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentHeight()
            ){
//                Text("Match Details", style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Normal,
//                    fontFamily = lilitaOneFamily)
//                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Column (
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ){
                        Image(
                            painter = profile,
                            contentDescription = "dummy avatar",
                            modifier = Modifier.size(50.dp),
                        )
                        Text(data.myUserName ?: "", style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = lilitaOneFamily,
                        ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis

                        )
                    }
                    Box(modifier = Modifier.width(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 15.dp).width(70.dp)
                    ){

                        Text("${data.myRoundScore} - ${data.enemyRoundScore}", style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = lilitaOneFamily
                        ))
                    }
                    Box(modifier = Modifier.width(10.dp))
                    Column (
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = profile,
                            contentDescription = "dummy avatar",
                            modifier = Modifier.size(50.dp),
                        )
                        Text(data.enemyUserName ?: "", style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = lilitaOneFamily,
                        ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis

                        )
                    }


                }
                Box(modifier = Modifier.height(10.dp))
//                Text("Scores", style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Normal,
//                    fontFamily = lilitaOneFamily,),
//                )
                Row (
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Text("${data.myScore.toString()} pts", style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = lilitaOneFamily,),
                    )
                    Text("${data.enemyScore.toString()} pts", style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = lilitaOneFamily,),

                        )
                }
                Box(modifier = Modifier.height(20.dp))
                Text(data.matchTime.toString(), style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = lilitaOneFamily,),
                )
                Box(modifier = Modifier.height(20.dp))

                IconButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Icon(painter = painterResource(R.drawable.close_button),contentDescription = "close button")
                }
                Box(modifier = Modifier.height(10.dp))

            }
        }
    }

    Box(
        modifier = Modifier.padding(vertical = 3.dp).border(
            width = 1.dp,
            color = resultTextColor,
            shape = RoundedCornerShape(15.dp)
        ).clickable(enabled = true, onClick = { showBottomSheet = true })
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


