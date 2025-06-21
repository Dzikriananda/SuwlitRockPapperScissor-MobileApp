package com.dzikri.suwlitrockpaperscissor.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "", // ✅ add this
    modifier: Modifier = Modifier,
    borderColor: Color = if (isError) Color.Red else Color(0xFF2395d2),
    borderWidth: Dp = 2.dp,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    height : Dp = 45.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        singleLine = true,
        textStyle = TextStyle(fontSize = 17.sp, color = Color.Black),
        modifier = modifier,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
//                innerTextField = innerTextField,
                innerTextField = {
                    Box(
                        Modifier.defaultMinSize(minHeight = height),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                },
                placeholder = {
                    Text(text = placeholder, fontSize = 17.sp, color = Color.Gray)
                },
                supportingText = if (isError && errorMessage.isNotEmpty()) {
                    { Text(errorMessage, color = Color.Red, fontSize = 12.sp) }
                } else null,
                singleLine = true,
                enabled = true,
                isError = isError,
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = borderColor,
                    errorBorderColor = Color.Red,
                    disabledBorderColor = Color.LightGray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(horizontal = 8.dp),
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled = true,
                        isError = isError,
                        interactionSource = interactionSource,
                        shape = shape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = borderColor,
                            errorBorderColor = Color.Red,
                            disabledBorderColor = Color.LightGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        focusedBorderThickness = 2.dp,
                        unfocusedBorderThickness = borderWidth,
                    )
                }
            )
        }
    )
}
