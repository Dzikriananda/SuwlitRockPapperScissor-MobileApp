package com.dzikri.suwlitrockpaperscissor.ui.screen

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.dzikri.suwlitrockpaperscissor.data.model.ResultOf
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.LoginViewModel
import com.dzikri.suwlitrockpaperscissor.data.viewmodel.RegisterViewModel
import com.dzikri.suwlitrockpaperscissor.ui.theme.lilitaOneFamily
import com.dzikri.suwlitrockpaperscissor.ui.component.CustomTextField
import com.dzikri.suwlitrockpaperscissor.ui.navigation.Screen

@Composable
fun RegisterScreen(navController: NavController,viewModel: RegisterViewModel = hiltViewModel() ) {

    val username by viewModel.usernameInput.collectAsState()
    val email by viewModel.emailInput.collectAsState()
    val password by viewModel.passwordInput.collectAsState()
    val registerState by viewModel.registerResponse.collectAsState()

    if(registerState is ResultOf.Failure){
        ShowAlertDialogRegister(
            onClick = {viewModel.dismissAlertDialog()},
            message = (registerState as ResultOf.Failure).message ?: "Something went wrong",
            title = "Sign Up Failed"

        )
    } else if(registerState is ResultOf.Success){
        ShowAlertDialogRegister(
            onClick = {viewModel.dismissAlertDialog()},
            message = "Now You can Sign In With Your Account",
            title = "Sign Up Success"
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        BackgroundImage(modifier = Modifier.matchParentSize())
        Column (modifier = Modifier.padding(top = 50.dp, start = 25.dp, end = 25.dp)){
            Image(
                painter = painterResource(id = R.drawable.logo_horizontal),
                contentDescription = "Logo",
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Create a New Account",
                fontFamily = lilitaOneFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.width(200.dp)
            )
            Text(
                text = "Take the first step toward your best experience!",
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.width(200.dp)
            )
        }
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
                        .size(150.dp)
                        .offset(y = (10).dp)
                        .align(Alignment.End)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp)
                        .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                        .background(Color.White)
                        .padding(horizontal = 30.dp)
                ) {
                    Column {
                        Text(
                            text = "Sign Up",
                            fontFamily = lilitaOneFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF266489),
                            fontSize = 45.sp,
                            modifier = Modifier.padding(0.dp,35.dp,0.dp,30.dp)
                        )
                        RegisterTextField(
                            value = email.text,
                            modifier = Modifier.height(45.dp).fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
                            placeHolderValue = "Email",
                            onChange = { a -> viewModel.onEmailChange(a)},
                            isError = email.isError,
                            errorMessage = email.errorMessage,
                            leadingIcon = {
                                Icon(imageVector = Icons.Filled.Email, contentDescription = "email icon")
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        RegisterTextField(
                            value = username.text,
                            modifier = Modifier.height(45.dp).fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
                            placeHolderValue = "Username",
                            onChange = { a -> viewModel.onUsernameChange(a)},
                            isError = username.isError,
                            errorMessage = username.errorMessage,
                            leadingIcon = {
                                Icon(imageVector = Icons.Filled.People, contentDescription = "username icon")
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        RegisterTextField(
                            value = password.text,
                            modifier = Modifier.height(45.dp).fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
                            placeHolderValue = "Password",
                            onChange = { a -> viewModel.onPasswordChange(a)},
                            isError = password.isError,
                            errorMessage = password.errorMessage,
                            leadingIcon = {
                                Icon(imageVector = Icons.Filled.Lock, contentDescription = "password icon")
                            },
                            isPassword = true
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            onClick = {
                                if(registerState != ResultOf.Loading){
                                    viewModel.register()
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF2395d2))
                        ) {
                            if(registerState== ResultOf.Loading){
                                CircularProgressIndicator(
                                    modifier = Modifier.width(30.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            } else {
                                Text("Sign Up")
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Have an Account? Sign In Here",
                            fontWeight = FontWeight.Normal,
                            color = Color(0XFF2395d2),
                            modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                                navController.popBackStack()

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
fun RegisterTextField(
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
        placeholder = placeHolderValue,
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
fun ShowAlertDialogRegister(onClick: () -> Unit,message: String,title: String) {
    AlertDialog( // 3
        onDismissRequest = { // 4
        },
        // 5
        title = { Text(text = title) },
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




