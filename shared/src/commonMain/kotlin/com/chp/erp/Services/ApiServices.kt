package com.example.lms.Services

import com.chp.lms.Services.Dataclass.AllCities
import com.chp.lms.Services.Dataclass.AllCityCategories
import com.chp.lms.Services.Dataclass.FailedResponse
import com.chp.lms.Services.Dataclass.FuelTypes
import com.chp.lms.Services.Dataclass.GeneralPolicyRatePayload
import com.chp.lms.Services.Dataclass.GeneralPolicyRateResponse
import com.chp.lms.Services.Dataclass.GetAllStates
import com.chp.lms.Services.Dataclass.GetNotificationsResponse
import com.chp.lms.Services.Dataclass.GetPolicyRates
import com.chp.lms.Services.Dataclass.GetRegistrationNumberResponse
import com.chp.lms.Services.Dataclass.GetVehicleDetails
import com.chp.lms.Services.Dataclass.InsuranceTypeUsingSegmentID
import com.chp.lms.Services.Dataclass.InsuranceTypes
import com.chp.lms.Services.Dataclass.InsurerGroupResponse
import com.chp.lms.Services.Dataclass.InsurerTypes
import com.chp.lms.Services.Dataclass.PPTsTypesBySegmentId
import com.chp.lms.Services.Dataclass.PolicyRateData
import com.chp.lms.Services.Dataclass.PolicySegmentResponse
import com.chp.lms.Services.Dataclass.ProductResponse
import com.chp.lms.Services.Dataclass.RefreshToken
import com.chp.lms.Services.Dataclass.RegisteredDeviceResponse
import com.chp.lms.Services.Dataclass.RenewalTypes
import com.chp.lms.Services.Dataclass.RenewalTypesBySegmentIdResponse
import com.chp.lms.Services.Dataclass.SearchPolicyRateData

import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer

import com.chp.lms.Services.Dataclass.SearchPolicyRatePayload
import com.chp.lms.Services.Dataclass.SlabResponse
import com.chp.lms.Services.Dataclass.UserData
import com.chp.lms.Services.Dataclass.UserDetails
import com.chp.lms.Services.Dataclass.UserResponse
import com.chp.lms.Services.Dataclass.VehicleBrandTypes
import com.chp.lms.Services.Dataclass.VehicleModels
import com.chp.lms.Services.Dataclass.VehicleTypes
import com.chp.lms.Services.Dataclass.VerifyOTP
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import kotlin.coroutines.cancellation.CancellationException


sealed class ApiException(message: String) : IOException(message) {
    class TimeoutException : ApiException("Request timed out")
    class NoInternetException : ApiException("No internet connection")
    class UnknownException(message: String) : ApiException(message)
}

@Serializable
class ApiConfig {
    companion object {

        @SerialName("ACCESS_API")
        const val  UAT_ACCESS_API = "https://api.1click.tech/api/access"
        const val ACCESS_API = "https://api.1clicktech.in/api/access"

        const val  UAT_NOTIFICATION_MANAGEMENT = "https://api.1click.tech/api/notifications"
        const val NOTIFICATION_MANAGEMENT = "https://api.1clicktech.in/api/notifications"

        const val SALES_TOOL_API = "https://sales-tool-api.1click.tech"
    }
}

class ApiServices {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // Module 1 - Authentication Flow / Login/ Register Flow --------------------------------------------------

    //Sent OTP for verification
    suspend fun setOTPApi(userid: String): Pair<HttpStatusCode, String> {
        try {
            val response: HttpResponse = client.get() {
                url("${ApiConfig.ACCESS_API}/verification/mobile/$userid")
                contentType(ContentType.Application.Json)
            }
            val responseBody = response.body<String>()
            if (response.status.isSuccess()) {
                return Pair(response.status, response.status.description)
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            throw IOException(e.message.toString())
        }
    }

    // Verify OTP
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun verifyOTP(requestBody: VerifyOTP): String {
        try {
            val response: HttpResponse = client.post() {
                url("${ApiConfig.ACCESS_API}/verify/mobile")
                contentType(ContentType.Application.Json)
                body = Json.encodeToString(VerifyOTP.serializer(), requestBody)

            }
            if (response.status.isSuccess()) {
                return response.status.value.toString()
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            throw IOException(e.message.toString())
        }
    }

    //Check User Name  availability
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun checkUsernameEndpoint(username: String): Boolean {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.ACCESS_API}/users/check-username?")
                contentType(ContentType.Application.Json)
                parameter("username", username)
            }
            val responseBody = response.body<String>()
            if (response.status.isSuccess()) {
                return responseBody == "true"
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            throw IOException(e.message.toString())
        }
    }

    //Check User phone number availability
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun checkPhoneNumber(phone: String): Boolean {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.ACCESS_API}/users/check-mobile-number?")
                contentType(ContentType.Application.Json)
                parameter("mobileNumber", phone)
            }
            val responseBody = response.body<String>()
            if (response.status.isSuccess()) {
                return responseBody == "true"
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            throw IOException(e.message.toString())
        }

    }

    //Check User phone number availability
    @Throws(IOException::class, CancellationException::class)
    suspend fun checkEmail(email: String): Boolean {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.ACCESS_API}/users/check-email?")
                contentType(ContentType.Application.Json)
                parameter("email", email)
            }
            val responseBody = response.body<String>()
            if (response.status.isSuccess()) {
                return responseBody == "true"
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            throw IOException(e.message.toString())
        }
    }


    //-----------------------------------Login API ------------------------------------------------
    //Login With Credential
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun loginApi(user: UserDetails): UserResponse {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.UAT_ACCESS_API}/auth/login")
                contentType(ContentType.Application.Json)
                header("X-Project-ID", "0d98736c-5f90-41b4-b689-1b1935aab762")
                header("Referer", "https://api.1click.tech")
                body = Json.encodeToString(UserDetails.serializer(), user)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            println("Login error Message loginApi ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    //Login with Phone OTP
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun loginWithPhone(phone: String): Boolean {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.UAT_ACCESS_API}/auth/login/send-otp")
                contentType(ContentType.Application.Json)
                parameter("input", phone)
                parameter("X-Project-ID", "0d98736c-5f90-41b4-b689-1b1935aab762")
            }
            if (response.status.isSuccess()) {
                return true
            } else {
                throw IOException(
                    "${response.status}"
                )
            }
        } catch (e: Exception) {
            println("Login error Message loginWithPhone ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    //Authenticate Phone OTP FOR Login with otp
    @Throws(IOException::class, CancellationException::class)
    suspend fun authenticateLoginOTP(phone: String, otp: String): UserResponse {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.UAT_ACCESS_API}/auth/login/authenticate-otp")
                contentType(ContentType.Application.Json)
                header("Referer", "https://api.1click.tech/")
                header("X-Project-ID", "0d98736c-5f90-41b4-b689-1b1935aab762")
                parameter("input", phone)
                parameter("otp", otp)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            println("Login error Message authenticateLoginOTP ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Get user who logged In
    @Throws(IOException::class, CancellationException::class)
    suspend fun getUserWhoLoggedIn(token: String, userid: String): UserData {
        try {
            val response: HttpResponse = client.get {
                url("https://api.1click.tech/api/access/users/$userid")
                header("Authorization", "Bearer $token")
            }

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                // Capture the error response in more detail
                val errorMessage = response.body<String>()
                println("Error Response ${response.status}: $errorMessage")
                throw IOException(errorMessage)
            }
        } catch (e: Exception) {
            throw IOException(e.message.toString())
        }

    }


    // Refresh Token API --------------------------------------------------

    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun refreshToken(refreshToken: RefreshToken): UserResponse {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.UAT_ACCESS_API}/auth/token")
                contentType(ContentType.Application.Json)
                header("Referer", "https://api.1click.tech")
                body = Json.encodeToString(RefreshToken.serializer(), refreshToken)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            println(e.message.toString())
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Forget password
    // Send Token to Mail to Reset Password
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun resetPassword(email: String): String {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.UAT_ACCESS_API}/auth/resetPassword")
                contentType(ContentType.Application.Json)
                header("X-Project-ID", "0d98736c-5f90-41b4-b689-1b1935aab762")
                parameter("email", email)

            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Reset Password Failed -> ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }


    // Change Password with the received Token
    @Throws(IOException::class, CancellationException::class)
    suspend fun changePasswordWithToken(token: String, newPassword: String): String {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.UAT_ACCESS_API}/auth/changePasswordWithToken")
                contentType(ContentType.Application.Json)
                header("X-Project-ID", "0d98736c-5f90-41b4-b689-1b1935aab762")
                parameter("token", token)
                parameter("newPassword", newPassword)

            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Change Password with Token Error Message -> ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }


    //----------------------------------------------------------------------

    // Get In-App Notifications
    @Throws(IOException::class, CancellationException::class)
    suspend fun getNotifications(token: String, projectId: String):
            List<GetNotificationsResponse> {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.UAT_NOTIFICATION_MANAGEMENT}/notifications/appNotifications")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                parameter("projectId", projectId)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Login error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Register Device to activate the push notification using Unique FCM Token


    // Module 2 - Sales Tools Filter Apis / Policy Rates Get Api
    // Read all Policy Rates
    @Throws(IOException::class, CancellationException::class)
    suspend fun getPolicyRates(token: String): GetPolicyRates {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/policy_rates/")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                parameter("skip", 0)
                parameter("limit", 10)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Get Policy Rates Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Read Policy Rate with Id
    @Throws(IOException::class, CancellationException::class)
    suspend fun getPolicyRateDataUsingId(token: String, policyRateId: String): PolicyRateData {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/policy_rates/$policyRateId")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")

            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Get Policy Rates Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Motor Filters ---------------------------------------------------------------------------
    // Vehicle Details ---------------------------------------------------------------------------
    // Read all vehicle types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getVehicleTypes(token: String): VehicleTypes {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/vehicle_type/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Vehicle Type Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Read all Fuel Types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getFuelTypes(token: String): FuelTypes {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/fuel_type/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Fuel Type Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    @Throws(IOException::class, CancellationException::class)
    suspend fun getVehicleBrands(token: String): VehicleBrandTypes {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/vehicle_brand/active")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Fuel Type Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    @Throws(IOException::class, CancellationException::class)
    suspend fun getVehicleModels(): VehicleModels {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/vehicle_model/active")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Fuel Type Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }
    // ------------------------------------------------------------------------------------------

    // Location Details --------------------------------------------------------------------------
    // Read all States
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllStates(token: String): GetAllStates {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/state/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All States Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Read all City Categories
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllCityCategory(token: String): AllCityCategories {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/city_category/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("City Category Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Read all City Categories
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllCities(token: String): AllCities {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/city/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Cities Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }
    // ------------------------------------------------------------------------------------------

    // Policy Details ----------------------------------------------------------------------------
    // Read all Insurance Types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllInsuranceTypes(token: String): InsuranceTypes {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/insurance_type/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Get All Insurance Type Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Read all Renewal Types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllRenewalTypes(token: String): RenewalTypes {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/renewal_type/all")
                contentType(ContentType.Application.Json)

            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Get Renewal Types Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Read all Insurer Types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllInsurerTypes(token: String): InsurerTypes {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/insurer/all")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All Insurer Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    //  --------------------------------------------------------------------------------------------
    // Health Filters -----------------------------------------------------------------------------
    // Slab Types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllSlabTypes(): SlabResponse {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/slab/")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All Slab Types Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Product Types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllProductTypes(): ProductResponse {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/product/")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All Product Types Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Insurer Groups Data
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllInsurerGroups(): InsurerGroupResponse {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/insurer_group/")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All Insurer Group Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Policy Segments
    @Throws(IOException::class, CancellationException::class)
    suspend fun getAllPolicySegments(): List<PolicySegmentResponse> {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/policy_segment/")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All Policy Segment Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Insurance Type Using Policy Segment ID
    @Throws(IOException::class, CancellationException::class)
    suspend fun getInsuranceTypeByPolicySegments(segmentId: String): InsuranceTypeUsingSegmentID {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/insurance_type/policy_segment/${segmentId}")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All Insurance Type Using SegmentID Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // PPTs Types Using Policy Segment ID
    @Throws(IOException::class, CancellationException::class)
    suspend fun getPPtsByPolicySegments(segmentId: String): PPTsTypesBySegmentId {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/ppt/policy_segment/${segmentId}")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("All PPts Types Using SegmentID Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Renewal Type Using Policy Segment ID
    @Throws(IOException::class, CancellationException::class)
    suspend fun getRenewalTypeBySegmentId(segmentId : String) : RenewalTypesBySegmentIdResponse {
        try {
            val response : HttpResponse = client.get {
                url("${ApiConfig.SALES_TOOL_API}/renewal_type/policy_segment/${segmentId}")
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()){
                return response.body()
            }
            else{
                throw IOException(
                    response.body<String>()
                )
            }
        }
        catch (e: Exception) {
            println("All Insurance Type Using SegmentID Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }


    //  --------------------------------------------------------------------------------------------

    // Search Policy Rate Data
    @Throws(IOException::class, CancellationException::class)
    @OptIn(InternalAPI::class)
    suspend fun searchPolicyRateData(
        token: String,
        searchData: SearchPolicyRatePayload
    ): SearchPolicyRateData {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.SALES_TOOL_API}/policy_rates/search")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                parameter("skip", 1)
                parameter("limit", 1000)
                body = Json.encodeToString(SearchPolicyRatePayload.serializer(), searchData)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }

        } catch (e: Exception) {
            println("Search Policy Rate Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Search General Policy Rate Data
    @Throws(IOException::class, CancellationException::class)
    @OptIn(InternalAPI::class)
    suspend fun searchGeneralPolicyRateData(
        token: String,
        searchData: GeneralPolicyRatePayload
    ): GeneralPolicyRateResponse {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.SALES_TOOL_API}/general_policy_rates/search")
                contentType(ContentType.Application.Json)
                parameter("skip", 1)
                parameter("limit", 100)
                body = Json.encodeToString(GeneralPolicyRatePayload.serializer(), searchData)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }

        } catch (e: Exception) {
            println("Search Policy Rate Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Get Registration Number from Image
    @Throws(IOException::class, CancellationException::class)
    @OptIn(InternalAPI::class)
    suspend fun getRegistrationNumberFromImage(
        token: String,
        filePath: String
    ): GetRegistrationNumberResponse {
        val cioClient = HttpClient(CIO)
        try {

            // Create a multipart form data request
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = "https://sales-tool-api.1click.tech/ocr/vehicle_number",
                formData = formData {
                    // Use Okio to read the file as a source
//                    val file = FileSystem.SYSTEM.metadata(filePath.toPath())
                    val source = FileSystem.SYSTEM.source(filePath.toPath()).buffer()

                    append("file", source.readByteArray(), Headers.build {
                        append(HttpHeaders.Accept, ContentType.Application.Json)
                        append(HttpHeaders.ContentType, "multipart/form-data")
                        append("Authorization", token)
                    })
                }
            )

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }

        } catch (e: Exception) {
            println("Upload Image Error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Get Vehicle Details for the Vehicle Number
    @Throws(IOException::class, CancellationException::class)
    suspend fun getVehicleDetails(token: String, vehicleNumber: String): GetVehicleDetails {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.SALES_TOOL_API}/ocr")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                parameter("vehicle_number", vehicleNumber)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw ApiException.UnknownException(response.body())
            }
        } catch (e: TimeoutCancellationException) {
            throw ApiException.TimeoutException()
        } catch (e: IOException) {
            if (e.message?.contains("No Internet") == true) {
                throw ApiException.NoInternetException()
            } else {
                throw ApiException.UnknownException(e.message ?: "Unknown error")
            }
        }
    }


    // Register Device for Enabling Push Notification
    @Throws(IOException::class, CancellationException::class)
    suspend fun registerDeviceForNotification(
        token: String,
        projectId: String,
        userId: String,
        deviceToken: String
    ):
            RegisteredDeviceResponse {
        try {
            val response : HttpResponse = client.post {
                url("${ApiConfig.UAT_NOTIFICATION_MANAGEMENT}/registeredDevices/register")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                parameter("userId", userId)
                parameter("projectId", projectId)
                parameter("deviceToken", deviceToken)
            }

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<String>()
                )
            }
        } catch (e: Exception) {
            println("Register Device error Message ${e.cause?.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }


    // --------------------------------------------------------------------------------------------


    // Register Device for Enabling Push Notification
    @Throws(IOException::class, CancellationException::class)
    suspend fun setregisterDeviceForNotification(
        token: String,
        projectId: String,
        userId: String,
        deviceToken: String
    ): RegisteredDeviceResponse {
        try {
            val response: HttpResponse = client.post {
                url(
                    ApiConfig.UAT_NOTIFICATION_MANAGEMENT +
                            "/registeredDevices/register"
                )
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                parameter("userId", userId)
                parameter("projectId", projectId)
                parameter("deviceToken", deviceToken)
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.status.value.toString()
                )
            }
        } catch (e: Exception) {
            println("Register Device error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }
}

