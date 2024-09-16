//
//  LMSVerifyOtpView.swift
//  iosApp
//
//  Created by Tusmit Shah on 22/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

enum Field : Hashable {
    case first, second, third, fourth, fifth, sixth
}

struct LMSVerifyOtpView: View {
    
    @ObservedObject var selectedTabIs : GetSelectedTab
    @Binding var isSignUpSheetActive : Bool
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router

    @ObservedObject var snackBar : SnackbarModel

    @State private var isResetPasswordActive = false
    @State private var firstDigit : String = ""
    @State private var secondDigit : String = ""
    @State private var thirdDigit : String = ""
    @State private var fourthDigit : String = ""
    @State private var fifthDigit : String = ""
    @State private var sixthDigit : String = ""
    
    @State private var phoneNumber : String = ""
    
    @FocusState private var focusField : Field?
    
    @State private var isButtonActive : Bool = false
    
    @State private var loginWithOtpCredentialValue = ""
    
    var body: some View {
        VStack(spacing:0){
            RoundedRectangle(cornerRadius: 2.5)
                .foregroundStyle(Color(hex: "#3c3c434D"))
                .frame(width: 36,height: 5)
                .padding(.top,5)
            
            CustomHeader(
                isBackButtonNeeded: true,
                headerTitle: "Verify with OTP",
                isCloseButtonNeeded: true,
                onBackButtontapped: {
                    withAnimation(.spring(response: 0.5, dampingFraction: 0.6)){
                        if selectedTabIs.isOtpSentFrom["LoginWithOtp"] == true   {
                            selectedTabIs._selectedTab = .loginWithOtpView
                        }
                        else if selectedTabIs.isOtpSentFrom["ForgotPassword"] == true {
                            selectedTabIs._selectedTab = .forgotPasswordView
                        }
                        else {
                            selectedTabIs._selectedTab = .verifyAccountView
                        }
                    }
                },
                onCloseButtontapped: {
                    isSignUpSheetActive = false
                }
            )
            
            VStack(spacing:6){
                Text("Please enter the OTP sent to you")
                    .font(.custom("Gilroy-SemiBold", size: 20))
                    .foregroundStyle(Color("textvalue"))
                
                HStack(spacing:3){
                    Text("Didn’t receive the OTP?")
                        .font(.custom("Gilroy-SemiBold", size: 12))
                        .foregroundStyle(Color(hex: "#949494"))
                    
                    Text("Check your credentials again!")
                        .font(.custom("Gilroy-Bold", size: 12))
                        .foregroundStyle(Color(hex: "#3960F6"))
                }
                
            }
            .padding(.top,36)
            .padding(.horizontal,16)
            
            HStack(spacing:14){
                EnterOtpTextField( otpDigit: $firstDigit){newValue in
                    if newValue.count >= 1 {
                        focusField = .second
                    }
                } onDeleteBackward: {
                    focusField = .first
                }
                .focused($focusField,equals: .first)
                .textContentType(.oneTimeCode)
                .contentShape(Rectangle())
                .onTapGesture {
                    focusField = .first
                }
                
                EnterOtpTextField( otpDigit: $secondDigit ){newValue in
                    if newValue.count >= 1 {
                        focusField = .third
                    }
                    else {
                        focusField = .first
                    }
                } onDeleteBackward: {
                    focusField = .first
                }
                .focused($focusField,equals: .second)
                .textContentType(.oneTimeCode)
                
                EnterOtpTextField( otpDigit: $thirdDigit ){newValue in
                    if newValue.count >= 1{
                        focusField = .fourth
                    }
                    else {
                        focusField = .second
                    }
                } onDeleteBackward: {
                    focusField = .second
                }
                .focused($focusField,equals: .third)
                .textContentType(.oneTimeCode)

                
                EnterOtpTextField( otpDigit: $fourthDigit ){newValue in
                    if newValue.count >= 1 {
                        focusField = .fifth
                    }
                    else {
                        focusField = .third
                    }
                } onDeleteBackward: {
                    focusField = .third
                }
                .focused($focusField,equals: .fourth)
                .textContentType(.oneTimeCode)

                
                EnterOtpTextField( otpDigit: $fifthDigit ){newValue in
                    if newValue.count > 0 && newValue.count < 2 {
                        focusField = .sixth
                    }
                    else {
                        focusField = .fourth
                    }
                } onDeleteBackward: {
                    focusField = .fourth
                }
                .focused($focusField,equals: .fifth)
                .textContentType(.oneTimeCode)

                
                EnterOtpTextField( otpDigit: $sixthDigit ){newValue in
                    if newValue.count < 1 {
                        focusField = .fifth
                    } 
                    else {
                        hideKeyboard()
                    }
                    isButtonActive = handleButtonValidation()
                    
                } onDeleteBackward: {
                    focusField = .fifth
                }
                .focused($focusField,equals: .sixth)
                .textContentType(.oneTimeCode)

              
            }
            .padding(.horizontal,16)
            .padding(.top,24)
                        
            
            Spacer()
            
            VStack(spacing:0){
                LMSCustomButton(
                    isButtonActive: isButtonActive,
                    buttonTitle: isResetPasswordActive ? "Reset Password" : "Get Started"
                ){
                    if isResetPasswordActive {
                        selectedTabIs._selectedTab = .resetPasswordView
                    }
                    else {
                        if selectedTabIs.isOtpSentFrom["LoginWithOtp"] == true {
                            handleOtpAuthentication()
                        }
                        else {
                            handleOtpVerification()
                        }
                    }
                }
                .padding(.horizontal,16)
                .padding(.vertical,12)
                
                HStack(alignment:.center,spacing:8){
                    Image("lock-01")
                    
                    Text("Don’t worry, we won’t share your details anywhere.")
                        .font(.custom("Gilroy-SemiBold", size: 12))
                        .foregroundStyle(Color("textvalue"))
                }
                .padding(.top,8)
                .padding(.bottom,12)
                
            }
        }
        .onAppear{
            if selectedTabIs.isResetPasswordViewActive {
                isResetPasswordActive = true
            }
        }
        .onReceive(accessModel.$userSpecs, perform: { userData in
            guard let number = userData?.mobileNumber else {
                return
            }
            self.phoneNumber = number
        })
        .onReceive(accessModel.$loginWithOtpCredential, perform: {value in
            if !value.isEmpty {
                self.loginWithOtpCredentialValue = value
            }
            else {
                print("Couldn't Receive the Credential!")
            }
        })
        
        
    }
    
    func handleButtonValidation() -> Bool {
        return !(firstDigit.isEmpty) && !(secondDigit.isEmpty) && !(thirdDigit.isEmpty)  && !(fourthDigit.isEmpty) && !(fifthDigit.isEmpty) && !(sixthDigit.isEmpty)
    }
    
    // Verifying the Login and Sign In Page OTP's
    func handleOtpVerification() {
        let otpEntered = "\(firstDigit)\(secondDigit)\(thirdDigit)\(fourthDigit)\(fifthDigit)\(sixthDigit)"
        let verifyPayload = VerifyOTP( mobileNumber: phoneNumber, otp: otpEntered)
        Task.init{
            do {
                let result = try await accessModel.verifyOtp(requestBody: verifyPayload )
                if result {
                    withAnimation{
                        isSignUpSheetActive = false
                    }
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                        snackBar.show(message: "Congratulations, your account has been successfully created.", title: "LOGIN SUCCESSFUL", type: .success)
                        withAnimation{
                            router.navigateTo(to: .homescreen)
                        }
                    })

                }

            }
            catch ApiError.networkFailure {
                 // Handle network failure, e.g., show error Snackbar
                 snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
             } catch ApiError.lowInternetConnection {
                 // Handle low internet connection, e.g., show error Snackbar
                 snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
             } catch ApiError.serverError(let status) {
                 // Handle server errors, e.g., show error Snackbar
                 snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
             } catch {
                 // Handle unknown errors
                 print("OTP Verification Failed!")
                 snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
             }

        }
    }
    
    // Authenticating the Registered User and Navigating to the HomeScreen
    func handleOtpAuthentication() {
        let otpEntered = "\(firstDigit)\(secondDigit)\(thirdDigit)\(fourthDigit)\(fifthDigit)\(sixthDigit)"
        Task.init{
            do
            {
                let result = try await accessModel.authenticateOtp(phoneNumber: loginWithOtpCredentialValue, otpValue: otpEntered)
                
                if result {
                    withAnimation{
                        isSignUpSheetActive = false
                    }
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                        snackBar.show(message: "LOGIN SUCCESSFUL", title: "Success", type: .success)
                        withAnimation{
                            router.navigateTo(to: .homescreen)
                        }
                    })
                }
            }
            catch ApiError.networkFailure {
                 // Handle network failure, e.g., show error Snackbar
                 snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
             } catch ApiError.lowInternetConnection {
                 // Handle low internet connection, e.g., show error Snackbar
                 snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
             } catch ApiError.serverError(let status) {
                 // Handle server errors, e.g., show error Snackbar
                 snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
             } catch ApiError.unknownError(let description){
                 // Handle unknown errors
                 print("OTP Verification Failed!")
                 if let data = description.data(using: .utf8),
                    let dictionary = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] {
                     let errorMessage = dictionary["message"] as? String
                     print("Login Failed -> \(String(describing: errorMessage))")
                     
                     // Extract the status code from the message
                     let components = errorMessage?.components(separatedBy: ":")
                     if let statusCodeString = components?.first?.trimmingCharacters(in: .whitespaces),
                             let statusCode = Int(statusCodeString) {
                                let message = authenticationError(for: statusCode)
                                snackBar.show(message: message, title: "Error", type: .error)
                     }
                     else {
                            snackBar.show(message: "Internal Server Error.", title: "Error", type: .error)
                     }
                    
                 }
                 else {
                            snackBar.show(message: "JSON Serilization Failed..Please try again later! ", title: "Error", type: .error)
                 }
             }
            
        }
    }
    
    // Error Message Functions
    func authenticationError(for statusCode: Int) -> String {
        switch statusCode {
        case 400:
            return "Invalid OTP"
            
        case 401:
            return "Access is denied due to invalid credentials"
        
        case 500 :
            return "Internal Server Error.."
            
        default:
            return "Unable to Connect to the Serverno  x!"
        }
    }
}

#Preview {
    LMSVerifyOtpView(
        selectedTabIs: GetSelectedTab(),
        isSignUpSheetActive: .constant(false),
        accessModel: AccessServiceViewModel(),
        router: Router(), 
        snackBar: SnackbarModel()
    )
}
