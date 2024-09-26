package com.example.lms.android.Services

import android.annotation.SuppressLint
import android.content.Context

//Locally Saving the details/Tokens and Use it Overall in the Project
// Save token
class Methods{
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