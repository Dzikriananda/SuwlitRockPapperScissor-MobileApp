package com.dzikri.suwlitrockpaperscissor.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.LoginViewModel
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import com.dzikri.suwlitrockpaperscissor.ui.component.CustomTextField

@Composable
fun LoginScreen(navController: NavController,viewModel: LoginViewModel = hiltViewModel() ) {
    val text by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Text(
            text = "Suwlit $text",
            fontFamily = lilitaOneFamily,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            fontSize = 70.sp,
            modifier = Modifier.align(Alignment.TopCenter).padding(0.dp,80.dp,0.dp,0.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 25.dp)
        ) {
           Column {
               Image(
                   painter = painterResource(id = R.drawable.logo_no_text),
                   contentDescription = "Logo",
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .size(200.dp)
                       .offset(y = (10).dp)
               )
               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(400.dp)
                       .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                       .background(Color.White)
                       .padding(horizontal = 30.dp)
               ) {
                  Column {
                      Text(
                          text = "Sign In",
                          fontFamily = lilitaOneFamily,
                          fontWeight = FontWeight.Normal,
                          color = Color(0xFF266489),
                          fontSize = 45.sp,
                          modifier = Modifier.padding(0.dp,35.dp,0.dp,30.dp)
                      )
                      LoginTextField(
                          modifier = Modifier.height(45.dp).fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
                          placeHolderValue = "Username or Email"
                      )
                      Spacer(modifier = Modifier.height(15.dp))
                      LoginTextField(
                          modifier = Modifier.height(45.dp).fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
                          placeHolderValue = "Password"
                      )
                      Spacer(modifier = Modifier.height(40.dp))
                      Button(
                          onClick = {},
                          shape = RoundedCornerShape(8.dp),
                          modifier = Modifier.fillMaxWidth().height(45.dp),
                          colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF2395d2))
                      ) {
                          Text("Sign In")
                      }
                      Spacer(modifier = Modifier.height(20.dp))
                      Text(
                          text = "Create a New Account",
                          fontWeight = FontWeight.Normal,
                          color = Color(0XFF2395d2),
                          modifier = Modifier.align(Alignment.CenterHorizontally).clickable { Log.d("ClickableTest", "dreamybull")
                          }
                      )

                  }
               }
           }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    placeHolderValue: String = ""
) {
    var text by remember { mutableStateOf("") }

    CustomTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = placeHolderValue,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    )
}





