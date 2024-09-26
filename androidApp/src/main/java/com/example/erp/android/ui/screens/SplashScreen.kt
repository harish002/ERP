package com.example.erp.android.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.erp.android.ui.graphs.Graph
import com.example.lms.android.Services.Methods
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SharedTransitionScope.CustSplashScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    ){
        val context= LocalContext.current
        val cLogo = "https://picsum.photos/300/300"
        val cName = "1 Click Policy ERP"
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        ){
        val (image, cname,parentCompany,description) = createRefs()

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .scale(Scale.FILL)
                .crossfade(true)
                .data(cLogo)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxSize()
                .sharedElement(
                    state = rememberSharedContentState(key = "logo"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                )
            ,
            loading = {
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.secondary
                    )
                }
            }
        )

        Text(text = cName,
            modifier = Modifier
                .constrainAs(cname) {
                    top.linkTo(image.bottom,24.dp)
                    start.linkTo(image.start)
                    end.linkTo(image.end)
                }
                .fillMaxWidth()
                .sharedElement(
                    state = rememberSharedContentState(key = "cName"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(text = "",
            modifier = Modifier
                .fillMaxWidth()

                .constrainAs(description) {
                    top.linkTo(cname.bottom,24.dp)
                    start.linkTo(image.start)
                    end.linkTo(image.end)
                }
                .sharedElement(
                    state = rememberSharedContentState(key = "des"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

            Text(modifier = Modifier
                .constrainAs(parentCompany) {
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                text = "A product of 1 Click Global family",
                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

    }

    val coroutineScope = rememberCoroutineScope()
    val encodedLogo = Uri.encode(cLogo)

    coroutineScope.launch {
//        delay(2000) // Wait for 2 seconds
        if(Methods().retrieve_Token(context = context)?.isNotBlank() == true){
            navController.navigate(Graph.MAIN)
        }else{
        navController.navigate("AuthScreen/$encodedLogo/$cName")
    }
    }
}