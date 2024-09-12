package com.example.lms.android.ui.Component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.erp.android.R

@Composable
fun Cust_Btn(
    isSplit:Boolean= false,
    isblue:Boolean = false,
    text:String,
    isEnable:Boolean =true,
    isleadingIcon:Boolean=false,
    leadingIcon:Int = R.drawable.phone_02,
    onClick : () -> Unit
    ){
    //for custom shadow/ spread effect
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
    //for runtime error solving
    val alphaValue = if (isblue) 0.05f else 0.03f
    val color = Color(0xFF000000).copy(alpha = alphaValue)

    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        enabled = isEnable,
        colors = ButtonDefaults.buttonColors(containerColor =
            if(!isblue) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.background,

            disabledContainerColor =  if(!isblue) MaterialTheme.colorScheme.onBackground
                .copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
            ),
        modifier = Modifier
            .fillMaxWidth(if(isSplit)0.5f else 1f)
            .border(
                width = 1.dp,
                color = if(isEnable){
                    if (isblue) MaterialTheme.colorScheme.background
                    else Color(0xFFD0D0D0)
                }else{
                    if(isblue) MaterialTheme.colorScheme.onBackground
                        .copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(
                color = color,
                borderRadius = 8.dp,
                blurRadius = 0.dp,
                offsetY = 0.dp,
                offsetX = 0.dp,
                spread = 4.dp
            )
    ) {
        if(isleadingIcon){
            Icon(
                painter = painterResource(id = leadingIcon),
                tint = MaterialTheme.colorScheme.background,
                contentDescription = "login with otp Icon"
            )

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        }
        Text(
            text = text,
            color = if(isblue) Color.White  else MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(vertical = 14.dp)
                .background(color = Color.Transparent)
        )
    }

}