package com.example.erp.android.apiServices

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.lms.Services.ApiServices
import com.example.lms.Services.Dataclass.AllCities
import com.example.lms.Services.Dataclass.AllCityCategories
import com.example.lms.Services.Dataclass.FuelTypes
import com.example.lms.Services.Dataclass.GetAllStates
import com.example.lms.Services.Dataclass.GetUserData
import com.example.lms.Services.Dataclass.GetVehicleDetails
import com.example.lms.Services.Dataclass.InsuranceTypes
import com.example.lms.Services.Dataclass.InsurerTypes
import com.example.lms.Services.Dataclass.PolicyRateData
import com.example.lms.Services.Dataclass.RenewalTypes
import com.example.lms.Services.Dataclass.SearchPolicyRatePayload
import com.example.lms.Services.Dataclass.UserData
import com.example.lms.Services.Dataclass.UserDetails
import com.example.lms.Services.Dataclass.VehicleTypes
import com.example.lms.Services.Dataclass.VerifyOTP
import com.example.lms.android.Services.Methods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

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

    private var _getPolicyRates = MutableStateFlow<List<PolicyRateData?>>(emptyList())
    val getPolicyRates: StateFlow<List<PolicyRateData?>> = _getPolicyRates


    suspend fun getAllPolicyRates(token: String): List<PolicyRateData?> {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getPolicyRates(token)
            }
            val response = deferredResponse.await()
            Log.d("policy Rates", response.data.toString())
            _getPolicyRates.value = response.data

            return response.data
        } catch (e: Exception) {
            Log.e(
                "policy Rates List error",
                e.message ?: "Unknown error"
            )
            listOf(null)
        }.toMutableList()

    }

    private var _getUserdata = MutableStateFlow<GetUserData?>(null)
    val getUserdata: MutableStateFlow<GetUserData?> = _getUserdata

    suspend fun getUserWhoLoggedIn(token: String): GetUserData? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getUserWhoLoggedIn(token)
            }
            val response = deferredResponse.await()
            Log.d("policy Rates", response.toString())
            // Update the MutableStateFlow's value
            _getUserdata.value = response
            response
        } catch (e: Exception) {
            Log.e(
                "get User Who logged in",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }
    }


    //FILTER OPTION LIST

    private var _getVehicleTypes = MutableStateFlow<VehicleTypes?>(null)
    val getVehicleTypes: MutableStateFlow<VehicleTypes?> = _getVehicleTypes

    suspend fun getAllVehicleTypes(token: String): VehicleTypes? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getVehicleTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("Vehicle Types", response.toString())
            _getVehicleTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get Vehicle Types",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }


    private var _getFuelTypes = MutableStateFlow<FuelTypes?>(null)
    val getFuelTypes: MutableStateFlow<FuelTypes?> = _getFuelTypes
    suspend fun getAllFuelTypes(token: String): FuelTypes? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getFuelTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("Fuel Types", response.toString())
            _getFuelTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get Fuel Types",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }
    }

    private var _getAllStates = MutableStateFlow<GetAllStates?>(null)
    val getAllStates: MutableStateFlow<GetAllStates?> = _getAllStates
    suspend fun getAllStates(token: String): GetAllStates? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllStates(token)
            }
            val response = deferredResponse.await()
            Log.d("Get All States", response.toString())
            _getAllStates.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get All States",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getAllCityCategory = MutableStateFlow<AllCityCategories?>(null)
    val getAllCityCategory: MutableStateFlow<AllCityCategories?> = _getAllCityCategory

    suspend fun getAllCityCategory(token: String): AllCityCategories? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllCityCategory(token)
            }
            val response = deferredResponse.await()
            Log.d("Get City Category", response.toString())
            _getAllCityCategory.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get All City Category",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }


    private var _getAllCities = MutableStateFlow<AllCities?>(null)
    val getAllCities: MutableStateFlow<AllCities?> = _getAllCities

    suspend fun getAllCities(token: String): AllCities? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllCities(token)
            }
            val response = deferredResponse.await()
            Log.d("Get All Cities", response.toString())
            _getAllCities.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get All Cities",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }


    private var _getAllInsuranceTypes = MutableStateFlow<InsuranceTypes?>(null)
    val getAllInsuranceTypes: MutableStateFlow<InsuranceTypes?> = _getAllInsuranceTypes

    suspend fun getAllInsuranceTypes(token: String): InsuranceTypes? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllInsuranceTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("Get All Cities", response.toString())
            _getAllInsuranceTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get All Cities",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getAllRenewalTypes = MutableStateFlow<RenewalTypes?>(null)
    val getAllRenewalTypes: MutableStateFlow<RenewalTypes?> = _getAllRenewalTypes

    suspend fun getAllRenewalTypes(token: String): RenewalTypes? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllRenewalTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("Get All Cities", response.toString())
            _getAllRenewalTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get All Cities",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getAllInsurerTypes = MutableStateFlow<InsurerTypes?>(null)
    val getAllInsurerTypes: MutableStateFlow<InsurerTypes?> = _getAllInsurerTypes


    suspend fun getAllInsurerTypes(token: String): InsurerTypes? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllInsurerTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("getAllInsurerTypes", response.toString())
            _getAllInsurerTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "getAllInsurerTypes",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getfilterPolicyRateData = MutableStateFlow<List<PolicyRateData?>>(emptyList())
    val getfilterPolicyRateData: MutableStateFlow<List<PolicyRateData?>> = _getfilterPolicyRateData

    suspend fun filterPolicyRateData(token: String, searchData: SearchPolicyRatePayload)
            : List<PolicyRateData?> {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().searchPolicyRateData(token, searchData)
            }
            val response = deferredResponse.await()
            Log.d("Success Filter Policy Rate Data", response.toString())
            _getfilterPolicyRateData.value = response.items
            return response.items
        } catch (e: Exception) {
            Log.e(
                "lter Policy Rate Data Errorr",
                e.message ?: "Unknown error"
            )
            listOf(null)
        }.toMutableList()
    }

    fun updatePolicyRates(filteredData: List<PolicyRateData?>) {
        // Logic to update the policyRatesList based on filterPolicyRateDatalist
        _getPolicyRates.value = filteredData // Assuming _policyRatesList is MutableStateFlow
    }

    private var _getRegistrationNumberFromImage = MutableStateFlow<String?>("")
    val getRegistrationNumberFromImage: StateFlow<String?> = _getRegistrationNumberFromImage

    // Mark the function as suspend
    suspend fun uploadImage(token: String, filePath: String): String? {
        // Prepare the URL
        val url = "https://sales-tool-api.1click.tech/ocr/vehicle_number"

        // Set logging interceptor for detailed request logs
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Configure OkHttpClient with increased timeouts and logging
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)  // Add logging interceptor to track requests
            .build()

        // Create a boundary for the multipart/form-data
        val boundary = "Boundary-${java.util.UUID.randomUUID()}"

        // Create the file reference
        val file = File(filePath)

        // Check if the file exists
        if (!file.exists()) {
            println("File does not exist at path: ${file.absolutePath}")
            return null
        }

        // Determine content type based on file extension
        val contentType = when (file.extension.lowercase()) {
            "png" -> "image/png"
            "jpeg" -> "image/jpeg"
            "jpg" -> "image/jpg"
            "gif" -> "image/gif"
            "heic" -> "image/heic"
            else -> "application/octet-stream" // Fallback for unknown types
        }

        // Create multipart form body
        val body = MultipartBody.Builder(boundary)
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", file.name,
                RequestBody.create(contentType.toMediaTypeOrNull(), file)
            )
            .build()

        // Create the request
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("accept", "application/json")
            .post(body)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                // Perform the network request
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseData = response.body?.string() ?: return@withContext null
                    println("Response: $responseData")

                    // Parse JSON to extract the result (assuming similar to the model)
                    val jsonResponse = JSONObject(responseData)
                    val result = jsonResponse.optString("result", null)
                    _getRegistrationNumberFromImage.value = result
                    return@withContext result
                } else {
                    println("Error during upload: ${response.message}")
                    return@withContext null
                }

            } catch (e: IOException) {
                println("Error during upload: ${e.localizedMessage}")
                return@withContext null
            }
        }
    }


    private var _getVehicleDetails = MutableStateFlow<GetVehicleDetails?>(null)
    val getVehicleDetails: MutableStateFlow<GetVehicleDetails?> = _getVehicleDetails

    suspend fun getVehicleDetails(token: String, vehicleNumber: String): String {
        return try {
            val response = withContext(Dispatchers.IO) {
                ApiServices().getVehicleDetails(token, vehicleNumber)
            }
            Log.d("Success getVehicleDetails", response.toString())
            _getVehicleDetails.value = response
            return response.status
        } catch (e: Exception) {
            Log.e("getVehicleDetails Error", e.message ?: "Unknown error")
            "null"
        }
    }

}