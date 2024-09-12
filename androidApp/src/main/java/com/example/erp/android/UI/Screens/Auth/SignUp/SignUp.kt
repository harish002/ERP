package com.example.lms.android.ui.Screens.Auth.SignUp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erp.android.R
import com.example.erp.android.UI.Graphs.AuthScreen
import com.example.lms.Services.Dataclass.RegisterRequest
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.Services.project_id
import com.example.lms.android.ui.Component.Cust_Btn
import com.example.lms.android.ui.Component.CustomTextInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//@SuppressLint("CoroutineCreationDuringComposition")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SignupScreen(
//    navController: NavController,
//    sheetState: SheetState,
//    viewModel: ApiViewModel,
//    scope: CoroutineScope,
//    onDismiss: () -> Unit
//) {
//    var fname by remember { mutableStateOf("") }
//    var lname by remember { mutableStateOf("") }
//    var uname by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var repassword by remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//
//    var flag = fname.isNotBlank() && lname.isNotBlank()
//            && uname.isNotBlank() && phone.isNotBlank() && email.isNotBlank()
//            && password.isNotBlank() && repassword.isNotBlank()
//
//    val alreadyUserText = buildAnnotatedString {
//        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)) {
//            append("Already have an account? ")
//        }
//        pushStringAnnotation(tag = "LOGIN", annotation = "login")
//        withStyle(
//            style = SpanStyle(color = MaterialTheme.colorScheme.background)
//        ) {
//            append("Log in")
//        }
//    }
//    val termAndConditionText = buildAnnotatedString {
//        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)) {
//            append("By logging in I accept 1 Click Tech LMS ")
//        }
//        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
//            append("Terms of Service, Privacy Policy")
//        }
//        append(", and ")
//        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
//            append("Honor Code.")
//        }
//    }
//    val customBodyLarge = TextStyle(
//        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
//        fontSize = 12.sp,
//        lineHeight = 20.sp,
//        color = MaterialTheme.colorScheme.onBackground
//    )
//
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxHeight(0.95f)
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        //title and Close btn And Drag Handle
//        Row(
//            modifier = Modifier
//                .padding(vertical = 5.dp)
//                .fillMaxWidth(1f),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .height(5.dp)
//                    .width(36.dp)
//                    .clip(RoundedCornerShape(3.dp))
//                    .background(Color.LightGray)
//            )
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(1f)
//                .wrapContentHeight(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(
//                onClick = { },
//                enabled = false
//            ) {}
//            Text(
//                text = "Sign Up",
//                textAlign = TextAlign.Start,
//                style = MaterialTheme.typography.titleLarge
//            )
//            Image(
//                painter = painterResource(id = R.drawable.chevron_left_normal),
//                modifier = Modifier.clickable {
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (!sheetState.isVisible) {
//                            onDismiss()
//                        }
//                    }
//                }, contentDescription = "Colse Button"
//            )
//
//        }
//        //Body
//        Row(
//            modifier = Modifier
//                .background(Color.Transparent)
//                .fillMaxWidth(1f),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            CustomTextInput(
//                label = "First Name",
//                value = fname,
//                inputType = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next
//                ),
//                modifier = Modifier.fillMaxWidth(0.5f),
//                onValueChange = { fname = it },
//                isSignUp = true,
//                isIcon = false
//
//            )
//
//            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
//
//            CustomTextInput(
//                label = "Last Name",
//                value = lname,
//                inputType = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next
//                ),
//                modifier = Modifier.fillMaxWidth(1f),
//                onValueChange = { lname = it },
//                isSignUp = true,
//                isIcon = false
//            )
//
//        }
//
//
//        CustomTextInput(
//            label = "Username",
//            isIcon = true,
//            inputType = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Next
//            ),
//            value = uname, onValueChange = {
//                uname = it
//            },
////                showError = isUsernameValid != true ,
//            isSignUp = true
//        )
//        Row(
//            modifier = Modifier
//                .background(Color.Transparent)
//                .fillMaxWidth(1f),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .fillMaxWidth(0.26f)
//                    .requiredHeight(IntrinsicSize.Max)
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(Color.Transparent)
//                    .border(1.dp, Color(0xFFD0D0D0), RoundedCornerShape(8.dp))
//                    .align(Alignment.Bottom)
//                    .padding(19.dp),
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        modifier = Modifier.size(20.dp),
//                        painter = painterResource(id = R.drawable.india_flag),
//                        contentDescription = null,
//                    )
//                    Text(" +91")
//                }
//            }
//            Spacer(modifier = Modifier.padding(5.dp))
//            CustomTextInput(
//                label = "Phone Number",
//                inputType = KeyboardOptions(
//                    keyboardType = KeyboardType.Phone,
//                    imeAction = ImeAction.Next
//                ),
//                value = phone,
//                onValueChange = { phone = it },
//                isSignUp = true
//            )
//        }
//        CustomTextInput(
//            label = "Email ID", value = email,
//            inputType = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            onValueChange = { email = it },
//            isSignUp = true
//        )
//        CustomTextInput(
//            label = "Password",
//            value = password,
//            inputType = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Next
//            ),
//            onValueChange = {
//                password = it
//            },
//            isSignUp = true
//        )
//        CustomTextInput(
//            label = "Re-Password",
//            inputType = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Next
//            ),
//            value = repassword,
//            passwordField = true,
//            onValueChange = { repassword = it },
//            isSignUp = true,
//            isIcon = false
//        )
//        if (password != repassword && repassword.isNotBlank()
//        ) {
//            Text(
//                text = "The password you’ve entered doesn’t match. Try again.",
//                modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.5.dp),
//                color = Color.Red
//            )
//        }
//
//        Spacer(modifier = Modifier.padding(vertical = 6.dp))
//
//        Cust_Btn(text = "Sign Up", isblue = true, isEnable = flag) {
//            scope.launch {
//                val deferredRegister = async {
//                    viewModel.register(
//                        RegisterRequest(
//                            username = uname,
//                            password = password,
//                            name = fname,
//                            surname = lname,
//                            email = email,
//                            mobileNumber = phone,
//                            gender = "MALE",
//                            optionalProjectRoles = listOf(""),
//                            optionalDepartmentRoles = listOf(""),
//                            projectId = project_id
//                        )
//                    ).await()
//                }
//
//                val result = deferredRegister.await()
//
//                if (result != null) {
//                    Toast.makeText(
//                        context, "Registration Is Successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    navController.navigate("${AuthScreen.Verify_Account.route}/register")
//                } else {
//                    Toast.makeText(
//                        context, "Registration Failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//            }
//        }
//        Text(
//            text = alreadyUserText,
//            modifier = Modifier
//                .padding(top = 12.dp)
//                .clickable { navController.navigate(AuthScreen.Login.route) },
//            color = MaterialTheme.colorScheme.onSecondary,
//            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
//            fontSize = 14.sp
//        )
//
//        Spacer(modifier = Modifier.padding(vertical = 12.dp))
//        Spacer(modifier = Modifier.weight(1f))
//
//        Text(
//            termAndConditionText,
//            modifier = Modifier
//                .padding(vertical = 16.dp)
//                .fillMaxWidth(),
//            style = customBodyLarge,
//            textAlign = TextAlign.Center
//        )
//    }
//}


//--------------------//Country code selection

//var phoneNumber: String by rememberSaveable { mutableStateOf("") }
//var fullPhoneNumber: String by rememberSaveable { mutableStateOf("") }
//var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }
//
//TogiCountryCodePicker(
//modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
//onValueChange = { (code, phone), isValid ->
//    Log.d("CCP", "onValueChange: $code $phone -> $isValid")
//
//    phoneNumber = phone
//    fullPhoneNumber = code + phone
//    isNumberValid = isValid
//},
//label = { Text("Phone Number") },
//)
