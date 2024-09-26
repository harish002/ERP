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
    val state: States,
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
    val description: String? = null,
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
data class States(
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
    val description: String? = null,
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
    val description: String? = null,
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
    val description: String? = null,
    val id: String,
    val status: Int,
    val created_at: String? = null
)

@Serializable
data class StateFromCityApi(
    val name: String,
    val description: String? = null,
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
    val description: String? = null,
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
//-----------------------------------------------------------------------------

// Read all Insurer Types ---------------------------------------------------
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

// Search Policy Rates --------------------------------------------------------
// Payload
@Serializable
data class SearchPolicyRatePayload(
    val state_id: String,
    val city_id: String,
    val city_category_id: String,
    val vehicle_type_id: String,
    val vehicle_model_id: String,
    val renewal_type_id: String,
    val insurance_type_id: String,
    val insurer_id: String,
    val fuel_type_id: String,
    val status: String,
    val page: Int,
    val size: Int
)

// Response Body
@Serializable
data class SearchPolicyRateData(
    val items: List<PolicyRateData>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

// Get Registration Number from Image
// Response Body
@Serializable
data class GetRegistrationNumberResponse(
    val result : String
)

// Get Vehicle Details -----------------------------------------------------------------------
// Response Body
@Serializable
data class GetVehicleDetails(
    val status: String,
    val message: String,
    val response_type: String? = null,
    val result: Result? = null
)

@Serializable
data class Result(
    val state_code: String? = null,
    val state: String? = null,
    val office_code: Int? = null,
    val office_name: String? = null,
    val reg_no: String? = null,
    val reg_date: String? = null,
    val purchase_date: String? = null,
    val owner_count: Int? = null,
    val owner_name: String? = null,
    val owner_father_name: String? = null,
    val current_address_line1: String? = null,
    val current_address_line2: String? = null,
    val current_address_line3: String? = null,
    val current_district_name: String? = null,
    val current_state: String? = null,
    val current_state_name: String? = null,
    val current_pincode: Int? = null,
    val current_full_address: String? = null,
    val permanent_address_line1: String? = null,
    val permanent_address_line2: String? = null,
    val permanent_address_line3: String? = null,
    val permanent_district_name: String? = null,
    val permanent_state: String? = null,
    val permanent_state_name: String? = null,
    val permanent_pincode: Int? = null,
    val permanent_full_address: String? = null,
    val owner_code_descr: String? = null,
    val reg_type_descr: String? = null,
    val vehicle_class_desc: String? = null,
    val chassis_no: String? = null,
    val engine_no: String? = null,
    val vehicle_manufacturer_name: String? = null,
    val model_code: String? = null,
    val model: String? = null,
    val body_type: String? = null,
    val cylinders_no: Int? = null,
    val vehicle_hp: Double? = null,
    val vehicle_seat_capacity: Int? = null,
    val vehicle_standing_capacity: Int? = null,
    val vehicle_sleeper_capacity: Int? = null,
    val unladen_weight: Int? = null,
    val vehicle_gross_weight: Int? = null,
    val vehicle_gross_comb_weight: Int? = null,
    val fuel_descr: String? = null,
    val color: String? = null,
    val manufacturing_mon: Int? = null,
    val manufacturing_yr: Int? = null,
    val norms_descr: String? = null,
    val wheelbase: Int? = null,
    val cubic_cap: Double? = null,
    val floor_area: Int? = null,
    val ac_fitted: String? = null,
    val audio_fitted: String? = null,
    val video_fitted: String? = null,
    val vehicle_purchase_as: String? = null,
    val vehicle_catg: String? = null,
    val dealer_code: String? = null,
    val dealer_name: String? = null,
    val dealer_address_line1: String? = null,
    val dealer_address_line2: String? = null,
    val dealer_address_line3: String? = null,
    val dealer_district: String? = null,
    val dealer_pincode: String? = null,
    val dealer_full_address: String? = null,
    val sale_amount: Int? = null,
    val laser_code: String? = null,
    val garage_add: String? = null,
    val length: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    val reg_upto: String? = null,
    val fit_upto: String? = null,
    val annual_income: Int? = null,
    val op_dt: String? = null,
    val imported_vehicle: String? = null,
    val other_criteria: Int? = null,
    val status: String? = null,
    val vehicle_type: String? = null,
    val tax_mode: String? = null,
    val mobile_no: Long? = null,
    val email_id: String? = null,
    val pan_no: String? = null,
    val aadhar_no: String? = null,
    val passport_no: String? = null,
    val ration_card_no: String? = null,
    val voter_id: String? = null,
    val dl_no: String? = null,
    val verified_on: String? = null,
    val dl_validation_required: Boolean? = null,
    val condition_status: Boolean? = null,
    val vehicle_insurance_details: VehicleInsuranceDetails? = null,
    val vehicle_pucc_details: VehiclePuccDetails? = null,
    val permit_details: String? = null,
    val latest_tax_details: LatestTaxDetails? = null,
    val financer_details: Financer_details? = null
)

@Serializable
data class VehicleInsuranceDetails(
    val insurance_from: String,
    val insurance_upto: String,
    val insurance_company_code: Int,
    val insurance_company_name: String,
    val opdt: String,
    val policy_no: String,
    val vahan_verify: String,
    val reg_no: String
)

@Serializable
data class VehiclePuccDetails(
    val pucc_from: String,
    val pucc_upto: String,
    val pucc_centreno: String,
    val pucc_no: String,
    val op_dt: String
)

@Serializable
data class LatestTaxDetails(
    val reg_no: String,
    val tax_mode: String,
    val payment_mode: String,
    val tax_amt: Int,
    val tax_fine: Int,
    val rcpt_dt: String,
    val tax_from: String? = null,
    val tax_upto: String? = null,
    val collected_by: String,
    val rcpt_no: String
)

@Serializable
data class Financer_details(
    val hp_type: String,
    val financer_name: String,
    val financer_address_line1: String,
    val financer_address_line2: String,
    val financer_address_line3: String,
    val financer_district: Int,
    val financer_pincode: Int,
    val financer_state: String,
    val financer_full_address: String,
    val hypothecation_dt: String,
    val op_dt: String
)

//--------------------------------------------------------------------------------

// Register Devices for Push Notification
// Response
@Serializable
data class RegisteredDeviceResponse(
    val id: String? = null,
    val remarks: String? = null,
    val createdDate: String? = null,
    val createdBy: String? = null,
    val lastModifiedDate: String? = null,
    val lastModifiedBy: String? = null,
    val version: Int,
    val projectId: String? = null,
    val userId: String? = null,
    val deviceTokens: List<String>? = emptyList()
)

//-------------------------------------------------------------------------------


