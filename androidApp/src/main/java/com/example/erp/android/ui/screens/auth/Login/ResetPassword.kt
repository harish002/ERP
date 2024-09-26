package com.example.erp.android.ui.screens.auth.Login

import android.annotation.SuppressLint
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.erp.android.R
import com.example.erp.android.ui.component.CustBtn
import com.example.erp.android.ui.component.CustomTextInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")


@Composable
fun Reset_Password(
    navController: NavController,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDismiss: () -> Unit,
) {
    var createPassword by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title and Close button and Drag Handle
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
                },
                contentDescription = "Back Button"
            )

            Text(
                text = "Reset Your Password",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge
            )

            Image(
                painter = painterResource(id = R.drawable.close_button),
                modifier = Modifier.clickable {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                },
                contentDescription = "Close Button"
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        Text(
            text = "Please set a new password.",
            color = Color(0xFF4E4E4E),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.padding(vertical = 3.dp))

        Text(
            text = "Set password using 8-20 characters, at least 1 number, 1 letter and 1 special character.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.padding(vertical = 12.dp))

        CustomTextInput(
            isIcon = false,
            value = createPassword,
            onValueChange = { createPassword = it },
            label = "Create password",
            inputType = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        CustomTextInput(
            isIcon = false,
            value = rePassword,
            onValueChange = {
                rePassword = it

            },
            label = "Re-enter password",
            inputType = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        CustBtn(
            text = "Reset Password",
            isblue = true,
            isEnable = createPassword == rePassword
        ) {
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.lock_01),
                contentDescription = "Lock Icon"
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


//@Preview
//@Composable
//fun prevReset(){
//     Reset_Password(
//        rememberNavController(),
//         rememberCoroutineScope(),
//         {  },
//    )
//}
