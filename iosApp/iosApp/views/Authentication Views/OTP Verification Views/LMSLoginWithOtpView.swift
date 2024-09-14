//
//  LMSLoginWithOtpView.swift
//  iosApp
//
//  Created by Tusmit Shah on 24/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LMSLoginWithOtpView: View {
    
    @Binding var isSignupSheetActive : Bool
    let width : CGFloat
    let height : CGFloat
    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel
    
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var credentialValue = ""
    @State private var placeHolder = "Enter Phone Number"
    @State private var isButtonActive = false
    
    var body: some View {
        VStack(spacing:0){
            
            RoundedRectangle(cornerRadius: 2.5)
                .foregroundStyle(Color(hex: "#3c3c434D"))
                .frame(width: 36,height: 5)
                .padding(.top,5)
            
            ScrollView{
                
                VStack(spacing:0){
                    CustomHeader(
                        isBackButtonNeeded: true,
                        headerTitle: "Log in with OTP",
                        isCloseButtonNeeded: true,
                        onBackButtontapped: {
                            selectedTabIs._selectedTab = .loginView
                        },
                        onCloseButtontapped: {
                            isSignupSheetActive = false
                        }
                    )
                    
                    VStack(alignment:.center,spacing:6){
                        Text("Please enter your credentials")
                            .font(.custom("Gilroy-SemiBold", size: 20))
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                        
                        Text("Below you can choose where you would like to receive your OTP for logging in.")
                            .font(.custom("Gilroy-SemiBold", size: 12))
                            .foregroundStyle(Color(hex: "#949494"))
                            .multilineTextAlignment(.center)
                    }
                    .padding(.horizontal,16)
                    .padding(.top,36)
                    .padding(.bottom,24)
                    
                    
                    VStack(spacing:16){
                        SelectionView(label: $placeHolder)
                        
                        CustomTextField(
                            text: $credentialValue ,
                            label: placeHolder,
                            xOffset: placeHolder == "Enter Phone Number" ? width * 0.25 : width * 0.295,
                            yOffset: 27, 
                            accessModel: accessModel, 
                            validationOnlyForSignUp: false
                        )
                        .padding(.horizontal,16)
                        
                    }
                    .padding(.bottom,24)
                    
                    
                    LMSCustomButton(
                        isButtonActive: !credentialValue.isEmpty,
                        buttonTitle: "Next",
                        buttonTapped: {
                                hideKeyboard()
                                if placeHolder == "Enter Phone Number" {
                                    
                                    Task.init{
                                        do
                                        {
                                            let result = try await accessModel.loginWithOtp(phoneNumber: credentialValue)
                                            
                                            if result {
                                                
                                                print("OTP Sent from Login With OTP")
                                                DispatchQueue.main.async{
                                                    withAnimation{
                                                        snackBar.show(message: "We've sent a verification code.", title: "OTP Verification", type: .info)
                                                    }
                                                }
                                                accessModel.loginWithOtpCredential = credentialValue
                                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                                                    withAnimation{
                                                        selectedTabIs._selectedTab = .verifyOtpView
                                                        selectedTabIs.isResetPasswordViewActive = false
                                                        selectedTabIs.isOtpSentFrom["LoginWithOtp"] = true
                                                        selectedTabIs.isOtpSentFrom["ForgotPassword"] = false
                                                    }
                                                })
                                               
                                            }
                                            else {
                                                DispatchQueue.main.async{
                                                    withAnimation{
                                                        snackBar.show(message: "Failed to Send OTP.", title: "Error", type: .error)
                                                    }
                                                }
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
                                        } catch ApiError.unknownError(let description) {
                                            // Handle unknown errors
                                            print("OTP Verification Failed!")
                                            snackBar.show(
                                                message: description,
                                                title: "Error",
                                                type: .error)
                                        }
                                    }
                                    
                                }
                                else {
                                    print("Email-Id Selected !!")
                                  
                                }

                        }
                    )
                    .padding(.horizontal,16)
                    
                }
                
                
            }
            
            Spacer()
            
            HStack(spacing:8){
                Image("lock-01")
                
                Text("Don’t worry, we won’t share your details anywhere.")
                    .font(.custom("Gilroy-Medium", size: 12))
                    .foregroundStyle(Color("textvalue"))
                
            }
        }
        .background(Color("bottomsheet_background"))
    }
}

#Preview {
    GeometryReader{geometry in
        LMSLoginWithOtpView(
            isSignupSheetActive: .constant(false), 
            width: geometry.size.width,
            height: geometry.size.height,
            selectedTabIs: GetSelectedTab(), 
            accessModel: AccessServiceViewModel(), 
            snackBar: SnackbarModel()
        )
    }
}
