package com.dzikri.suwlitrockpaperscissor.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dzikri.suwlitrockpaperscissor.R
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.LoginViewModel
import com.dzikri.suwlitrockpaperscissor.ui.component.BackgroundImage
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import com.dzikri.suwlitrockpaperscissor.ui.component.CustomTextField
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen

@Composable
fun LoginScreen(navController: NavController,viewModel: LoginViewModel = hiltViewModel() ) {

    val username by viewModel.usernameOrEmailInput.collectAsState()
    val password by viewModel.passwordInput.collectAsState()
    val loginByEmailPasswordState by viewModel.loginResponse.collectAsState()
    val loginByGoogleState by viewModel.loginWithGoogleResponse.collectAsStateWithLifecycle()
    val isNowSigningIn by viewModel.isInSignInAttempt.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val displayMetrics = context.resources.displayMetrics

    // Width and height of screen
    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels

    LaunchedEffect(Unit) {
        Log.d("Height : ", height.toString())
        Log.d("Width : ", width.toString())
    }

    if(loginByEmailPasswordState is ResultOf.Failure){
        ShowAlertDialog(
            onClick = {viewModel.dismissAlertDialog()},
            message = (loginByEmailPasswordState as ResultOf.Failure).message ?: "Something went wrong"
        )
    }

    LaunchedEffect(loginByEmailPasswordState) {
        if(loginByEmailPasswordState is ResultOf.Success){
            viewModel.resetLoginState()
            navController.navigate(route = Screen.Home.route)
        }
    }

    if(loginByGoogleState is ResultOf.Failure){
        ShowAlertDialog(
            onClick = {viewModel.dismissAlertDialog()},
            message = (loginByGoogleState as ResultOf.Failure).message ?: "Something went wrong"
        )
    }

    LaunchedEffect(loginByGoogleState) {
        if(loginByGoogleState is ResultOf.Success){
            viewModel.resetLoginState()
            navController.navigate(route = Screen.Home.route)
        }
    }



    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Text(
            text = "Suwlit",
            fontFamily = lilitaOneFamily,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            fontSize = 70.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(0.dp, (height * 0.035).dp, 0.dp, 0.dp)
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
                       .size((height * 0.09).dp) //Originally 200.dp
                       .offset(y = (10).dp)
               )
               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight(0.75f)
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
                          value = username.text,
                          modifier = Modifier
                              .height(45.dp)
                              .fillMaxWidth()
                              .border(
                                  width = 1.dp,
                                  color = Color.Gray,
                                  shape = RoundedCornerShape(8.dp)
                              ),
                          placeHolderValue = "Email",
                          onChange = { a -> viewModel.onUsernameOrEmailChange(a)},
                          isError = username.isError,
                          errorMessage = username.errorMessage,
                          leadingIcon = {
                              Icon(imageVector = Icons.Filled.Email, contentDescription = "email icon")
                          }
                      )
                      Spacer(modifier = Modifier.height(15.dp))
                      LoginTextField(
                          value = password.text,
                          modifier = Modifier
                              .height(45.dp)
                              .fillMaxWidth()
                              .border(
                                  width = 1.dp,
                                  color = Color.Gray,
                                  shape = RoundedCornerShape(8.dp)
                              ),
                          placeHolderValue = "Password",
                          onChange = { a -> viewModel.onPasswordChange(a)},
                          isError = password.isError,
                          errorMessage = password.errorMessage,
                          leadingIcon = {
                              Icon(imageVector = Icons.Filled.Lock, contentDescription = "password icon")
                          },
                          isPassword = true
                      )
                      Spacer(modifier = Modifier.height(30.dp))
                      Button(
                          onClick = {
                              if(!isNowSigningIn){
                                  viewModel.login()
                              }
                          },
                          shape = RoundedCornerShape(8.dp),
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(45.dp),
                          colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF2395d2))
                      ) {
                          if(loginByEmailPasswordState == ResultOf.Loading){
                              CircularProgressIndicator(
                                  modifier = Modifier.width(30.dp),
                                  color = MaterialTheme.colorScheme.secondary,
                                  trackColor = MaterialTheme.colorScheme.surfaceVariant,
                              )
                          } else {
                              Text("Sign In")
                          }
                      }
                      Spacer(modifier = Modifier.height(10.dp))
                      Button(
                          onClick = {
                              if(!isNowSigningIn) {
                                  viewModel.loginWithGoogle(context = context)
                              }
                          },
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(45.dp)
                              .border( width = 2.dp,
                                  color = Color(0XFF2395d2),
                                  shape = RoundedCornerShape(8.dp)),
                          colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                      ) {
                          if(loginByGoogleState == ResultOf.Loading){
                              CircularProgressIndicator(
                                  modifier = Modifier.width(30.dp),
                                  color = MaterialTheme.colorScheme.secondary,
                                  trackColor = MaterialTheme.colorScheme.surfaceVariant,
                              )
                          } else {
                              Image(
                                  painter = painterResource(R.drawable.google_icon),
                                  contentDescription = "background",
                                  modifier = Modifier.height(40.dp)
                              )
                              Text("Sign in with Google", color = Color(0XFF2395d2))                          }
                      }
                      Spacer(modifier = Modifier.height(20.dp))
                      Text(
                          text = "Create a New Account",
                          fontWeight = FontWeight.Normal,
                          color = Color(0XFF2395d2),
                          modifier = Modifier
                              .align(Alignment.CenterHorizontally)
                              .clickable {
                                  if(!isNowSigningIn){
                                      navController.navigate(route = Screen.Register.route)
                                  }
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
        placeholder = { Text(text = placeHolderValue, fontSize = 17.sp, color = Color.Gray) },
        isError = isError,
        borderColor = Color.Gray,
        borderWidth = 1.dp,
        shape = RoundedCornerShape(8.dp),
        errorMessage = errorMessage,
        modifier = Modifier.fillMaxWidth(),
        height = 45.dp,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isPassword = isPassword
    )
}

@Composable
fun ShowAlertDialog(onClick: () -> Unit,message: String) {
    AlertDialog( // 3
        onDismissRequest = { // 4
        },
        // 5
        title = { Text(text = "Sign In Failed") },
        text = { Text(text = message) },
        confirmButton = { // 6
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onClick()
                }
            ) {
                Text(
                    text = "Close",
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}




