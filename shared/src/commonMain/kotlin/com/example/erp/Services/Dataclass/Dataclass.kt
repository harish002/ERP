package com.example.lms.Services.Dataclass

import kotlinx.serialization.Serializable

// Module 1 ------------------------------------------------------------------
// Login Api Data Classes ----------------------
// Login Request Body
@Serializable
data class UserDetails(
    val password: String,
    val username: String
)

// Refresh Token Request Body
@Serializable
data class RefreshToken(
    val refreshToken : String
)

//Login Response Data Class
@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String?=null,
    val userData: UserData,
    val token: String,
    val refreshToken: String
)

@Serializable
data class UserData(
    val id: String,
    val name: String,
    val username: String,
    val surname: String,
    val email: String?=null,
    val mobileNumber: String,
    val gender: String,
    val birthDate: String?=null,
    val enabled: Boolean,
    val superAdmin: Boolean,
    val note: String?=null,
    val projectRoles: List<ProjectRole>,
    val departmentRoles: List<DepartmentRole>,
    val zones: List<Zone>,
    val departments: List<Department>,
    val verifications: List<Verification>,
    val currentRole: CurrentRole?=null,
    val notificationPreferences: List<NotificationPreference>,
    val isAvailable: Boolean,
    val createdDate: String,
)

@Serializable
data class ProjectRole(
    val id: String,
    val name: String,
    val level: Int,
    val project: Project,
    val role: Role,
    val permissions: List<Permission>,
    val scope: String? = null,
    val admin: Boolean,
    val superAdmin: Boolean
)

@Serializable
data class DepartmentRole(
    val id: String,
    val name: String,
    val level: Int,
    val department: Department,
    val role: Role? = null,
    val permissions: List<Permission> ?= null,
    val admin: Boolean,
    val superAdmin: Boolean
)

@Serializable
data class Zone(
    val id: String,
    val name: String,
    val parentZone: String,
    val zones: List<String>? = null
)
@Serializable
data class Department(
    val id: String,
    val level: Int,
    val name: String,
    val projects: List<Project>? = null,
    val roles: List<String>? = null,
    val organisation: Organisation? = null,
    val zones: List<Zone>? = null
)
@Serializable
data class DepartmentX(
    val id: String,
    val level: Int,
    val name: String,
    )
@Serializable
data class Verification(
    val type: String,
    val verified: Boolean
)
@Serializable
data class CurrentRole(
    val id: String,
    val name: String
)
@Serializable
data class NotificationPreference(
    val notificationType: String,
    val notificationChannels: List<String>
)
@Serializable
data class Project(
    val id: String,
    val name: String,
    val subject: String? = null,
    val department: DepartmentX?= null,//String in Example
    val roles: List<String>?= emptyList(),
    val createdDate: String?= null,
    val pipelines: List<Pipeline>?= emptyList()
)
@Serializable
data class Role(
    val id: String,
    val name: String,
    val admin: Boolean,
    val superAdmin: Boolean
)

@Serializable
data class Permission(
    val id: String,
    val name: String,
    val type: String? = null,
    val module: Module? = null
)

@Serializable
data class Pipeline(
    val id: String,
    val name: String,
    val active: Boolean
)
@Serializable
data class Module(
    val id: String,
    val name: String
)



@Serializable
data class Organisation(
    val id: String,
    val name: String,
    val departments: List<String>? = null,
    val zones: List<Zone>? = null
)
@Serializable
data class FailedResponse(
    val message: String,
    val timestamp: Long
)

// Get User Who is Logged In
// Response Body
@Serializable
data class GetUserData(
    val id: String,
    val username: String,
    val email: String?=null,
    val userData: UserData,

    )


// Register Api Data Classes ----------------------
// Register Request Body
@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val email: String,
    val mobileNumber: String,
    val gender: String,
    val optionalProjectRoles: List<String>,//need to change if pojo changes
    val optionalDepartmentRoles: List<String>,//need to change if pojo changes
    val projectId: String
)
//----------------------------------------------------

// Register Api Data Classes ----------------------
@Serializable
data class RegisterResponse(
    val id: String,
    val name: String,
    val username: String,
    val surname: String,
    val email: String,
    val mobileNumber: String,
    val gender: String,
    val enabled: Boolean,
    val superAdmin: Boolean,
    val verifications: List<Verification>,
    val isAvailable: Boolean,
    val createdDate: String
)
//----------------------------------------------------


// OTP Verification Data Classes
// Verify OTP Request Body
@Serializable
data class VerifyOTP(
    val mobileNumber: String,
    val otp: String
)
//-----------------------------------------------------------------------------


// Module 2 ------------------------------------------------------------------
// Course Category
// Give list of all course category
// All Course Category Response
@Serializable
data class AllCourseCategoryResponse(
    val id : String,
    val title : String
)

// Give a list of all Published Courses
// All Published Courses Response
@Serializable
data class PublishedCourseResponse(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail_id: String? = null,
    val thumbnail_url: String,
    val course_category_id: String,
    val organization_id: String,
    val project_id: String,
    val bundle_id: String? = null,
    val is_draft: Boolean,
    val is_final: Boolean,
    val is_published: Boolean,
    val instructor_id: String,
    val community_id: String? = null,
    val course_fee: Int,
    val currency: String,
    val is_public: Boolean,
    val is_encrypted: Boolean,
    val is_training_course: Boolean,
    val is_certificate_course: Boolean,
    val collection_guid: String? = null,
    val collection_object: String? = null,
    val is_free: Boolean,
    val discounted_fee: Int? = null,
    val course_length: Double,
    val average_rating: String,
    val rating_count: Int,
    val qb_subject_id: String? = null,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val instructor: Instructor? = null
)

@Serializable
data class Instructor(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val project_id: String? = null,
    val organization_id: String,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null
)


// Give list of course filtered by Course Category ID
// List of Course Category ID Request
@Serializable
data class AllCourseByCategoryIDRequest(
    val course_category_id : String
)

// Response for course filtered by course category ID is same as { PublishedCourseResponse }

// Get an object of Course by Course Id -------------------------
// Response Body
@Serializable
data class ShowCourseByCourseId(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail_id: String? = null,
    val thumbnail_url: String,
    val course_category_id: String,
    val organization_id: String,
    val project_id: String,
    val bundle_id: String? = null,
    val is_draft: Boolean,
    val is_final: Boolean,
    val is_published: Boolean,
    val instructor_id: String,
    val community_id: String? = null,
    val course_fee: Int,
    val currency: String,
    val is_public: Boolean,
    val is_encrypted: Boolean,
    val is_training_course: Boolean,
    val is_certificate_course: Boolean,
    val collection_guid: String? = null,
    val collection_object: String? = null,
    val is_free: Boolean,
    val discounted_fee: Int? = null,
    val course_length: Double,
    val average_rating: String,
    val rating_count: Int,
    val qb_subject_id: String? = null,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val sections : List<Section>? = emptyList(),
    val instructor: Instructor? = null
)

@Serializable
data class Section(
    val id: String,
    val title: String,
    val hierarchy: Int,
    val course_id: String,
    val qb_chapter_id: String? = null,
    val section_length: Int,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val contents: List<Content>? = emptyList()
)

@Serializable
data class Content(
    val id: String,
    val title: String,
    val description: String,
    val media_id: String? = null,
    val media_url: String? = null,
    val thumbnail_id: String? = null,
    val thumbnail_url:  String? = null,
    val hierarchy: Int,
    val course_id: String,
    val section_id: String,
    val content_type_id: String,
    val qb_topic_id: String? = null,
    val content_length: Int,
    val created_by:  String? = null,
    val updated_by:  String? = null,
    val deleted_by:  String? = null,
    val created_at:  String? = null,
    val updated_at:  String? = null,
    val deleted_at:  String? = null,
    val remarks:  String? = null
)
//-----------------------------------------------------------------------------


// Get Courses Assigned to a user -------------------------
// Response Body
@Serializable
data class GetUserCoursesResponse(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail_id: String? = null,
    val thumbnail_url: String,
    val course_category_id: String,
    val organization_id: String,
    val project_id: String,
    val bundle_id: String? = null,
    val is_draft: Boolean,
    val is_final: Boolean,
    val is_published: Boolean,
    val instructor_id: String,
    val community_id: String? = null,
    val course_fee: Int,
    val currency: String,
    val is_public: Boolean,
    val is_encrypted: Boolean,
    val is_training_course: Boolean,
    val is_certificate_course: Boolean,
    val collection_guid: String? = null,
    val collection_object: String? = null,
    val is_free: Boolean,
    val discounted_fee: Int? = null,
    val course_length: Double,
    val average_rating: String,
    val rating_count: Int,
    val qb_subject_id: String? = null,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val first_content: FirstContent,
    val progress_hours: Double,
    val instructor: Instructor? = null,
    val content: List<Content>? = emptyList()
)

@Serializable
data class FirstContent(
    val id: String,
    val title: String,
    val description: String,
    val media_id: String,
    val media_url: String,
    val thumbnail_id: String? = null,
    val thumbnail_url: String? = null,
    val hierarchy: Int,
    val course_id: String,
    val section_id: String,
    val content_type_id: String,
    val qb_topic_id: String? = null,
    val content_length: Int,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null
)
//-----------------------------------------------------------------------------

// Give an object of an course using course-Id / My Course Detail Information --------
// Payload
@Serializable
data class ShowMyCoursesPayload(
    val id : String
)

// Response Body
@Serializable
data class MyCoursesResponse(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail_id: String? = null,
    val thumbnail_url: String,
    val course_category_id: String,
    val organization_id: String,
    val project_id: String,
    val bundle_id: String? = null,
    val is_draft: Boolean,
    val is_final: Boolean,
    val is_published: Boolean,
    val instructor_id: String,
    val community_id: String? = null,
    val course_fee: Int,
    val currency: String,
    val is_public: Boolean,
    val is_encrypted: Boolean,
    val is_training_course: Boolean,
    val is_certificate_course: Boolean,
    val collection_guid: String,
    val collection_object: String? = null,
    val is_free: Boolean,
    val discounted_fee: Int? = null,
    val course_length: Double,
    val average_rating: String,
    val rating_count: Int,
    val qb_subject_id: String? = null,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val sections: List<MyCourse_Section>? = emptyList(),
    val instructor: MyCoursse_Instructor? = null
)

@Serializable
data class MyCourse_Section(
    val id: String,
    val title: String,
    val hierarchy: Int? = null,
    val course_id: String,
    val qb_chapter_id: String? = null,
    val section_length: Int,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val contents: List<MyCourse_Content>? = emptyList()
)

@Serializable
data class MyCoursse_Instructor(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val project_id: String,
    val organization_id: String,
    val created_by: String? = null,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null
)

@Serializable
data class MyCourse_Content(
    val id: String,
    val title: String,
    val description: String,
    val media_id: String,
    val media_url: String,
    val thumbnail_id: String? = null,
    val thumbnail_url: String? = null,
    val hierarchy: Int? = null,
    val course_id: String,
    val section_id: String,
    val content_type_id: String,
    val qb_topic_id: String? = null,
    val content_length: Int,
    val created_by: String,
    val updated_by: String? = null,
    val deleted_by: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val remarks: String? = null,
    val user_progress: UserProgress? = null,
    val user_assessment: UserAssessment? = null
)

@Serializable
data class UserProgress(
    val id: String,
    val user_id: String,
    val course_id: String,
    val section_id: String,
    val content_id: String,
    val is_completed: Boolean,
    val progress_hours: Double,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class UserAssessment(
    val id: String,
    val user_id: String,
    val course_id: String,
    val section_id: String,
    val content_id: String,
    val obtained_marks: Int,
    val total_marks: Int,
    val is_completed: Boolean,
    val created_at: String? = null,
    val updated_at: String? = null
)

//-----------------------------------------------------------------------------

// Get In-App Notifications -------------------------------------------------
// Response Body
@Serializable
data class GetNotificationsResponse(
    val id: String,
    val type: String,
    val notificationChannel: String,
    val content: String
)

//-----------------------------------------------------------------------------

// Module 2 - Sales Tools Filter Apis /  Policy Rates ---------------------------------------------
// Read All Policy Rates ------------------------------------------
// Response Body
@Serializable
data class GetPolicyRates(
    val message: String,
    val data : List<PolicyRateData>
)

@Serializable
data class PolicyRateData(
    val city_id: String,
    val fuel_type_id: String,
    val renewal_type_id: String,
    val insurance_type_id: String,
    val insurer_id: String,
    val vehicle_model_id: String,
    val payouts: String,
    val payins: String? = null,
    val description: String? = null,
    val id: String,
    val city: City,
    val vehicle_model: VehicleModel,
    val fuel_type: FuelType,
    val insurance_type: InsuranceType,
    val insurer: Insurer,
    val renewal_type: RenewalType,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

@Serializable
data class City(
    val name: String,
    val description: String? = null,
    val state_id: String,
    val city_category_id: String,
    val id: String,
    val city_category: CityCategory,
    val state: State,
    val status: Int,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null
)

@Serializable
data class VehicleModel(
    val id: String,
    val name: String,
    val description: String,
    val vehicle_brand_id: String,
    val vehicle_type_id: String,
    val vehicle_brand: VehicleBrand,
    val vehicle_type: VehicleType,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

@Serializable
data class FuelType(
    val name: String,
    val description: String? = null,
    val created_at: String? = null,
    val id: String,
    val status: Int
)

@Serializable
data class InsuranceType(
    val name: String,
    val description: String? = null,
    val created_at: String? = null,
    val id: String,
    val status: Int
)

@Serializable
data class Insurer(
    val id: String,
    val name: String,
    val status: Int,
    val description: String? = null,
    val created_at: String? = null
)

@Serializable
data class RenewalType(
    val name: String,
    val description: String? = null,
    val id: String,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

@Serializable
data class CityCategory(
    val name: String,
    val description: String? = null,
    val id: String,
    val status: Int,
    val created_at: String
)

@Serializable
data class State(
    val name: String,
    val description: String? = null,
    val id: String,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

@Serializable
data class VehicleBrand(
    val name: String,
    val description: String? = null,
    val id: String,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

@Serializable
data class VehicleType(
    val name: String,
    val description: String? = null,
    val id: String,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

//-----------------------------------------------------------------

// Read all Vehicle Types ------------------------------------------
// Response Body
@Serializable
data class VehicleTypes(
    val message: String,
    val data : List<Data>
)

@Serializable
data class Data(
    val name: String,
    val description: String? = null,
    val id: String,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)

// Read All Fuel Types -----------------------------------------------------
// Response Body
@Serializable
data class FuelTypes(
    val message: String,
    val data : List<FuelTypeData>
)

@Serializable
data class FuelTypeData(
    val name: String,
    val description: String? = null,
    val created_at: String? = null,
    val id: String,
    val status: Int
)

//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------------


