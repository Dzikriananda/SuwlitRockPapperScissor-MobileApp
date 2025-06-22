package com.dzikri.suwlitrockpaperscissor.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily

@Composable
fun HomeScreen(navController: NavController,innerPaddingValues: PaddingValues){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (modifier = Modifier.padding(innerPaddingValues).padding(horizontal = 25.dp)){
            AvatarImage()
            PlayComponent()

        }
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
@Preview
fun PlayComponent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        // Stack Player 1 and 2 freely
        Box(modifier = Modifier.fillMaxSize()) {
            PlayerSide(
                label = "Player 1",
                handImage = R.drawable.human_scissors_home,
                blobImage = R.drawable.human_scissors_home_bg,
                alignment = Alignment.CenterStart,
                modifier = Modifier.align(Alignment.CenterStart),
                handAlignment = Alignment.TopStart,
            )

            PlayerSide(
                label = "Player 2",
                handImage = R.drawable.human_rock_home,
                blobImage = R.drawable.human_rock_home_bg,
                alignment = Alignment.CenterEnd,
                modifier = Modifier.align(Alignment.CenterEnd),
                handAlignment = Alignment.BottomEnd,

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
                modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 70.dp, start = 30.dp)
            )
            Text(
                text = "Player 2",
                fontFamily = lilitaOneFamily,
                fontSize = 25.sp,
                color = Color(0xFF002244),
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 30.dp, end = 20.dp)
            )
                    Button(
            onClick = { /* TODO: Navigate to multiplayer game */ },
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
                text = "Multi Player",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
            }
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
                .height(300.dp) // 👈 Full canvas to scale as large as needed
                .align(handAlignment),
            contentScale = ContentScale.Fit
        )
    }
}


