package com.example.erp.android.ui.screens.auth

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.erp.android.R
import com.example.erp.android.ui.graphs.AuthNav_Graph
import com.example.erp.android.ui.graphs.AuthScreen

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.Auth_Main(
    navController: NavHostController,
    authNavController: NavHostController,
    mainNavController: NavHostController,
    logo: String,
    cName: String,
    animatedVisibilityScope: AnimatedContentScope
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf("") }

    val context = LocalContext.current
    val customBodyLarge = TextStyle(
        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onBackground
    )


        ConstraintLayout(
            modifier = Modifier

                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(
                    horizontal = 16.dp
                ),

            ) {
            val (image, cname,
                parentCompany,
                description,
                loginBtn, signUpBtn) = createRefs()


            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        verticalBias = 1f
                        bottom.linkTo(cname.top, margin = 18.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxSize()
                    .sharedElement(
                        state = rememberSharedContentState(key = "logo"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }))
//            SubcomposeAsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .scale(Scale.FILL)
//                    .crossfade(true)
//                    .data("https://hellopolicy.com/images/1clickpolicy_logo.svg")
//                    .build(),
//                contentDescription = "Image",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .constrainAs(image) {
//                        top.linkTo(parent.top)
//                        verticalBias = 1f
//                        bottom.linkTo(cname.top, margin = 18.dp)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//                    .size(86.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .fillMaxSize()
//                    .sharedElement(
//                        state = rememberSharedContentState(key = "logo"),
//                        animatedVisibilityScope = animatedVisibilityScope,
//                        boundsTransform = { _, _ ->
//                            tween(durationMillis = 1000)
//                        }),
//                loading = {
//                    Box(
//                        Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .size(50.dp)
//                                .align(Alignment.Center),
//                            color = MaterialTheme.colorScheme.secondary
//                        )
//                    }
//                }
//            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(cname) {
                        top.linkTo(parent.top)
                        verticalBias = 0.5f
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .sharedElement(
                        state = rememberSharedContentState(key = "cName"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = cName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    "Simplifying Sales for Smarter Policy Agents.",
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Black,
                    style = customBodyLarge,
                    textAlign = TextAlign.Center
                )
            }
            OutlinedButton(
                onClick = {
                    page = AuthScreen.Login.route
                    showBottomSheet = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(loginBtn) {
                        bottom.linkTo(parentCompany.top, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                shape = RoundedCornerShape(8.dp),
                border = null,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor =
                MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = "Log in", color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 14.dp)
                )
            }
                //SIGN UP BUTTON
//            OutlinedButton(
//                    onClick = {
//                    page = AuthScreen.SignUp.route
//                    showBottomSheet = true
//                },
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
//                    .constrainAs(signUpBtn) {
//                        bottom.linkTo(parentCompany.top, margin = 24.dp)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    },
//            ) {
//                Text(
//                    text = "Sign up",
//                    color = Color.White,
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(vertical = 14.dp)
//                )
//            }

            Spacer(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                modifier = Modifier
                    .constrainAs(parentCompany) {
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = "A product of 1 Click Global family",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                dragHandle = null,
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.onBackground,
            ) {
                AuthNav_Graph(
                    navController,
                    authNavController,
                    route = page,
                    sheetState = sheetState,
                    scope = scope,
                    onDismiss = { showBottomSheet = false }
                )

            }
        }




}


