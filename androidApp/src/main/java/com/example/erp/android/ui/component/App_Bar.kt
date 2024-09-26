package com.example.erp.android.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.erp.android.ERPTheme

@Composable
fun AppbarComponent(title:String){

 ERPTheme {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.onBackground)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1 ,
                color = Color.Black,
            )

        }
    }

}