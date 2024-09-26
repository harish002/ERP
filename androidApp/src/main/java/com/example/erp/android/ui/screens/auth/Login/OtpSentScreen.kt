package com.example.erp.android.ui.screens.auth.Login

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.erp.android.R
import com.example.erp.android.ui.graphs.AuthScreen
import com.example.erp.android.ui.component.CustBtn
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpSent_Screen(
    navController: NavController,
    sheetState: SheetState,
    otptype: String,
    scope: CoroutineScope,
    phone:String,
    onDismiss: () -> Unit
){
    val verifyAccText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
            append("We’ve sent the verification code to ")
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
            append("XXXXXX"+phone.drop(6)+" ")
        }
        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))){
            append("Do check for the code and enter it on the next screen.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        //drag handle
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
            Image(painter = painterResource(id = R.drawable.chevron_left_normal),
                modifier = Modifier.clickable {
                    navController.navigateUp()
                }, contentDescription = "back Button"
            )


        }
        Spacer(modifier = Modifier.weight(0.4f))
        Image(
            painter = painterResource(id = R.drawable.otpsent),
            contentDescription = "OTP sent Logo"
        )
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(text = "We’ve sent the code",
            color = Color(0xFF4E4E4E),
            style = MaterialTheme.typography.labelLarge)

        Spacer(modifier = Modifier.padding(vertical = 6.dp))

        Text(text = verifyAccText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.weight(1f))

        CustBtn(text = "Got the verification code") {
            navController.navigate("${AuthScreen.Otp.route}/login/$otptype/$phone")

        }
        Spacer(modifier = Modifier.padding(vertical = 6.dp))

        Text(
            "I didn’t get it.",
            modifier = Modifier
                .padding(vertical = 16.dp)
                .wrapContentWidth()
                .clickable { navController.navigate(AuthScreen.AccNot_Found.route) },
            color = Color(0xFF4E4E4E),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(bottom = 48.dp))
    }
}