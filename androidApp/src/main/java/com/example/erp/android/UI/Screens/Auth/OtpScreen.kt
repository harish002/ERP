package com.example.lms.android.ui.Screens.Auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.erp.android.R
import com.example.erp.android.UI.Graphs.AuthScreen
import com.example.erp.android.UI.Graphs.Graph
import com.example.lms.Services.Dataclass.VerifyOTP
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.ui.Component.Cust_Btn
import com.example.lms.android.ui.Component.Otp_Field
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Otp_Screen(
    mainNavController: NavController,
    authnavController: NavController,
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: ApiViewModel,
    otptype: String,
    screentype: String,
    phone: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val otpText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
            append("Didn’t receive the OTP? ")
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
            append("Check your credentials again!")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var otpValue by remember { mutableStateOf("") }
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
                    authnavController.navigateUp()
                }, contentDescription = "back Button"
            )

            Text(
                text = if (otptype == "loginOTP") "Verify with OTP"
                else "Enter Your Code",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(id = R.drawable.chevron_left_normal),
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
            text = "Please enter the OTP sent to you",
            color = Color(0xFF4E4E4E),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.padding(vertical = 3.dp))

        Text(
            text = otpText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.padding(vertical = 12.dp))
        Otp_Field(otpText = otpValue) { value, otpInputFilled ->
            otpValue = value
        }

        Spacer(modifier = Modifier.weight(1f))

        Log.d("otptype value", otptype)
        Cust_Btn(
            text = if (screentype != "login" && otptype != "loginOTP") "Get Password Reset Code"
            else if (otptype == "forgot") "Reset Password"
            else "Get Started",
            isblue = true
        ) {
            if (otptype == "forgot") {
                authnavController.navigate(AuthScreen.Reset_Password.route)
            }else {
                scope.launch {
                    if (otptype == "loginOTP") {
                        val (status, result) = viewModel
                            .authenticateLoginOTP(phone, otpValue,context = context)
                        withContext(Dispatchers.Main) {
                            if (status) {
                                Toast.makeText(
                                    context, "Logged In Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if(viewModel.userSpec.value?.verifications?.isEmpty() == true){
                                    authnavController.navigate("${AuthScreen.Verify_Account.route}/login")
                                }else{
                                    mainNavController.navigate(Graph.MAIN) {
                                        popUpTo(Graph.AUTH) {
                                            inclusive = true
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context, result.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
//                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }

                        }

                    } else if (otptype == "verifyNumber") {
                        val result = viewModel.verifyOTP(requestbody = VerifyOTP(phone, otpValue))
                        withContext(Dispatchers.Main) {
                            if (result != null) {
                                if (result.isNotBlank()) {
                                    Toast.makeText(
                                        context, result,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    mainNavController.navigate(Graph.MAIN) {
                                        popUpTo(Graph.AUTH) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context, result.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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

}