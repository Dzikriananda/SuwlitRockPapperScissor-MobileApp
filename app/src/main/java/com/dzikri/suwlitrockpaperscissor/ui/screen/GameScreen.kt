package com.dzikri.suwlitrockpaperscissor.ui.screen

import android.media.MediaPlayer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import androidx.compose.foundation.Image
import com.dzikri.suwlitrockpaperscissor.R
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.dzikri.suwlitrockpaperscissor.data.model.GameStartingStatus
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.GameViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import com.dzikri.suwlitrockpaperscissor.data.enums.Move
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.DisposableEffect


@Composable
fun GameScreen(navController: NavController,innerPaddingValues: PaddingValues,roomId: String?,viewModel: GameViewModel = hiltViewModel()) {

    val roomStatus by viewModel.gameInitStatus.collectAsStateWithLifecycle()
    val timerCountDown = viewModel.gameStartTimerCount.collectAsStateWithLifecycle()
    val roundTimerCountDown = viewModel.roundTimerCount.collectAsStateWithLifecycle()
    val gameStartingStatus by viewModel.gameStartingStatus.collectAsStateWithLifecycle()
    var showQuitDialog by remember { mutableStateOf(false) }


    val mContext = LocalContext.current
    val gameStartCountDownMediaPlayer = remember {
        MediaPlayer.create(mContext, R.raw.countdown)
    }
    val gameBGMMediaPlayer = remember {
        MediaPlayer.create(mContext, R.raw.gameplay_bg_tetris_theme_piano)
    }

    fun playCountdownSound() {
        gameStartCountDownMediaPlayer.start()
    }

    fun playBGM() {
        gameBGMMediaPlayer.start()
    }

    fun clearMediaPlayer(){
        gameStartCountDownMediaPlayer.release()
        gameBGMMediaPlayer.release()
    }

    DisposableEffect(Unit) {
        onDispose {
            clearMediaPlayer()
            viewModel.clearJob()
        }
    }


    LaunchedEffect(gameStartingStatus) {
        if(gameStartingStatus){
            playBGM()
        }
    }

    BackHandler {
        if(roomStatus == ResultOf.Started) {
            showQuitDialog = true
        }
    }

    if (showQuitDialog) {
        QuitGameDialog(
            onConfirm = {
                showQuitDialog = false
                navController.popBackStack()
            },
            onDismiss = {
                showQuitDialog = false
            }
        )
    }




    //Wajib agar tidak terjadi kebocoran ram/memori HP
    fun onPressBack() {
        gameStartCountDownMediaPlayer.release()
        gameBGMMediaPlayer.release()
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
        if(roomId != null) {
            viewModel.joinRoom(roomId)
        } else {
            viewModel.createRoom()
        }
    }



    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Text(
            text = roundTimerCountDown.value.toString(),
            style = TextStyle(fontSize = 44.sp, fontFamily = lilitaOneFamily, color = Color.White),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
        PlayerHands()
        Column (modifier = Modifier
            .padding(innerPaddingValues)
        ){
            if(gameStartingStatus) {
                TopComponent()
            }

            Spacer(modifier = Modifier.weight(1f))
            if(gameStartingStatus) {
                Row (
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ){
                    PlayerButton(Move.Rock,{})
                    PlayerButton(Move.Paper,{})
                    PlayerButton(Move.Scissors,{})
                }
            }

        }

    }
}

@Composable
fun TopComponent() {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dreamybull12xxx",
                style = TextStyle(fontSize = 20.sp, fontFamily = lilitaOneFamily, color = Color.Black),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(100.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.logo_horizontal),
                contentDescription = "Logo",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "You",
                style = TextStyle(fontSize = 20.sp, fontFamily = lilitaOneFamily, color = Color.Black),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(100.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
            ,
            verticalAlignment = Alignment.Top
        ) {
            PlayerScore(modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            PlayerScore(modifier = Modifier.width(100.dp))
        }
    }
}


@Composable
fun PlayerScore(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Round", fontSize = 12.sp, color = Color.White)
        Text(
            text = "0",
            style = TextStyle(fontSize = 44.sp, fontFamily = lilitaOneFamily, color = Color.White),
            textAlign = TextAlign.Center,
        )
        Text("Game", fontSize = 12.sp, color = Color.White)
        Text(
            text = "0",
            style = TextStyle(fontSize = 44.sp, fontFamily = lilitaOneFamily, color = Color.White),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun PlayerButton(move: Move,onClick: () -> Unit) {

    val icon: Int = when(move){
        Move.Rock -> R.drawable.rock_button
        Move.Paper -> R.drawable.paper_button
        Move.Scissors -> R.drawable.scissors_button
    }

    val bgColor: Color = when(move){
        Move.Rock -> Color(0xFFBA1A1A).copy(alpha = 0.7f)
        Move.Paper -> Color(0xFFD27623).copy(alpha = 0.7f)
        Move.Scissors -> Color(0xFF27C200).copy(alpha = 0.7f)
    }

    Box(
        modifier = Modifier.size(100.dp).then(
            if(move == Move.Paper) Modifier.offset(y = -50.dp)
            else Modifier
        ).background(bgColor, shape = CircleShape).clickable{
            onClick()
        }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "scissors_button",
            modifier = Modifier.align(Alignment.Center).size(70.dp).then(
                if (move == Move.Scissors) Modifier.offset(x = -2.dp)
                else Modifier
            )
        )

    }
}

@Composable
fun PlayerHands() {
    var visible by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Toggle button to trigger animation
//        Button(
//            onClick = { visible = !visible },
//            modifier = Modifier.align(Alignment.Center)
//        ) {
//            Text("Toggle Slide")
//        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it * 2 },
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.humanrock),
                contentDescription = "Top Image",
                modifier = Modifier
                    .rotate(90f).height(350.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Bottom image: slide in from bottom, out to bottom
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it * 2 },
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.humanrock),
                contentDescription = "Bottom Image",
                modifier = Modifier
                    .rotate(270f)
                    .height(350.dp),
                contentScale = ContentScale.Fit
            )
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

@Composable
fun QuitGameDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Are you sure you want to quit the game?")
                Spacer(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(8.dp),
                        ) {
                        Text("Quit")
                    }
                }
            }
        }
    }
}