    package com.dzikri.suwlitrockpaperscissor.ui.screen

    import android.util.Log
    import androidx.activity.compose.BackHandler
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.aspectRatio
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.pager.HorizontalPager
    import androidx.compose.foundation.pager.PagerDefaults
    import androidx.compose.foundation.pager.rememberPagerState
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Email
    import androidx.compose.material.icons.filled.People
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.ModalBottomSheet
    import androidx.compose.material3.ModalBottomSheetProperties
    import androidx.compose.material3.SheetValue
    import androidx.compose.material3.Text
    import androidx.compose.material3.rememberModalBottomSheetState
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.input.pointer.pointerInput
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.ui.window.SecureFlagPolicy
    import androidx.hilt.navigation.compose.hiltViewModel
    import androidx.lifecycle.compose.collectAsStateWithLifecycle
    import androidx.navigation.NavController
    import com.dzikri.suwlitrockpaperscissor.R
    import com.dzikri.suwlitrockpaperscissor.data.enums.GameMode
    import com.dzikri.suwlitrockpaperscissor.data.model.InputFieldState
    import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
    import com.dzikri.suwlitrockpaperscissor.data.viewmodel.HomeViewModel
    import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
    import com.dzikri.suwlitrockpaperscissor.ui.component.CustomTextField
    import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen
    import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
    import kotlinx.coroutines.launch

    @Composable
    fun HomeScreen(navController: NavController,innerPaddingValues: PaddingValues,viewModel: HomeViewModel = hiltViewModel()){

        val joinRoomStatus by viewModel.isJoiningRoom.collectAsStateWithLifecycle()

        LaunchedEffect(joinRoomStatus) {
            if(joinRoomStatus) {
                Log.d("tagg", "my Message")
                navController.navigate(route = Screen.Game.route)
            }
        }



        Box(
            modifier = Modifier.fillMaxSize()
        ){
            BackgroundImage(modifier = Modifier.matchParentSize())
            Column (modifier = Modifier
                .padding(innerPaddingValues)
                .padding(horizontal = 25.dp)){
                AvatarImage()
                PlayComponent(viewModel = viewModel)
            }
        }
    }

    @Composable
    fun TopComponent() {
        Row (){

        }
    }

    @Composable
    fun AvatarImage() {
        val image = painterResource(id = R.drawable.dummy_avatar)
        Image(
            painter = image,
            contentDescription = "dummy avatar",
            modifier = Modifier.size(50.dp),
        )
    }

    @Composable
    fun PlayComponent(viewModel: HomeViewModel) {
        val pagerState = rememberPagerState(pageCount = { 2 })

        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .height(420.dp)
                    .fillMaxWidth(),
                pageSpacing = 16.dp,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            ) { page ->
                when (page) {
                    0 -> PlayComponentOption(viewModel = viewModel,gameMode = GameMode.Multiplayer)
                    1 -> PlayComponentOption(viewModel = viewModel,gameMode = GameMode.VsBot)
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 12.dp else 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) Color(0xFFDE7629) else Color.LightGray)
                    )
                }
            }
        }
    }


    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun PlayComponentOption(gameMode: GameMode,viewModel: HomeViewModel) {
        val isJoiningRoom = viewModel.isJoiningRoom.collectAsStateWithLifecycle()

        val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { newState ->
            newState != SheetValue.Hidden //  Stop bottom sheet from hiding on outside press
        })

        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        val player2: String = if (gameMode == GameMode.Multiplayer) "Player 2" else "Bot"
        val buttonText: String = if (gameMode == GameMode.Multiplayer) "Multiplayer" else "Vs Bot"

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    properties = ModalBottomSheetProperties(
                        securePolicy = SecureFlagPolicy.SecureOn,
                        shouldDismissOnBackPress = false
                    )
                ) {

                    Box(modifier = Modifier
                        .height(300.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 0.dp)){
                    IconButton(
                        onClick = {
                            if(!isJoiningRoom.value) {
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet = false
                                }
                            }

                        } ) {
                        Icon(painter = painterResource(R.drawable.close_button),contentDescription = "close button")
                    }
                    if(gameMode == GameMode.Multiplayer) MultiplayerModalComponent(viewModel = viewModel) else VsBotModalComponent()


                }


            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            PlayerSide(
                label = player2,
                handImage = R.drawable.human_rock_home,
                blobImage = R.drawable.human_rock_home_bg,
                alignment = Alignment.CenterEnd,
                modifier = Modifier.align(Alignment.CenterEnd),
                handAlignment = Alignment.BottomEnd,

                )

            PlayerSide(
                label = "Player 1",
                handImage = R.drawable.human_scissors_home,
                blobImage = R.drawable.human_scissors_home_bg,
                alignment = Alignment.CenterStart,
                modifier = Modifier.align(Alignment.CenterStart),
                handAlignment = Alignment.TopStart,
            )



            Text(
                text = "VS",
                fontFamily = lilitaOneFamily,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002244),
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "Player 1",
                fontFamily = lilitaOneFamily,
                fontSize = 25.sp,
                color = Color(0xFF002244),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 70.dp, start = 30.dp)
            )
            Text(
                text = player2,
                fontFamily = lilitaOneFamily,
                fontSize = 25.sp,
                color = Color(0xFF002244),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 30.dp, end = 20.dp)
            )
            Button(
                onClick = { showBottomSheet = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE7629)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

        }
    }
    @Composable
    fun PlayerSide(
        label: String,
        handImage: Int,
        blobImage: Int,
        alignment: Alignment,
        handAlignment: Alignment,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Background (left or right half)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(alignment), // Align blob to left or right
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = blobImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            // Hand (takes entire space, not clipped to 0.5f)
            Image(
                painter = painterResource(id = handImage),
                contentDescription = null,
                modifier = Modifier
                    .height(275.dp)
                    .align(handAlignment)
                ,
                contentScale = ContentScale.Fit
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GameTextField(
        value: String,
        modifier: Modifier = Modifier,
        placeHolderValue: String = "",
        onChange: (String) -> Unit,
        isError: Boolean,
        errorMessage: String,
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        isPassword: Boolean = false,
    ) {
        CustomTextField(
            value = value,
            onValueChange = { onChange(it) },
            placeholder = { Text(text = placeHolderValue, fontFamily = lilitaOneFamily, fontSize = 30.sp, color = Color.Gray) },
            isError = isError,
            borderColor = Color.Gray,
            borderWidth = 1.dp,
            shape = RoundedCornerShape(8.dp),
            errorMessage = errorMessage,
            modifier = Modifier.fillMaxWidth(),
            height = 55.dp,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isPassword = isPassword,
            horizontalPadding = 20.dp,
            textStyle = TextStyle(fontFamily = lilitaOneFamily, fontSize = 30.sp)
        )
    }

@Composable
fun MultiplayerModalComponent(viewModel: HomeViewModel) {
    val roomIdInput = viewModel.roomIdInput.collectAsStateWithLifecycle()
    val isJoiningRoom = viewModel.isJoiningRoom.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Multiplayer",
            fontFamily = lilitaOneFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontSize = 35.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.createNewRoom()

            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF2395d2))
        ) {
            if(isJoiningRoom.value  == true){
                CircularProgressIndicator(
                    modifier = Modifier.width(30.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            } else {
                Text(
                    text = "New Room",
                    fontFamily = lilitaOneFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 25.sp,
                )            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Enter Room",
            fontFamily = lilitaOneFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontSize = 35.sp,
        )
        //                   Spacer(modifier = Modifier.height(20.dp))
        GameTextField(
            value = roomIdInput.value.text ,
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                ),
            placeHolderValue = "Room Code",
            onChange = {it -> viewModel.onRoomIdInputChange(it)},
            isError = false,
            errorMessage = "",

            )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF2395d2))
        ) {
            Text(
                text = "Enter Room",
                fontFamily = lilitaOneFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 25.sp,
            )
        }

    }

}






    @Composable
    fun VsBotModalComponent() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Versus Bot",
                fontFamily = lilitaOneFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontSize = 35.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {

                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF2395d2))
            ) {
                Text(
                    text = "Play",
                    fontFamily = lilitaOneFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 25.sp,
                )
            }
        }

    }

