package com.example.lms.android.ui.Screens.Auth.Login

import android.graphics.BlurMaskFilter
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erp.android.R
import com.example.erp.android.UI.Graphs.AuthScreen
import com.example.erp.android.UI.Graphs.Graph
import com.example.lms.Services.Dataclass.UserDetails
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.ui.Component.Cust_Btn
import com.example.lms.android.ui.Component.CustomTextInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login_Screen(
    navController: NavController,
    authnavController: NavController,
    viewModel: ApiViewModel,
    sheetState: SheetState, scope: CoroutineScope, onDismiss: () -> Unit
){
    fun Modifier.shadow(
        color: Color = Color.Black,
        borderRadius: Dp = 0.dp,
        blurRadius: Dp = 0.dp,
        offsetY: Dp = 0.dp,
        offsetX: Dp = 0.dp,
        spread: Dp = 0f.dp,
        modifier: Modifier = Modifier
    ) = this.then(
        modifier.drawBehind {
            this.drawIntoCanvas {
                val paint = Paint()
                val frameworkPaint = paint.asFrameworkPaint()
                val spreadPixel = spread.toPx()
                val leftPixel = (0f - spreadPixel) + offsetX.toPx()
                val topPixel = (0f - spreadPixel) + offsetY.toPx()
                val rightPixel = (this.size.width + spreadPixel)
                val bottomPixel = (this.size.height + spreadPixel)

                if (blurRadius != 0.dp) {
                    frameworkPaint.maskFilter =
                        (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
                }

                frameworkPaint.color = color.toArgb()
                it.drawRoundRect(
                    left = leftPixel,
                    top = topPixel,
                    right = rightPixel,
                    bottom = bottomPixel,
                    radiusX = borderRadius.toPx(),
                    radiusY = borderRadius.toPx(),
                    paint
                )
            }
        }
    )

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var uname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val flag = uname.isNotBlank() && password.isNotBlank()
    val termAndConditionText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)){
            append("By logging in I accept 1 Click Tech LMS ")
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)){
            append("Terms of Service, Privacy Policy")
        }
        append(", and ")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)) {
            append("Honor Code.")
        }
    }

    val customBodyLarge = TextStyle(
        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onBackground
    )

    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .padding(horizontal = 16.dp)
            .imePadding()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //title and Close btn and drag handle
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
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { },
                enabled = false
            ) {}
            Text(
                text = "Log in",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge
            )
            Image(painter = painterResource(id = R.drawable.union_1),
                modifier = Modifier.clickable {
                    scope.launch {sheetState.hide()}.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                }, contentDescription = "Close Button"
            )
        }
        Spacer(modifier = Modifier.padding(top = 36.dp))
        //Body
        CustomTextInput(label = "Username",
            isIcon = false,
            inputType = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = uname, onValueChange = { uname = it })

        CustomTextInput(label = "Password", value = password,
            isIcon = false,
            passwordField = true,
            inputType = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            onValueChange = { password = it })

        Spacer(modifier = Modifier.padding(vertical = 12.dp))


        Cust_Btn(text = "Log In", isblue = true, isEnable = flag) {
            coroutineScope.launch {
                val (status, result) = viewModel
                    .login(UserDetails(username = uname, password = password,), context = context)
                withContext(Dispatchers.Main) {
                    if (status) {
                        Toast.makeText(
                            context, "Logged In Successfully IT Should navigate to VerifyAccount",
                            Toast.LENGTH_SHORT
                        ).show()
                        if(viewModel.userSpec.value?.verifications?.isEmpty() == true){
                            authnavController.navigate("${AuthScreen.Verify_Account.route}/login")
                        }else{
                            navController.navigate(Graph.MAIN) {
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
            }
        }

        Text(
            text = "Forgot Username or Password?",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    authnavController.navigate("${AuthScreen.Get_OTP.route}/forgot")

                },
            color = MaterialTheme.colorScheme.background,
            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
            fontSize = 14.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.left_line),
                contentDescription = "left line"
            )
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.labelSmall
            )
            Image(
                painter = painterResource(id = R.drawable.right_line),
                contentDescription = "left line"
            )
        }
        Cust_Btn(text = "Log In with OTP",
            isleadingIcon = true) {

            authnavController.navigate("${AuthScreen.Get_OTP.route}/loginOTP")
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            termAndConditionText,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            style = customBodyLarge,
            textAlign = TextAlign.Center
        )

    }
}



