package com.example.lms.Services

import com.example.lms.Services.Dataclass.AllCourseCategoryResponse
import com.example.lms.Services.Dataclass.FailedResponse
import com.example.lms.Services.Dataclass.GetContentTypes
import com.example.lms.Services.Dataclass.GetNotificationsResponse
import com.example.lms.Services.Dataclass.GetUserCoursesResponse
import com.example.lms.Services.Dataclass.GetUserData
import com.example.lms.Services.Dataclass.MyCoursesResponse
import com.example.lms.Services.Dataclass.PublishedCourseResponse
import com.example.lms.Services.Dataclass.RefreshToken
import com.example.lms.Services.Dataclass.RegisterRequest
import com.example.lms.Services.Dataclass.RegisterResponse
import com.example.lms.Services.Dataclass.ShowCourseByCourseId
import com.example.lms.Services.Dataclass.ShowMyCoursesPayload
import com.example.lms.Services.Dataclass.UserDetails
import com.example.lms.Services.Dataclass.UserResponse
import com.example.lms.Services.Dataclass.VerifyOTP
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

@Serializable
class ApiConfig {
    companion object {

        @SerialName("ACCESS_API")
//        const val ACCESS_API = "https://api.1clicktech.in/api/access"
        const val ACCESS_API = "https://api.1click.tech/api/access"
        const val COURSE_MANAGEMENT_API = "https://ccm-api.1clicktech.in/api"
        const val NOTIFICATION_MANAGEMENT = "https://api.1clicktech.in/api/notifications"
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
                url("${ApiConfig.ACCESS_API}/auth/login")
                contentType(ContentType.Application.Json)
                header("Referer", "https://api.1click.tech")
                header("X-Project-ID", "0d98736c-5f90-41b4-b689-1b1935aab762")
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
                url("${ApiConfig.ACCESS_API}/auth/login/send-otp?")
                contentType(ContentType.Application.Json)
//                header("Referer","https://api.1clicktech.in")
                parameter("input", phone)
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
                url("${ApiConfig.ACCESS_API}/auth/login/authenticate-otp?")
                contentType(ContentType.Application.Json)
                header("Referer", "https://api.1clicktech.in")
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
    suspend fun getUserWhoLoggedIn(token: String): GetUserData {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.ACCESS_API}/users")
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
            throw IOException(e.message.toString())
        }

    }


    // Refresh Token API --------------------------------------------------

    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun refreshToken(refreshToken: RefreshToken): UserResponse {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.ACCESS_API}/auth/token")
                contentType(ContentType.Application.Json)
                header("Referer", "https://api.1clicktech.in")
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


    // Module 1 - Authentication Flow / Login/Register Flow --------------------------------------------------

    // Course Category
    //  Give list of all course category
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun allCourseCategory(token: String): List<AllCourseCategoryResponse> {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/course_category/forSelectBox")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.status.value.toString()
                )
            }
        } catch (e: Exception) {
            println("Login error Message allCourseCategory ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Published Courses
    // Give list of All Published Courses
    @Throws(IOException::class, CancellationException::class)
    suspend fun allPublishesCourses(token: String): List<PublishedCourseResponse> {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/course/publishedCourse")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }

        } catch (e: Exception) {
            println("Login error Message allPublishesCourses ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }

    }

    // Search Course By Course Category ID
    // Gives List of Courses Filtered By Course Category ID
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun searchCourseByCategoryId(
        token: String,
        courseCategoryId: String
    ): List<PublishedCourseResponse> {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/course/searchCourseByCategoryId")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("course_category_id", courseCategoryId)
                        }
                    )
                )
            }

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw IOException(
                    response.body<FailedResponse>().message
                )
            }
        } catch (e: Exception) {
            println("Login error Message searchCourseByCategoryId ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Get Course Details By Course ID
    @Throws(IOException::class, CancellationException::class)
    suspend fun showCourseById(
        token: String,
        courseId: String
    ): ShowCourseByCourseId {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/course/show")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("id", courseId)
                        }
                    )
                )
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


    // Get Courses Assigned to a user
    @Throws(IOException::class, CancellationException::class)
    suspend fun getUserCourses(token: String): List<GetUserCoursesResponse> {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/course_enrollment/getUserCourses")
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
            println("Login error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

    // Give an object of an course using course-Id / My Course Detail Information
    @OptIn(InternalAPI::class)
    @Throws(IOException::class, CancellationException::class)
    suspend fun showMyCourses(
        token: String,
        myCourseId: ShowMyCoursesPayload
    ): List<MyCoursesResponse> {
        try {
            val response: HttpResponse = client.post {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/my_courses/show")
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                body = Json.encodeToString(ShowMyCoursesPayload.serializer(), myCourseId)
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

    // Get In-App Notifications
    @Throws(IOException::class, CancellationException::class)
    suspend fun getNotifications(token: String, projectId: String):
            List<GetNotificationsResponse> {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.NOTIFICATION_MANAGEMENT}/notifications/appNotifications")
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

    // Get list of all content types
    @Throws(IOException::class, CancellationException::class)
    suspend fun getContentTypes(token: String): List<GetContentTypes> {
        try {
            val response: HttpResponse = client.get {
                url("${ApiConfig.COURSE_MANAGEMENT_API}/content_type/show")
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
            println("Login error Message ${e.message}")
            throw e.message?.let { IOException(it) }!!
        }
    }

//    POLICY DETAILS


}