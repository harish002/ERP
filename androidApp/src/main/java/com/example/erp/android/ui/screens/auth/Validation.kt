package com.example.erp.android.ui.screens.auth

import com.example.erp.android.apiServices.ApiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ERValidation {

    val scope = CoroutineScope(Dispatchers.IO)
    val viewModel = ApiViewModel()
    var job: Job? = null // for username check delay


    fun isValidText(value: String, label: String): Boolean {
        return when (label) {
            "Username" -> isValidUsername(value)
            "Email ID" -> isValidEmail(value)
            "Phone Number" -> isValidNumber(value)
            "Password" -> isValidpassword(value)
            else -> false
        }
    }

    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    fun isValidpassword(password: String): Boolean {
        return password.matches(Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$"))
    }


    fun String.isValidPinCode(): Boolean {
        val pinCodeRegex = Regex("^[1-9][0-9]{5}$")
        return matches(pinCodeRegex) && length == 6
    }

    fun isValidNumber(phone: String): Boolean {
        return phone.matches(Regex("^[0-9]{10}\$"))
    }

     fun isValidUsername(username: String): Boolean {
        return username.matches( Regex("^[A-Za-z][A-Za-z0-9_]{3,29}$"))
    }


}

