package com.policy.lms.android.Services

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//Locally Saving the details/Tokens and Use it Overall in the Project
// Save token
class Methods {

    fun save_Token(token: String, context: Context) {
        clearToken(context)
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("AuthToken", token)
        editor.apply()
    }

    // Retrieve token
    fun retrieve_Token(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("AuthToken", null)
    }

    // Clear token
    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("AuthToken")
        editor.apply()
    }

    //save userID
    fun save_UserID(token: String, context: Context) {
        clearUserID(context)
        val sharedPreferences = context
            .getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("UserID", token)
        editor.apply()
    }

    // Retrieve userID
    fun retrieve_UserID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("UserID", null)
    }

    // Clear userID
    fun clearUserID(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("UserID")
        editor.apply()
    }

    // Save refresh token
    fun save_RefreshToken(context: Context, refreshToken: String) {
        clearRefreshToken(context)
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("RefreshToken", refreshToken)
        editor.apply()
    }



    // Retrieve refresh token
    fun retrieveRefreshToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("RefreshToken", null)
    }

    // Clear refresh token
    fun clearRefreshToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("RefreshToken")
        editor.apply()
    }
    ////////////////////
    fun save_userID(userID: String, context: Context) {
        clear_UserID(context)
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("UserId", userID)
        editor.apply()
    }

    // Retrieve token
    fun retrieve_userID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("UserId", null)
    }

    // Clear token
    fun clear_UserID(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("UserId")
        editor.apply()
    }

    //////////////////////////////
    fun save_DToken(token: String, context: Context) {
        clearDToken(context)
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("DeviceToken", token)
        editor.apply()
    }

    // Retrieve token
    fun retrieve_DToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("DeviceToken", null)
    }

    // Clear token
    fun clearDToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("DeviceToken")
        editor.apply()
    }

    /////////////////////////////
    // seconds to hrs function
    @SuppressLint("DefaultLocale")
    fun convertSecondsToHrs(seconds: Double): String {
        val hours = (seconds / 3600).toInt()
        val minutes = ((seconds % 3600) / 60).toInt()
//        val remainingSeconds = (seconds % 60).toInt()

        return String.format("%02d:%02d", hours, minutes)
    }

    //for Progress bar
    fun calculatePercentage(part: Int, whole: Int): Double {
        return if (whole > 0) {
            (part.toDouble() / whole) * 100
        } else {
            0.0 // Avoid division by zero
        }
    }
}

fun getfirstInstall(context: Context): Boolean? {
    val sharedPref = context.getSharedPreferences(
        "1clickpolicy sales tool",
        Context.MODE_PRIVATE
    ) ?: return null
    return sharedPref.getBoolean("firstInstall", true)
}

fun saveFirstInstall(context: Context) {
    val sharedPref =
        context.getSharedPreferences("1clickpolicy sales tool", Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putBoolean("firstInstall", false)
        commit()
    }
}


fun createLinearGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFEBF1FF),
        ),
        start = Offset(100f, 0f),
        end = Offset(700f, 0f)
    )
}



