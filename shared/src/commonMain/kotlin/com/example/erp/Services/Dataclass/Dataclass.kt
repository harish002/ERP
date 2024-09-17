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
    val vehicle_model_id: String?=null,
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
    val description: String?=null,
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
    val data : List<VehicleData>? = emptyList()
)

@Serializable
data class VehicleData(
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
    val data : List<FuelTypeData>? = emptyList()
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

// Read all States ----------------------------------------------------------
@Serializable
data class GetAllStates(
    val message: String,
    val data : List<StatesData>? = emptyList()
)

@Serializable
data class StatesData(
    val name: String,
    val description: String?=null,
    val id: String,
    val created_at: String,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)
//-----------------------------------------------------------------------------

// Read All City Categorys -----------------------------------------------------
@Serializable
data class AllCityCategories(
    val message: String,
    val data : List<CityCategoryData>? = emptyList()
)

@Serializable
data class CityCategoryData(
    val name: String,
    val description: String? = null,
    val id: String,
    val status: Int,
    val created_at: String? = null
)

//-----------------------------------------------------------------------------

// Read all Cities -----------------------------------------------------
@Serializable
data class AllCities(
    val message: String,
    val data : List<CityData>
)

@Serializable
data class CityData(
    val name: String,
    val description: String?=null,
    val state_id: String,
    val city_category_id: String,
    val id: String,
    val city_category: CityCategoryFromCityApi,
    val state: StateFromCityApi,
    val status: Int,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null
)

@Serializable
data class CityCategoryFromCityApi(
    val name: String,
    val description: String,
    val id: String,
    val status: Int,
    val created_at: String? = null
)

@Serializable
data class StateFromCityApi(
    val name: String,
    val description: String,
    val id: String,
    val created_at: String,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)
//-----------------------------------------------------------------------------

// Read all Insurance Types ---------------------------------------------------
@Serializable
data class InsuranceTypes(
    val message: String,
    val data : List<InsuranceTypeData>
)

@Serializable
data class InsuranceTypeData(
    val name: String,
    val description: String,
    val created_at: String? = null,
    val id: String,
    val status: Int
)

//-----------------------------------------------------------------------------

// Read all Renewal Types ---------------------------------------------------
@Serializable
data class RenewalTypes(
    val message: String,
    val data : List<RenewalTypeData>
)

@Serializable
data class RenewalTypeData(
    val name: String,
    val description: String,
    val id: String,
    val created_at: String? = null,
    val created_by: String? = null,
    val updated_at: String? = null,
    val updated_by: String? = null,
    val deleted_at: String? = null,
    val deleted_by: String? = null,
    val status: Int
)
//-----------------------------------------------------------------------------

// Read all Insurer Types---------------------------------------------------
@Serializable
data class InsurerTypes(
    val message: String,
    val data : List<InsurerData>
)

@Serializable
data class InsurerData(
    val id: String,
    val name: String,
    val status: Int,
    val description: String? = null,
    val created_at: String? = null
)

//-----------------------------------------------------------------------------




//------------------------------------------------------------------------------------


