package com.example.erp.android.ui.screens.auth.Login

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.erp.android.R
import com.example.erp.android.apiServices.ApiViewModel
import com.example.erp.android.ui.component.CustBtn
import com.example.erp.android.ui.component.CustomTextInput
import com.example.erp.android.ui.component.SelectionView
import com.example.erp.android.ui.graphs.AuthScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun Get_OTP(
    navController: NavController,
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: ApiViewModel,
    isforgot: String,
    onDismiss: () -> Unit,
) {
    var selectedValueOTP by remember { mutableStateOf("") }
    val context = LocalContext.current
    var sendOTPto by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //title and Close btn And Drag Handle
        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(36.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.LightGray)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(vertical = 16.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.chevron_left_normal),
                modifier = Modifier.clickable {
                    navController.navigateUp()
                }, contentDescription = "back Button"
            )

            Text(
                text = if (isforgot == "loginOTP") "Log in with OTP"
                else if (isforgot == "verify Account") "Verify your Account"
//                else if(isforgot == "") "Get Password Reset Code"
                else "Get Password Reset Code",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(id = R.drawable.close_btn),
                modifier = Modifier.clickable {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                }, contentDescription = "Colse Button"
            )

        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "Please enter your credentials",
            color = Color(0xFF949494),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.padding(vertical = 3.dp))

        Text(
            text = "Below you can choose where you would like to receive your OTP for logging in.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.padding(vertical = 12.dp))

        val label = remember { mutableStateOf("Select Credential") }
        var isEmailSelected =
            SelectionView(label = label, isforgot)


        CustomTextInput(
            value = sendOTPto,
            isIcon = false,
            onValueChange = { sendOTPto = it },
            label = if (isEmailSelected) "Enter Email Id" else "Enter Phone Number",
            inputType = if (isEmailSelected) KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            else KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )


        if (selectedValueOTP != "") {
            CustomTextInput(
                value = sendOTPto,
                onValueChange = {
                    sendOTPto = it

                },
                label = selectedValueOTP,
                inputType = KeyboardOptions(
                    keyboardType = if (selectedValueOTP != "Number")
                        KeyboardType.Email else KeyboardType.Phone
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CustBtn(
            text = if (isforgot == "loginOTP") "Next"
            else if (isforgot == "verify Account") "Verify"
            else "Get Password Reset Code",
            isblue = true
        ) {
            scope.launch {
                if (isEmailSelected) {
//                    navController.navigate("${AuthScreen.OtpSent.route}/$isforgot/$sendOTPto")
//                    showPopup = !showPopup
                    if (isforgot == "forgot") {

                        val resetResult = viewModel.resetPassword(sendOTPto)
                        showPopup = (resetResult == "Success")
                        if(resetResult != "Success"){
                        Toast.makeText(context, resetResult, Toast.LENGTH_SHORT).show()
                        }
//                       Toast.makeText(context, "Yet to define forgot ", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Yet to define for Email (OTP)", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (isforgot == "forgot") {
                        showPopup = !showPopup
//                       Toast.makeText(context, "Yet to define forgot ", Toast.LENGTH_SHORT).show()
                    } else {
                        val result = viewModel.loginWithOTP(phone = sendOTPto)
                        withContext(Dispatchers.Main) {
                            if (result == true) {
                                Toast.makeText(
                                    context, "OTP Sent Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
//                                  navController.navigate(AuthScreen.Verify_Account.route)
//                          navController.navigate("${AuthScreen.Otp.route}/login/login/$sendOTPto")
                                navController.navigate("${AuthScreen.OtpSent.route}/$isforgot/$sendOTPto")

                            } else {
                                Toast.makeText(
                                    context, " Failed Something went wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(
                painter = painterResource(id = R.drawable.lock_01),
                contentDescription = "lock svg"
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                "Don’t worry, we won’t share your details anywhere.",
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .wrapContentWidth(),
                color = Color(0xFF4E4E4E),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )


        }

    }

    if (showPopup) {
        Popup(
            onDismissRequest = { showPopup = false }, // Close popup when clicked outside
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = false

            )
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.9f),
            ) {
                AnimatedPreloader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    context,
                    navController
                )

                androidx.compose.material3.Text(
                    text = "Reset Password Link Sent",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp, // Increase the font size to 24sp (you can adjust the size as needed)
                        fontWeight = FontWeight.Bold // Add bold style if desired
                    )
                )

                Text(
                    text = "Please check your inbox harish.chouhan@1click.tech ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp, // Increase the font size to 24sp (you can adjust the size as needed)
                        fontWeight = FontWeight.Light // Add bold style if desired
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            showPopup = false
                            navController.navigateUp()
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Okay",
                            color = Color.Blue,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Blue,
                                    shape = RoundedCornerShape(20),
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }


}


@Composable
fun AnimatedPreloader(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.animation
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = true,
    )

    LaunchedEffect(preloaderProgress) {
        if (preloaderProgress >= 1f) {
            Toast.makeText(context, "Mail sent", Toast.LENGTH_SHORT).show()
        }
    }


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}