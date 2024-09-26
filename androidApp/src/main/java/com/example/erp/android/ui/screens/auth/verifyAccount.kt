package com.example.lms.android.ui.Screens.Auth

//@SuppressLint("StateFlowValueCalledInComposition")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Verify_Account(
//    navController: NavController,
//    sheetState: SheetState,
//    viewModel: ApiViewModel,
//    scope: CoroutineScope,
//    verifyType: String,
//    onDismiss: () -> Unit
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val phone=if(verifyType =="login")viewModel.userSpec.value?.mobileNumber
//                else viewModel.registeruserSpec.value?.mobileNumber
//    val email=if(verifyType =="login")viewModel.userSpec.value?.email
//    else viewModel.registeruserSpec.value?.email
//
//    val encrptMail = "${email?.take(4)}" +"XXXXXXX"+
//            "${email?.takeLast(4)} "
//
//    var sendOTPto by remember { mutableStateOf<String?>("") }
//    val verifyAccText = buildAnnotatedString {
//        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
//            append("We’ll send a verification code to either ")
//        }
//        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
//            append(encrptMail)
//        }
//        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
//            append("OR")
//        }
//        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
//            append(" XXXXXX"+phone?.drop(6)+" ")
//        }
//        withStyle(style = SpanStyle(color = Color(0xFF4E4E4E))) {
//            append("Please select the platform below where you want us to send the verification code.")
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxHeight(0.95f)
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        //drag handle
//        Log.d("Check Navigation","test")
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
//        //Back button
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(1f)
//                .padding(vertical = 16.dp)
//                .wrapContentHeight(),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.chevron_left_normal),
//                modifier = Modifier.clickable {
//                    navController.navigateUp()
//                },
//                contentDescription = "back Button"
//            )
//
//
//        }
//        Spacer(modifier = Modifier.weight(0.4f))
//        Image(
//            painter = painterResource(id = com.example.lms.R.drawable.tick),
//            contentDescription = "Verify Account Logo"
//        )
//        Spacer(modifier = Modifier.padding(vertical = 15.dp))
//        Text(
//            text = "Verify your account",
//            color = Color(0xFF4E4E4E),
//            style = MaterialTheme.typography.labelLarge
//        )
//
//        Spacer(modifier = Modifier.padding(vertical = 6.dp))
//
//        Text(
//            text = verifyAccText,
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.bodyMedium
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        CustBtn(text = "Get OTP on text message") {
//            coroutineScope.launch {
//                if (verifyType == "register") {
//                    sendOTPto = viewModel.registeruserSpec.value?.id
//                } else if (verifyType == "login") {
//                    sendOTPto = viewModel.userSpecs?.id.toString()
//                }
//                //Change the endpoint with Login with Phone number API
//                val result = sendOTPto?.let { viewModel.sendOTP(it) }
//                withContext(Dispatchers.Main) {
//                    if (result != null) {
//                        Toast.makeText(
//                            context, "OTP Sent Successfully", Toast.LENGTH_SHORT
//                        ).show()
////                      navController.navigate(AuthScreen.Verify_Account.route)
//                        navController.navigate(
//                            "${AuthScreen.Otp.route}/login/verifyNumber/$phone"
//                        )
//                    } else {
//                        Toast.makeText(
//                            context, " Failed Successfully", Toast.LENGTH_SHORT
//                        ).show()
////                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//        Spacer(modifier = Modifier.padding(vertical = 6.dp))
//
//        CustBtn(text = "Get OTP on Email ID") {
//            navController.navigate("${AuthScreen.Otp.route}/login/login/$phone")
//
//        }
//        Spacer(modifier = Modifier.padding(bottom = 48.dp))
//    }
//}