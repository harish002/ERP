package com.example.lms.android.ui.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.erp.android.R
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.ui.Screens.Auth.LMSvalidation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Composable
fun CustomTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    viewModel: ApiViewModel= ApiViewModel(),
    inputType: KeyboardOptions,
    passwordField: Boolean = false,
    isIcon: Boolean = true,
    isSignUp: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.colors(
        cursorColor = Color.Black,
        disabledLabelColor = Color(0xFF949494),
        focusedLabelColor = Color(0xFF4E4E4E),
        unfocusedTextColor = Color(0xFF949494),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.background,
        unfocusedIndicatorColor = Color(0xFFD0D0D0)
    )
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>("") }
    var isEmailAvailable by remember { mutableStateOf(false) }
    var isPhoneNumAvailable by remember { mutableStateOf(false) }
    var isUsernameAvailable by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Loading state for username check
    var job by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()



    Column {
        OutlinedTextField(
            keyboardOptions = inputType,
            value = value,
            shape = RoundedCornerShape(20),
            onValueChange = {
                onValueChange(it)
                isValid = LMSvalidation().isValidText(it, label)
                if(label == "Username"){
                    if(isValid){
                        isLoading = true
                        scope.launch {
                            isUsernameAvailable = viewModel.checkUsername(it) == true
                            isValid =
                                isUsernameAvailable == true // Update isValid based on the response
                            isLoading = false // Stop loading after getting the response
                        }
                    }
                }
                if(label == "Phone Number"){
                    if(isValid){
                    isLoading = true
                     scope.launch {
                        isPhoneNumAvailable = viewModel.checkPhoneNumber(it) == true
                        isValid = isPhoneNumAvailable == true // Update isValid based on the response
                        isLoading = false // Stop loading after getting the response
                    } }
                }
                if(label == "Email ID"){
                    if(isValid){
                        isLoading = true
                        scope.launch {
                            isEmailAvailable = viewModel.checkUserEmail(it) == true
                            isValid =
                                isEmailAvailable == true // Update isValid based on the response
                            isLoading = false // Stop loading after getting the response

                        }
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown && event.key == Key.Enter) {
                        keyboardController?.hide() // Hide the keyboard on Enter key press
                        true
                    } else {
                        false
                    }
                },
            label = {
                Text(
                    text = label,
                    color = Color(0xFF949494),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            visualTransformation = if (passwordField && !passwordVisibility) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                if (value.isNotBlank()) {
                    if (passwordField) {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility }
                        ) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    }

                    if (isIcon) {
                        if (label == "Password") {
                            Image(
                                painter = if (isValid) painterResource(R.drawable.chat_radio_button)
                                else painterResource(R.drawable.password_validate),
                                contentDescription = if (isValid) "Password is valid"
                                else "Password is invalid"
                            )
                        }
                        else if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Gray,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Image(
                                painter = if (isValid) painterResource(R.drawable.chat_radio_button)
                                else painterResource(R.drawable.error_field),
                                contentDescription = if (isValid) "Is Okay Check Icon"
                                else "Field not available"
                            )
                        }
                    }
                }
            },
            colors = colors,
        )
        // Show error message if there is an error
        if (value.isNotBlank() && !isValid && isSignUp) {
                 errorMessage = when (label) {
                "Username" -> {
                                if (!LMSvalidation().isValidUsername(value)) {
                                    "Invalid Format"
                                } else if (!isUsernameAvailable) {
                                    "This Usename is already Taken"
                                } else {
                                    "This Usename is already Taken"
                                }
                            }
                "Email ID" ->{
                                if (!LMSvalidation().isValidEmail(value)) {
                                    "Invalid Email Format"
                                } else if (!isEmailAvailable) {
                                    "This Email is already Used In Different Account"
                                } else {
                                    "This Email is already Used In Different Account"
                                }
                }
                "Phone Number"  -> {
                                if (!LMSvalidation().isValidNumber(value)) {
                                    "Invalid Number"
                                } else if (!isPhoneNumAvailable) {
                                    "This Number is already Used"
                                } else {
                                    "This Number is already Used"
                                }
                            }
                "Password" -> "Improve your password using 8-20 characters, at least 1 number, 1 letter and 1 special character."
                else -> null
            }

            errorMessage?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.5.dp),
                    color = Color.Red
                )
            }
        }
    }
}






