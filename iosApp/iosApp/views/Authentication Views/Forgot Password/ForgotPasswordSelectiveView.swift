//
//  ForgotPasswordSelectiveView.swift
//  iosApp
//
//  Created by Tusmit Shah on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ForgotPasswordSelectiveView: View {
    
    @Binding var isSignupSheetActive : Bool
    let width : CGFloat
    let height : CGFloat
    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel
    
    @State private var credentialValue = ""
    @State private var placeHolder = "Enter Phone Number"
    
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
                        headerTitle: "Forgot Password",
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
                        isButtonActive: true,
                        buttonTitle: "Get Password Reset Code",
                        buttonTapped: {
                            selectedTabIs._selectedTab = .otpSentSuccess
                            selectedTabIs.isResetPasswordViewActive = true
                            selectedTabIs.isOtpSentFrom["ForgotPassword"] = true
                            selectedTabIs.isOtpSentFrom["LoginWithOtp"] = false

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
    GeometryReader{geonetry in
        ForgotPasswordSelectiveView(
            isSignupSheetActive: .constant(false),
            width: geonetry.size.width,
            height: geonetry.size.height,
            selectedTabIs: GetSelectedTab(), 
            accessModel: AccessServiceViewModel()
        )
    }
}
