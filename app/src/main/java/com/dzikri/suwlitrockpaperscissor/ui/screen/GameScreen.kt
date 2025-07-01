package com.dzikri.suwlitrockpaperscissor.ui.screen

import android.media.MediaPlayer
import android.os.Message
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import kotlinx.coroutines.delay
import android.util.Log
import com.dzikri.suwlitrockpaperscissor.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.data.model.GameStartingStatus
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.GameViewModel

@Composable
fun GameScreen(navController: NavController,innerPaddingValues: PaddingValues,roomId: String?,viewModel: GameViewModel = hiltViewModel()) {

    var bool by remember { mutableStateOf(true) }
    val roomStatus by viewModel.gameInitStatus.collectAsStateWithLifecycle()
    val timerCountDown = viewModel.timerCount.collectAsStateWithLifecycle()

    val mContext = LocalContext.current
    val mMediaPlayer = MediaPlayer.create(mContext, R.raw.countdown)

    fun playCountdownSound() {
        mMediaPlayer.start()
    }


    //Wajib agar tidak terjadi kebocoran ram/memori HP
    fun onPressBack() {
        viewModel.clearJob()
        navController.popBackStack()
    }

    fun onRetry() {
        viewModel.retry()
    }

    when(roomStatus) {
        is ResultOf.Success<GameStartingStatus> -> {
            if((roomStatus as ResultOf.Success<GameStartingStatus>).value.gameStart == true) {
                GameStartDialog(
                    onDismissRequest = {onPressBack()},
                    timerCountDown = timerCountDown.value.toString()
                )
                LaunchedEffect(true) {
                    playCountdownSound()
                    viewModel.startGame()
                }
            } else {
                WaitingDialog(
                    onDismissRequest = {onPressBack()},
                    message = (roomStatus as ResultOf.Success<GameStartingStatus>).value.roomId
                )
            }
        }
        is ResultOf.Loading -> LoadingDialog(onDismissRequest = {onPressBack()})
        is ResultOf.Failure -> ErrorDialog(
            onDismissRequest = { onPressBack() },
            onRetryRequest = {onRetry()},
            (roomStatus as ResultOf.Failure).message!!
        )
        is ResultOf.Started -> {}
    }


    LaunchedEffect(Unit) {
        viewModel.createRoom()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (modifier = Modifier
            .padding(innerPaddingValues)
        ){
            Text(roomId ?: "", style = TextStyle(fontSize = 40.sp))
        }

    }
}

@Composable
fun WaitingDialog(onDismissRequest: () -> Unit,message: String) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        BackHandler {
            onDismissRequest()
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,){
                Text(
                    text = "Room Created!",
                    style = TextStyle(fontSize = 24.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Share this code with your friend to join:",
                    style = TextStyle(fontSize = 20.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message.toString(),
                    style = TextStyle(fontSize = 20.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Composable
fun LoadingDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        BackHandler {
            onDismissRequest()
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center).
                size(50.dp)
            )
        }
    }
}

@Composable
fun ErrorDialog(onDismissRequest: () -> Unit,onRetryRequest: () -> Unit,errorMessage: String,) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        BackHandler {
            onDismissRequest()
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Error!",
                    style = TextStyle(fontSize = 24.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = errorMessage,
                    style = TextStyle(fontSize = 20.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        onRetryRequest()
                    }
                ) {
                    Text(
                        text = "Retry",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun GameStartDialog(onDismissRequest: () -> Unit,timerCountDown: String) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        BackHandler {
            onDismissRequest()
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Other User Has Joined !",
                    style = TextStyle(fontSize = 24.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Starting Game In :",
                    style = TextStyle(fontSize = 20.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = timerCountDown,
                    style = TextStyle(fontSize = 30.sp, fontFamily = lilitaOneFamily),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}