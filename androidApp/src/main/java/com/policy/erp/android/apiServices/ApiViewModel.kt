package com.policy.erp.android.apiServices

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.policy.lms.Services.ApiServices
import com.policy.lms.Services.Dataclass.AllCities
import com.policy.lms.Services.Dataclass.AllCityCategories
import com.policy.lms.Services.Dataclass.FuelTypes
import com.policy.lms.Services.Dataclass.GeneralPolicyRateItem
import com.policy.lms.Services.Dataclass.GeneralPolicyRatePayload
import com.policy.lms.Services.Dataclass.GetAllStates
import com.policy.lms.Services.Dataclass.GetNotificationsResponse
import com.policy.lms.Services.Dataclass.GetVehicleDetails
import com.policy.lms.Services.Dataclass.InsuranceTypeUsingSegmentID
import com.policy.lms.Services.Dataclass.InsuranceTypes
import com.policy.lms.Services.Dataclass.InsurerByInsurerGrp
import com.policy.lms.Services.Dataclass.InsurerGroupResponse
import com.policy.lms.Services.Dataclass.InsurerTypes
import com.policy.lms.Services.Dataclass.InsurerX
import com.policy.lms.Services.Dataclass.PPTsTypesBySegmentId
import com.policy.lms.Services.Dataclass.PolicyRateData
import com.policy.lms.Services.Dataclass.PolicySegmentResponse
import com.policy.lms.Services.Dataclass.ProductResponse
import com.policy.lms.Services.Dataclass.RegisteredDeviceResponse
import com.policy.lms.Services.Dataclass.RenewalTypes
import com.policy.lms.Services.Dataclass.RenewalTypesBySegmentIdResponse
import com.policy.lms.Services.Dataclass.SearchPolicyRatePayload
import com.policy.lms.Services.Dataclass.SlabResponse
import com.policy.lms.Services.Dataclass.UserData
import com.policy.lms.Services.Dataclass.UserDetails
import com.policy.lms.Services.Dataclass.VehicleBrands
import com.policy.lms.Services.Dataclass.VehicleModels
import com.policy.lms.Services.Dataclass.VehicleTypes
import com.policy.lms.Services.Dataclass.VerifyOTP
import com.policy.lms.android.Services.Methods

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

val project_id = "0d98736c-5f90-41b4-b689-1b1935aab762"

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
            val userID=response.userData.id
            if (userID != null) {
                Methods().save_userID(userID, context)
                Log.d("User ID", userID)
            }
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
            val userID = response.userData.id
            if (userID != null) {
                Methods().save_userID(userID, context)
            }
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

    private var _getFilterRates = MutableStateFlow<List<PolicyRateData?>>(emptyList())
    val getFilterRates: StateFlow<List<PolicyRateData?>> = _getFilterRates

    private var _getFilterGeneralRates = MutableStateFlow<List<GeneralPolicyRateItem?>>(emptyList())
    val getFilterGeneralRates: StateFlow<List<GeneralPolicyRateItem?>> = _getFilterGeneralRates


    suspend fun getAllPolicyRates(
        token: String,
        context: Context,
        payload: SearchPolicyRatePayload,
        logout: () -> Unit
    ): List<PolicyRateData?> {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices()
                    .searchPolicyRateData(token, payload)
//                    .getPolicyRates(token)
            }
            val response = deferredResponse.await()
            Log.d("policy Rates", response.items.toString())
            _getPolicyRates.value = response.items

            return response.items
        } catch (e: Exception) {
            //Refresh Token is not working
            if (e.message == "401") {
                Toast.makeText(context, "Session time out please login again", Toast.LENGTH_LONG)
                    .show()
                logout()
            }
            Log.e(
                "policy Rates List error",
                e.message ?: "Unknown error"
            )
            listOf(null)
        }.toMutableList()
    }

    private var _getUserdata = MutableStateFlow<UserData?>(null)
    val getUserdata: MutableStateFlow<UserData?> = _getUserdata

    suspend fun getUserWhoLoggedIn(token: String, userId: String): UserData? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getUserWhoLoggedIn(token, userId)
            }
            val response = deferredResponse.await()
            Log.d("UserWhoLoggedIN", response.toString())
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

    private var _getVehicleBrand = MutableStateFlow<VehicleBrands?>(null)
    val getVehicleBrands: MutableStateFlow<VehicleBrands?> = _getVehicleBrand

    suspend fun getAllVehicleBrands(token: String): VehicleBrands? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getVehicleBrands(token)
            }
            val response = deferredResponse.await()
            Log.d("Vehicle Brands", response.toString())
            _getVehicleBrand.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get Vehicle Brands",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getVehicleModels = MutableStateFlow<VehicleModels?>(null)
    val getVehicleModels: MutableStateFlow<VehicleModels?> = _getVehicleModels

    suspend fun getAllVehicleModels(token: String): VehicleModels? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getVehicleModels(token)
            }
            val response = deferredResponse.await()
            Log.d("Vehicle Models", response.toString())
            _getVehicleModels.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get Vehicle Models",
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
            Log.d("Get InsuranceTypes", response.toString())
            _getAllInsuranceTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get InsuranceTypes",
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
            Log.d("Get RenewalTypes", response.toString())
            _getAllRenewalTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get RenewalTypes",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getAllRenewalTypesBySegmentId =
        MutableStateFlow<RenewalTypesBySegmentIdResponse?>(null)
    val getAllRenewalTypesBySegmentId: MutableStateFlow<RenewalTypesBySegmentIdResponse?> =
        _getAllRenewalTypesBySegmentId

    suspend fun fetchAllRenewalTypesBySegmentId(
        token: String,
        id: String
    ): RenewalTypesBySegmentIdResponse? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getRenewalTypeBySegmentId(id, token)
            }
            val response = deferredResponse.await()
            Log.d("Get AllRenewalTypesBySegmentId", response.toString())
            _getAllRenewalTypesBySegmentId.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get AllRenewalTypesBySegmentId",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    private var _getSlabTypesBySegmentId = MutableStateFlow<SlabResponse?>(null)
    val getSlabTypesBySegmentId: MutableStateFlow<SlabResponse?> = _getSlabTypesBySegmentId

    suspend fun fetchSlabTypesBySegmentId(token: String, id: String): SlabResponse? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getSlabTypesBySegmentId(id, token)
            }
            val response = deferredResponse.await()
            Log.d("Get SlabTypesBySegmentId", response.toString())
            _getSlabTypesBySegmentId.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get SlabTypesBySegmentId",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }


    private var _getInsurerByInsurerGrp = MutableStateFlow<InsurerByInsurerGrp?>(null)
    val getInsurerByInsurerGrp: MutableStateFlow<InsurerByInsurerGrp?> = _getInsurerByInsurerGrp

    suspend fun fetchInsurerByInsurerGrp(token: String, id: String):
            InsurerByInsurerGrp? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getInsurerByInsurerGrp(id, token)
            }
            val response = deferredResponse.await()
            Log.d("Get InsurerByInsurerGrp", response.toString())
            _getInsurerByInsurerGrp.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "Get InsurerByInsurerGrp",
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

    // Get Health Filters
// Slab Types
    private var _getAllSlabTypes = MutableStateFlow<SlabResponse?>(null)
    val getAllSlabTypes: MutableStateFlow<SlabResponse?> = _getAllSlabTypes

    suspend fun getAllSlabTypes(token: String): SlabResponse? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllSlabTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("getAllSlabTypes", response.toString())
            _getAllSlabTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "err getAllSlabTypes",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }

    }

    // Get Insurer Groups
    private var _getInsurerGroups = MutableStateFlow<InsurerGroupResponse?>(null)
    val getInsurerGroupsObj1: MutableStateFlow<InsurerGroupResponse?> = _getInsurerGroups

    suspend fun getInsurerGroups(token: String): InsurerGroupResponse? {
        return try {
            val response = ApiServices().getAllInsurerGroups(token)
            Log.d("get all InsurerGroups", response.toString())
            _getInsurerGroups.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "get InsurerGroups error",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }
    }

    // Policy Segments Api
    private var _getPolicySegments = MutableStateFlow<List<PolicySegmentResponse?>>(emptyList())
    val getPolicySegments: StateFlow<List<PolicySegmentResponse?>> = _getPolicySegments

    suspend fun getPolicySegments(token: String): List<PolicySegmentResponse>? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllPolicySegments(
                    token
                )
            }
            val response = deferredResponse.await()
            Log.d("getgetPolicySegments", response.toString())
            _getPolicySegments.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "getPolicySegments",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }
    }

    // Product Types Api
    private var _getProductTypes = MutableStateFlow<ProductResponse?>(null)
    val getProductTypes: MutableStateFlow<ProductResponse?> = _getProductTypes

    suspend fun getProductTypes(token: String): ProductResponse? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getAllProductTypes(token)
            }
            val response = deferredResponse.await()
            Log.d("getAllProductTypes", response.toString())
            _getProductTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "err getProductTypes",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }
    }

    // Insurance Type Using Policy Segment ID
    private var _getInsuranceTypeByID = MutableStateFlow<InsuranceTypeUsingSegmentID?>(null)
    val getInsuranceTypeByID: MutableStateFlow<InsuranceTypeUsingSegmentID?> = _getInsuranceTypeByID

    suspend fun getInsuranceTypeByPolicySegments(
        segmentId: String,
        token: String
    ): InsuranceTypeUsingSegmentID? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getInsuranceTypeByPolicySegments(segmentId, token)
            }
            val response = deferredResponse.await()
            Log.d("getInsuranceTypeByID", response.toString())
            _getInsuranceTypeByID.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "err getInsuranceTypeByID",
                e.message ?: "Unknown error"
            )
            null // Return null in case of an exception
        }
    }

    // PPTs Type Using Policy Segment ID
    private var _getPPtsTypes = MutableStateFlow<PPTsTypesBySegmentId?>(null)
    val getPPtsTypes: MutableStateFlow<PPTsTypesBySegmentId?> = _getPPtsTypes

    suspend fun getPPTsTypesBySegmentId(segmentId: String, token: String): PPTsTypesBySegmentId? {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getPPtsByPolicySegments(segmentId, token)
            }
            val response = deferredResponse.await()
            Log.d("getAllTypesBySegmentId", response.toString())
            _getPPtsTypes.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "err getPPtsTypes",
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
            val response =
                ApiServices().searchPolicyRateData(token, searchData)

            Log.d("Succexx Policy Rate Data", response.toString())
            _getfilterPolicyRateData.value = response.items
            return response.items
        } catch (e: Exception) {
            Log.e(
                "err filter Policy Rate Data Erorrr",
                e.message ?: "Unknown error"
            )
            listOf(null)
        }.toMutableList()
    }


    private var _getGeneralSearchData = MutableStateFlow<List<GeneralPolicyRateItem?>>(emptyList())
    val getGeneralSearchData: MutableStateFlow<List<GeneralPolicyRateItem?>> = _getGeneralSearchData

    suspend fun filterGeneralSearchData(token: String, searchData: GeneralPolicyRatePayload)
            : List<GeneralPolicyRateItem?> {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().searchGeneralPolicyRateData(token, searchData)
            }
            val response = deferredResponse.await()
            Log.d("getfilterPolicyRateData", response.toString())
            _getGeneralSearchData.value = response.items
            return response.items
        } catch (e: Exception) {
            Log.e(
                "err filter _getfilterPolicyRateData",
                e.message ?: "Unknown error"
            )
            listOf(null)
        }.toMutableList()
    }

    fun updatePolicyRates(filteredData: List<PolicyRateData?>) {
        // Logic to update the policyRatesList based on filterPolicyRateDatalist
        _getPolicyRates.value = filteredData // Assuming _policyRatesList is MutableStateFlow

    }

    fun updateFilterPolicyRates(filteredData: List<PolicyRateData?>) {
        // Logic to update the policyRatesList based on filterPolicyRateDatalist
        _getFilterRates.value = filteredData // Assuming _policyRatesList is MutableStateFlow
    }

    fun updateFilterGeneralRates(filteredData: List<GeneralPolicyRateItem?>) {
        // Logic to update the policyRatesList based on filterPolicyRateDatalist
        _getFilterGeneralRates.value = filteredData // Assuming _policyRatesList is MutableStateFlow
    }

    private var _getRegistrationNumberFromImage = MutableStateFlow<String?>("")
    val getRegistrationNumberFromImage: StateFlow<String?> = _getRegistrationNumberFromImage

    // Mark the function as suspend
    suspend fun uploadImage(token: String, filePath: String): String? {
        // Prepare the URL
//        val url = "https://sales-tool-api.1click.tech/ocr/vehicle_number"// UAT
        val url = "https://sales-tool-api.1clickpolicy.com/ocr/vehicle_number"

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
            e.message.toString()
        }
    }


    private var _deviceRegiestration = MutableStateFlow<RegisteredDeviceResponse?>(null)
    val deviceRegiestration: MutableStateFlow<RegisteredDeviceResponse?> = _deviceRegiestration

    suspend fun registerDeviceForNotification(
        token: String,
        projectId: String,
        userId: String,
        deviceToken: String
    ): String {
        return try {
            val response = withContext(Dispatchers.IO) {
                ApiServices().registerDeviceForNotification(
                    token,
                    projectId,
                    userId,
                    deviceToken
                )
            }
            _deviceRegiestration.value = response
            Log.d("registerDev200", response.toString())
            return "Success"
        } catch (e: Exception) {
            Log.e(
                "registerDeviceForNotification Error",
                e.localizedMessage ?: "Unknown error"
            )
            e.message.toString()
        }
    }


    private var _resetPassword = MutableStateFlow<String?>(null)
    val resetPassword: MutableStateFlow<String?> = _resetPassword

    suspend fun resetPassword(
        email: String,
    ): String {
        return try {
            val response = withContext(Dispatchers.IO) {
                ApiServices().resetPassword(
                    email
                )
            }
            Log.d("Success resetPassword Sent", response)
            _resetPassword.value = response
            return "Success"
        } catch (e: Exception) {
            Log.e(
                "reset Password Error",
                e.localizedMessage ?: "Unknown error"
            )
            e.message.toString()
        }
    }

    //  GET NOTIFICATION
    private var _getNotifications = MutableStateFlow<List<GetNotificationsResponse?>>(emptyList())
    val getNotifications: MutableStateFlow<List<GetNotificationsResponse?>> = _getNotifications

    suspend fun fetchNotifications(token: String)
            : List<GetNotificationsResponse?> {
        return try {
            val deferredResponse = viewModelScope.async {
                ApiServices().getNotifications(token, project_id)
            }
            val response = deferredResponse.await()
            Log.d("Succexx _getfilterPolicyRateData", response.toString())
            _getNotifications.value = response
            return response
        } catch (e: Exception) {
            Log.e(
                "filter _getfilterPolicyRateData",
                e.message ?: "Unknown error"
            )
            listOf(null)
        }.toMutableList()
    }

}