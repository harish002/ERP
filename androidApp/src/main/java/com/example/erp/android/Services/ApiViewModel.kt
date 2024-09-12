package com.example.lms.android.Services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.lms.Services.ApiServices
import com.example.lms.Services.Dataclass.UserData
import com.example.lms.Services.Dataclass.UserDetails
import com.example.lms.Services.Dataclass.VerifyOTP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

val project_id = "c319ab33-dbf1-45e7-b566-521cfecfb3e5"

class ApiViewModel : ViewModel() {
    var userSpecs: UserData? = null

    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    //Login API
    private var _userSpec = MutableStateFlow<UserData?>(null)
    val userSpec: MutableStateFlow<UserData?> =
        _userSpec //  Temp for getting validation of user[mail/ phone]

    @SuppressLint("SuspiciousIndentation")
    suspend fun login(user: UserDetails, context: Context): Pair<Boolean,
            Any?> = withContext(viewModelScope.coroutineContext) {
        try {
            val response = ApiServices().loginApi(user)
            val authToken = response.token
            Methods().save_Token(authToken, context)
            val authRefreshToken = response.refreshToken
            Methods().save_RefreshToken(context, authRefreshToken)
            val userData = UserData(
                id = response.userData.id,
                name = response.userData.name,
                username = response.userData.username,
                surname = response.userData.surname,
                email = response.userData.email,
                mobileNumber = response.userData.mobileNumber,
                gender = response.userData.gender,
                enabled = response.userData.enabled,
                superAdmin = response.userData.superAdmin,
                projectRoles = response.userData.projectRoles ?: emptyList(),
                departmentRoles = response.userData.departmentRoles ?: emptyList(),
                zones = response.userData.zones ?: emptyList(),
                departments = response.userData.departments ?: emptyList(),
                isAvailable = response.userData.isAvailable,
                createdDate = response.userData.createdDate,
                verifications = response.userData.verifications,
                notificationPreferences = response.userData.notificationPreferences,
                birthDate = response.userData.birthDate,
                note = response.userData.note,
                currentRole = response.userData.currentRole
            )
            userSpecs = userData

            _userSpec.value = userData

            println("UserData : ${userSpecs.toString()}")
            Log.d("User Data", userSpecs.toString())
            Pair(true, userSpecs)
        } catch (e: Exception) {
            println(e.message)
            e.message?.let { Log.d("login Error", it) }
            Pair(false, e.message)
        }
    }

    //LOGIN WITH OTP
    //send otp to number
    suspend fun loginWithOTP(phone: String): Boolean? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().loginWithPhone(phone)
            }
            val response = deferredResponse.await()
            Log.d("Sent OPT Data", response.toString())
            response
        } catch (e: Exception) {
            Log.e("Sent OPT Error", e.message ?: "Unknown error")
            null
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun authenticateLoginOTP(phone: String, otp: String, context: Context): Pair<Boolean,
            Any?> = withContext(viewModelScope.coroutineContext) {
        try {
            val response = ApiServices().authenticateLoginOTP(phone, otp)
            val authToken = response.token
            Methods().save_Token(authToken, context)
            val authRefreshToken = response.refreshToken
            Methods().save_RefreshToken(context, authRefreshToken)
            val userData = UserData(
                id = response.userData.id,
                name = response.userData.name,
                username = response.userData.username,
                surname = response.userData.surname,
                email = response.userData.email,
                mobileNumber = response.userData.mobileNumber,
                gender = response.userData.gender,
                enabled = response.userData.enabled,
                superAdmin = response.userData.superAdmin,
                projectRoles = response.userData.projectRoles ?: emptyList(),
                departmentRoles = response.userData.departmentRoles ?: emptyList(),
                zones = response.userData.zones ?: emptyList(),
                departments = response.userData.departments ?: emptyList(),
                isAvailable = response.userData.isAvailable,
                createdDate = response.userData.createdDate,
                verifications = response.userData.verifications,
                notificationPreferences = response.userData.notificationPreferences,
                birthDate = response.userData.birthDate,
                note = response.userData.note,
                currentRole = response.userData.currentRole
            )
            userSpecs = userData

            _userSpec.value = userData

            Log.d("Login With otp User Data", userSpecs.toString())
            Pair(true, userSpecs)
        } catch (e: Exception) {
            println(e.message)
            e.message?.let { Log.d("Login With otp Error", it) }
            Log.d("Login Status", e.message.toString())
            Pair(false, e.message ?: "Login With otp failed")
        }
    }


    //Send OTP
    suspend fun sendOTP(userid: String): Deferred<String?> {
        return viewModelScope.async {
            try {
                val response = ApiServices().setOTPApi(userid)
                Log.d("Sent OPT Data", response.toString())
                response.toString()
            } catch (e: Exception) {
                Log.e("Sent OPT Error", e.message ?: "Unknown error")
                null
            }
        }
    }

    //Check User Name availability
    suspend fun checkUsername(userName: String): Boolean? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().checkUsernameEndpoint(userName)
            }
            val response = deferredResponse.await()
            Log.d("check Username", response.toString())
            response
        } catch (e: Exception) {
            Log.e("Error Check username", e.message ?: "Unknown error")
            null
        }
    }

    //Check User Name availability
    suspend fun checkPhoneNumber(phone: String): Boolean? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().checkPhoneNumber(phone)
            }
            val response = deferredResponse.await()
            Log.d("Check Phone Number", response.toString())
            response
        } catch (e: Exception) {
            Log.e("Error Phone Number", e.message ?: "Unknown error")
            null
        }
    }

    //Check User Name availability
    suspend fun checkUserEmail(email: String): Boolean? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().checkEmail(email)
            }
            val response = deferredResponse.await()
            Log.d("check User Email", response.toString())
            response
        } catch (e: Exception) {
            Log.e("Error Check user email", e.message ?: "Unknown error")
            null
        }
    }

    //Verify User OTP
    suspend fun verifyOTP(requestbody: VerifyOTP): String? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().verifyOTP(requestbody)
            }
            val response = deferredResponse.await()
            Log.d("check verify OTP", response)
            response
        } catch (e: Exception) {
            Log.e("Error verify OTP", e.message ?: "Unknown error")
            null
        }
    }


}