
//  Created by Tusmit Shah on 05/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

enum ApiError: Error {
    case networkFailure
    case lowInternetConnection
    case invalidResponse
    case serverError(status: String)
    case unknownError(description: String)
}

class AccessServiceViewModel : ObservableObject {
    
    
    // Use of variable in the Views for mutable data's
    @Published var loginWithOtpCredential : String = ""
    @Published var specificCourseId : String = ""       // Used in Course Detail Screen for Fetching Details about the course --> /course/show
    @Published var myCourseId : String = ""             // Used in Course Start View for Getting Course Information --> /my_courses/show
    
    // Fix Variable Values
    let projectId : String = "c319ab33-dbf1-45e7-b566-521cfecfb3e5"
    
    // Module 1 ----------------------------------------------------------------------------------------------------------------
    // Login With Otp Api's ViewModel--------------------------------------------------------
    // Login
    
    @Published var userSpecs : UserData?
    @Published var loginErrorMessage : String = ""
    func login(user: UserDetails) async throws -> (Bool,String) {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async { [self] in
                Task {
                    do
                    {
                        let response = try await ApiServices().loginApi(user: user)
                        
                        let authToken = response.token
                        saveToken(token: authToken)
                        
                        let authRefreshToken = response.refreshToken
                        saveRefreshToken(refreshToken: authRefreshToken)
                        
                        let userData = UserData(
                            id: response.userData.id,
                            name: response.userData.name, 
                            username: response.userData.username,
                            surname: response.userData.surname,
                            email: response.userData.email,
                            mobileNumber: response.userData.mobileNumber,
                            gender: response.userData.gender,
                            birthDate: response.userData.birthDate,
                            enabled: response.userData.enabled,
                            superAdmin: response.userData.superAdmin,
                            note: response.userData.note,
                            projectRoles: response.userData.projectRoles,
                            departmentRoles: response.userData.departmentRoles,
                            zones: response.userData.zones,
                            departments: response.userData.departments,
                            verifications: response.userData.verifications,
                            currentRole: response.userData.currentRole,
                            notificationPreferences: response.userData.notificationPreferences ,
                            isAvailable: response.userData.isAvailable,
                            createdDate: response.userData.createdDate
                            
                        )
                        
                        self.userSpecs = userData

                        print("UserData : \(String(describing: userSpecs))")
                        self.loginErrorMessage = ""
                        continuation.resume(returning: (true,loginErrorMessage))
                        
                    }
                    catch let error as NSError {
                        
                        print("Catch Error : \(error.code) -> ", error.localizedDescription)

                            // Extract the status code from the message
                            let components = error.localizedDescription.components(separatedBy: ":")
                            print("Error Components -> \(components)")
                            if let statusCodeString = components.first?.trimmingCharacters(in: .whitespaces),
                                    let statusCode = Int(statusCodeString) {
                                       let message = loginErrorMessage(for: statusCode)
                                        self.loginErrorMessage = message
                                        continuation.resume(returning: (false,loginErrorMessage))
                            }
                            else {
                                self.loginErrorMessage = "Server Error! Please try again later! "
                                continuation.resume(returning: (false,loginErrorMessage))
                            }
                           
                       

                    }
                }
            }
        }
    }
    
    // Get user who is logged in
    func getUserData(token : String) async throws -> UserData {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getUserWhoLoggedIn(token: token)
                        
                        let userData = UserData(
                            id: response.userData.id,
                            name: response.userData.name,
                            username: response.userData.username,
                            surname: response.userData.surname,
                            email: response.userData.email,
                            mobileNumber: response.userData.mobileNumber,
                            gender: response.userData.gender,
                            birthDate: response.userData.birthDate,
                            enabled: response.userData.enabled,
                            superAdmin: response.userData.superAdmin,
                            note: response.userData.note,
                            projectRoles: response.userData.projectRoles,
                            departmentRoles: response.userData.departmentRoles,
                            zones: response.userData.zones,
                            departments: response.userData.departments,
                            verifications: response.userData.verifications,
                            currentRole: response.userData.currentRole,
                            notificationPreferences: response.userData.notificationPreferences ,
                            isAvailable: response.userData.isAvailable,
                            createdDate: response.userData.createdDate
                            
                        )
                        self.userSpecs = userData
                        continuation.resume(returning: userData)
                        print("User Who LoggedIn Data : \(String(describing: self.userSpecs))")
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                    
                }
            }
        }
    }


    // Sent Otp
    func sendOtp(userId : String) async throws -> Bool {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().setOTPApi(userid: userId)
                        print("Received Data -> \(response)")

                        if let pairResponse = response as? KotlinPair<AnyObject,AnyObject> {
                            let firstValue =  pairResponse.first
                            let secondValue = pairResponse.second

                            if let httpStatus = firstValue as? Ktor_httpHttpStatusCode {
                                let httpStatusString = String(describing: httpStatus)
                                print("HTTP Status: \(httpStatusString)")

                                if httpStatusString == "200 OK" {
                                    print("Sent Otp -> Otp Sent Successfully!")
                                    continuation.resume(returning: true)
                                }
                                else {
                                    print("Failed with status: \(httpStatusString)")
                                    continuation.resume(returning: false)
                                }
                            }
                            else {
                                print("Unexpected first value : \(String(describing: firstValue))")
                                continuation.resume(returning: false)
                            }

                        }
                        else {
                            // Handle case where response is not a KotlinPair
                            print("Unexpected response type: \(type(of: response))")
                            continuation.resume(returning: false)
                        }

                    }
                    catch let error as NSError {
                        print("Sent Error", error.localizedDescription)
                        continuation.resume(returning: false)
                    }

                }
            }
        }
    }

    // Verify Otp
    func verifyOtp(requestBody : VerifyOTP) async throws -> Bool {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().verifyOTP(requestBody: requestBody)
                        
                        print("Otp Received -> \(response)")
                        continuation.resume(returning: true)
                        
                    }
                    catch let error as NSError {
                        print("Sent Error", error.localizedDescription)
                        if error.domain == NSURLErrorDomain {
                            switch error.code {
                            case NSURLErrorNotConnectedToInternet :
                                continuation.resume(throwing: ApiError.networkFailure)

                            case NSURLErrorTimedOut :
                                continuation.resume(throwing: ApiError.lowInternetConnection)

                            default :
                                continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                            }
                        }
                        else {
                            continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                        }
                    }
                }
            }
        }
    }
    // --------------------------------------------------------------------------------------

    
    // Login With Otp Api's ViewModel--------------------------------------------------------
    
    // Login with Phone Otp
    func loginWithOtp(phoneNumber : String) async throws -> Bool {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                   do
                   {
                       let response = try await ApiServices().loginWithPhone(phone: phoneNumber)
                       
                       if response as! Bool {
                           print("OTP Sent Successfully!")
                           continuation.resume(returning: response as! Bool)
                       }
                       else {
                           print("Sending OTP Failed!")
                           continuation.resume(returning: response as! Bool)
                       }
                   }
                   catch let error as NSError {
                        print("Sent Error", error.localizedDescription)
                        if error.domain == NSURLErrorDomain {
                            switch error.code {
                            case NSURLErrorNotConnectedToInternet :
                                continuation.resume(throwing: ApiError.networkFailure)

                            case NSURLErrorTimedOut :
                                continuation.resume(throwing: ApiError.lowInternetConnection)

                            default :
                                continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                            }
                        }
                        else {
                            print("OTP Sent Error -> \(error.localizedDescription)")
                            continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                        }
                    }
                    
                }
            }
        }
    }
    
    // Authenticate the Login OTP / Verify
    func authenticateOtp(phoneNumber : String , otpValue : String) async throws -> Bool{
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().authenticateLoginOTP(phone: phoneNumber, otp: otpValue)
                        
                        let authToken = response.token
                        saveToken(token: authToken)
                        
                        let authRefreshToken = response.refreshToken
                        saveRefreshToken(refreshToken: authRefreshToken)
                        
                        let userData = UserData(
                            id: response.userData.id,
                            name: response.userData.name,
                            username: response.userData.username,
                            surname: response.userData.surname,
                            email: response.userData.email,
                            mobileNumber: response.userData.mobileNumber,
                            gender: response.userData.gender,
                            birthDate: response.userData.birthDate,
                            enabled: response.userData.enabled,
                            superAdmin: response.userData.superAdmin,
                            note: response.userData.note,
                            projectRoles: response.userData.projectRoles,
                            departmentRoles: response.userData.departmentRoles,
                            zones: response.userData.zones,
                            departments: response.userData.departments,
                            verifications: response.userData.verifications,
                            currentRole: response.userData.currentRole,
                            notificationPreferences: response.userData.notificationPreferences ,
                            isAvailable: response.userData.isAvailable,
                            createdDate: response.userData.createdDate
                            
                        )
                        
                        self.userSpecs = userData
                        print("UserData : \(String(describing: self.userSpecs))")
                        continuation.resume(returning: true)
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    
    // ---------------------------------------------------------xx----------------------------------------------------------------
    
    
    

    // Get In-App Notifications
    func getNotifications(token:String, projectID : String) async throws -> [GetNotificationsResponse]{
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getNotifications(token: token, projectId: projectID)
                        
                        if !response.isEmpty {
                            print("Notifications List Fetched -> \(response)")
                            continuation.resume(returning: response)
                        }
                        else {
                            print("No New Notifications")
                            continuation.resume(returning: [])
                        }
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    // Module 2 - Sales Tools Filter Apis / Policy Rates Get Api -----------------------------------------------------
    // Read all Policy Rates
    @Published var policyRatesData : [PolicyRateData] = []
    func getPolicyRates(token : String) async throws -> [PolicyRateData] {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getPolicyRates(token: token)
                        
                        if !response.data.isEmpty {
                            self.policyRatesData = response.data
                            continuation.resume(returning: response.data)
                        }
                        else {
                            print("Policy Rate Data is empty!")
                            continuation.resume(returning: [])
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    // Search Policy Rates
    func searchPolicyRates(token : String, searchPayload : SearchPolicyRatePayload) async throws -> Bool{
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().searchPolicyRateData(token: token, searchData: searchPayload)
                        if !response.items.isEmpty{
                            self.policyRatesData = response.items
                            print("Filtered Policy Rate Data is Fetched!")
                            continuation.resume(returning: true)
                        }
                        else {
                            print("Filtered Policy Rate Data is empty!")
                            continuation.resume(returning: false)
                        }
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    // Get Single Policy Rate using Policy Rate ID
    @Published var singlePolicyRate : PolicyRateData?
    func getSinglePolicyRate(token : String, id : String) async throws -> PolicyRateData {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getPolicyRateDataUsingId(token: token, policyRateId: id)
                        self.singlePolicyRate = response
                        continuation.resume(returning: response)
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }

    // Read all vehicle types
    @Published var vehicleTypes : [VehicleData] = []
    func getVehicleTypes(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getVehicleTypes(token: token)
                        if ((response.data?.isEmpty) != nil) {
                            guard let tempList = response.data else {
                                return
                            }
                            self.vehicleTypes = tempList
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Vehicle Type Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    // Read all Fuel Types
    @Published var fuelTypes : [FuelTypeData] = []
    func getFuelTypes(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getFuelTypes(token: token)
                        if ((response.data?.isEmpty) != nil) {
                            guard let tempList = response.data else {
                                return
                            }
                            self.fuelTypes = tempList
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Fuel Type Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    // Read all States Data
    @Published var getAllStatesData : [StatesData] = []
    func getAllStates(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getAllStates(token: token)
                        if ((response.data?.isEmpty) != nil) {
                            guard let tempList = response.data else {
                                return
                            }
                            self.getAllStatesData = tempList
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Fuel Type Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    
    // Read all City Category Data
    @Published var getAllCityCategories : [CityCategoryData] = []
    func getAllCityCategories(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getAllCityCategory(token: token)
                        if ((response.data?.isEmpty) != nil) {
                            guard let tempList = response.data else {
                                return
                            }
                            self.getAllCityCategories = tempList
                            continuation.resume(returning: ())
                        }
                        else {
                            print("City Category Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    
    // Read all City Data
    @Published var getAllCites : [CityData] = []
    func getAllCitiesData(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getAllCities(token: token)
                        if (!response.data.isEmpty) {
                            self.getAllCites = response.data
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Cities Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    
    // Read all Insurance Types Data
    @Published var insuranceTypes : [InsuranceTypeData] = []
    func getAllInsuranceTypes(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getAllInsuranceTypes(token: token)
                        if (!response.data.isEmpty) {
                            self.insuranceTypes = response.data
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Insurances Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    // Read all Renewal Types Data
    @Published var renewalTypes : [RenewalTypeData] = []
    func getAllRenewalTypes(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getAllRenewalTypes(token: token)
                        if (!response.data.isEmpty) {
                            self.renewalTypes = response.data
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Renewal Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    
    
    // Read all Insurer Types Data
    @Published var insurerTypes : [InsurerData] = []
    func getAllInsurerTypes(token: String) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            DispatchQueue.main.async {
                Task {
                    do
                    {
                        let response = try await ApiServices().getAllInsurerTypes(token: token)
                        if (!response.data.isEmpty) {
                            self.insurerTypes = response.data
                            continuation.resume(returning: ())
                        }
                        else {
                            print("Insurer Data is empty!")
                            continuation.resume(returning: ())
                        }
                        
                    }
                    catch let error as NSError {
                         print("Sent Error", error.localizedDescription)
                         if error.domain == NSURLErrorDomain {
                             switch error.code {
                             case NSURLErrorNotConnectedToInternet :
                                 continuation.resume(throwing: ApiError.networkFailure)

                             case NSURLErrorTimedOut :
                                 continuation.resume(throwing: ApiError.lowInternetConnection)

                             default :
                                 continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                             }
                         }
                         else {
                             continuation.resume(throwing: ApiError.unknownError(description: error.localizedDescription))
                         }
                     }
                }
            }
        }
    }
    // ----------------------------------------------------------------------------------------------------------------
    
     
    //---------------------------------------------------------xx----------------------------------------------------------------
     
    // Error Message Functions
    func loginErrorMessage(for statusCode: Int) -> String {
        switch statusCode {
        case 400:
            return "The credentials you’ve entered don’t match our records. Please check your credentials and try again!"
        case 401:
            return "Access is denied due to invalid credentials"
        default:
            return "Server Error, Please try again later!"
        }
    }
    
    
}




//    // Sign In Api's ------------------------------------------------------------------------
//    // Check Username Validation
//    func checkUsername(userName : String) async throws -> Bool {
//        return try await withCheckedThrowingContinuation { continuation in
//            DispatchQueue.main.async {
//                Task {
//                    do
//                    {
//                        let response = try await ApiServices().checkUsernameEndpoint(username: userName)
//                        if response as! Bool {
//                            print("Username is Unique!")
//                        }
//                        else {
//                            print("Username is already taken!")
//                        }
//                        continuation.resume(returning: response as! Bool)
//                    }
//                    catch let error as NSError {
//                        print("Sent Error", error.localizedDescription)
//                        continuation.resume(throwing: error)
//                    }
//                }
//            }
//        }
//    }
//
//    // Check Email Validation
//    func checkEmail(email : String) async throws -> Bool {
//        return try await withCheckedThrowingContinuation { continuation in
//            DispatchQueue.main.async {
//                Task {
//                    do
//                    {
//                        let response = try await ApiServices().checkEmail(email: email)
//                        if response as! Bool {
//                            print("Email-ID is Unique!")
//                        }
//                        else {
//                            print("Email-ID is already taken!")
//                        }
//                        continuation.resume(returning: response as! Bool)
//                    }
//                    catch let error as NSError {
//                        print("Sent Error", error.localizedDescription)
//                        continuation.resume(throwing: error)
//                    }
//                }
//            }
//        }
//    }
//
//    // Check Email Validation
//    func checkPhoneNumber(number : String) async throws -> Bool {
//        return try await withCheckedThrowingContinuation { continuation in
//            DispatchQueue.main.async {
//                Task {
//                    do
//                    {
//                        let response = try await ApiServices().checkPhoneNumber(phone: number)
//                        if response as! Bool {
//                            print("Phone Number is Unique!")
//                        }
//                        else {
//                            print("Phone Number is already taken!")
//                        }
//                        continuation.resume(returning: response as! Bool)
//                    }
//                    catch let error as NSError {
//                        print("Sent Error", error.localizedDescription)
//                        continuation.resume(throwing: error)
//                    }
//                }
//            }
//        }
//    }

//    //Register User Api
//    @Published var registerSpecs : RegisterResponse?
//    func registerUser(registerPayload : RegisterRequest) async throws -> RegisterResponse {
//        return try await withCheckedThrowingContinuation { continuation in
//            DispatchQueue.main.async {
//                Task {
//                    do
//                    {
//                        let response = try await ApiServices().registerApi(requestBody: registerPayload)
//
//                        let registerData = RegisterResponse(
//                            id: response.id,
//                            name: response.name,
//                            username: response.username,
//                            surname: response.surname,
//                            email: response.email,
//                            mobileNumber: response.mobileNumber,
//                            gender: response.gender,
//                            enabled: response.enabled,
//                            superAdmin: response.superAdmin,
//                            verifications: response.verifications,
//                            isAvailable: response.isAvailable,
//                            createdDate: response.createdDate
//                        )
//                        print("Registered Successfully! -> \(registerData)")
//                        self.registerSpecs = registerData
//                        continuation.resume(returning: registerData)
//                    }
//                    catch let error as NSError {
//                        print("Sent Error", error.localizedDescription)
//                        continuation.resume(throwing: error)
//                    }
//                }
//            }
//        }
//    }

// --------------------------------------------------------------------------------------
