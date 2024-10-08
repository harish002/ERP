package com.example.lms.android.ui.Screens.Auth

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.erp.android.apiServices.ApiViewModel
import com.example.erp.android.ui.component.CustBtn
import com.example.erp.android.ui.graphs.AuthScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Verify_Account(
    navController: NavController,
    sheetState: SheetState,
    viewModel: ApiViewModel,
    scope: CoroutineScope,
    verifyType: String,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val phone=if(verifyType =="login")viewModel.userSpec.value?.mobileNumber
                else //ERP sales tool don't have the any
                    ""
//                    viewModel.registeruserSpec.value?.mobileNumber
    val email=if(verifyType =="login")viewModel.userSpec.value?.email
    else
        ""
//        viewModel.registeruserSpec.value?.email

    val encrptMail = "${email?.take(4)}" +"XXXXXXX"+
            "${email?.takeLast(4)} "

    var sendOTPto by remember { mutableStateOf<String?>("") }
    val verifyAccText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
            append("We’ll send a verification code to either ")
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
            append(encrptMail)
        }
        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
            append("OR")
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
            append(" XXXXXX"+phone?.drop(6)+" ")
        }
        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
            append("Please select the platform below where you want us to send the verification code.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //drag handle
        Log.d("Check Navigation","test")
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
        //Back button
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(vertical = 16.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.chevron_left_normal),
                modifier = Modifier.clickable {
                    navController.navigateUp()
                },
                contentDescription = "back Button"
            )


        }
        Spacer(modifier = Modifier.weight(0.4f))
        Image(
            painter = painterResource(id = R.drawable.tick),
            contentDescription = "Verify Account Logo"
        )
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "Verify your account",
            color = Color(0xFF4E4E4E),
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.padding(vertical = 6.dp))

        Text(
            text = verifyAccText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        CustBtn(text = "Get OTP on text message") {
            coroutineScope.launch {
                // Sence
//                if (verifyType == "register") {
//                    sendOTPto = viewModel.registeruserSpec.value?.id
//                } else
                    if (verifyType == "login") {
                    sendOTPto = viewModel.userSpecs?.id.toString()
                }
                //Change the endpoint with Login with Phone number API
                val result = sendOTPto?.let { viewModel.sendOTP(it) }
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        Toast.makeText(
                            context, "OTP Sent Successfully", Toast.LENGTH_SHORT
                        ).show()
//                      navController.navigate(AuthScreen.Verify_Account.route)
                        navController.navigate(
                            "${AuthScreen.Otp.route}/login/verifyNumber/$phone"
                        )
                    } else {
                        Toast.makeText(
                            context, " Failed Successfully", Toast.LENGTH_SHORT
                        ).show()
//                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 6.dp))

        CustBtn(text = "Get OTP on Email ID") {
            navController.navigate("${AuthScreen.Otp.route}/login/login/$phone")

        }
        Spacer(modifier = Modifier.padding(bottom = 48.dp))
    }
}