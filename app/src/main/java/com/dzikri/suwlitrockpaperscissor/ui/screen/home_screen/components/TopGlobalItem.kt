package com.dzikri.suwlitrockpaperscissor.ui.screen.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.data.model.TopGlobalItem
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily

@Composable
fun TopGlobalInstance(data: TopGlobalItem,thisPlayerUsername: String,onClicked: (() -> Unit)?) {
    val profile = painterResource(id = R.drawable.dummy_avatar)
    val isThisPlayer = thisPlayerUsername == data.username

    var topThreeImageId: Int? = null
    when(data.rank) {
        1 -> topThreeImageId = R.drawable.rank_1
        2 -> topThreeImageId = R.drawable.rank_2
        3 -> topThreeImageId = R.drawable.rank_3
        else -> null
    }

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).then(
            if(isThisPlayer)
                Modifier.clip(RoundedCornerShape(8.dp)).
                background(Color(0XFFD27623)).
                clickable(true, onClick = {
                    onClicked!!.invoke()
                })
            else Modifier
        )
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