////
////  VerifyResetPasswordOTPView.swift
////  iosApp
////
////  Created by Tusmit Shah on 25/07/24.
////  Copyright © 2024 orgName. All rights reserved.
////
//
//import SwiftUI
//
//struct VerifyResetPasswordOTPView: View {
//    
//    @ObservedObject var selectedTabIs : GetSelectedTab
//    
//    @State private var isResetPasswordActive = false
//    @State private var firstDigit : String = ""
//    @State private var secondDigit : String = ""
//    @State private var thirdDigit : String = ""
//    @State private var fourthDigit : String = ""
//    @State private var fifthDigit : String = ""
//    @State private var sixthDigit : String = ""
//    @FocusState private var focusField : Field?
//    
//    var body: some View {
//        VStack(spacing:0){
//            RoundedRectangle(cornerRadius: 2.5)
//                .foregroundStyle(Color(hex: "#3c3c434D"))
//                .frame(width: 36,height: 5)
//                .padding(.top,5)
//            
//            CustomHeader(
//                isBackButtonNeeded: true,
//                headerTitle: "Verify with OTP",
//                isCloseButtonNeeded: true,
//                onBackButtontapped: {
//                    withAnimation(.spring(response: 0.5, dampingFraction: 0.6)){
//                        if selectedTabIs.isOtpSentFrom["ForgotPassword"] == true {
//                            selectedTabIs._selectedTab = .otpSentSuccess
//                        }
//                    }
//                },
//                onCloseButtontapped: {
//                    
//                }
//            )
//            
//            VStack(spacing:6){
//                Text("Please enter the OTP sent to you")
//                    .font(.custom("Gilroy-SemiBold", size: 20))
//                    .foregroundStyle(Color("textvalue"))
//                
//                HStack(spacing:3){
//                    Text("Didn’t receive the OTP?")
//                        .font(.custom("Gilroy-SemiBold", size: 12))
//                        .foregroundStyle(Color(hex: "#949494"))
//                    
//                    Text("Check your credentials again!")
//                        .font(.custom("Gilroy-Bold", size: 12))
//                        .foregroundStyle(Color(hex: "#3960F6"))
//                }
//                
//            }
//            .padding(.top,36)
//            .padding(.horizontal,16)
//            HStack(spacing:14){
//                EnterOtpTextField( otpDigit: $firstDigit){newValue in
//                    if newValue.count == 1 {
//                        focusField = .second
//                    }
//                }
//                .focused($focusField,equals: .first)
//                .textContentType(.oneTimeCode)
//                
//                EnterOtpTextField( otpDigit: $secondDigit ){newValue in
//                    if newValue.count == 1 {
//                        focusField = .third
//                    }
//                }
//                .focused($focusField,equals: .second)
//                .textContentType(.oneTimeCode)
//
//                
//                EnterOtpTextField( otpDigit: $thirdDigit ){newValue in
//                    if newValue.count == 1 {
//                        focusField = .fourth
//                    }
//                }
//                .focused($focusField,equals: .third)
//                .textContentType(.oneTimeCode)
//
//                
//                EnterOtpTextField( otpDigit: $fourthDigit ){newValue in
//                    if newValue.count == 1 {
//                        focusField = .fifth
//                    }
//                }
//                .focused($focusField,equals: .fourth)
//                .textContentType(.oneTimeCode)
//
//                
//                EnterOtpTextField( otpDigit: $fifthDigit ){newValue in
//                    if newValue.count == 1 {
//                        focusField = .sixth
//                    }
//                }
//                .focused($focusField,equals: .fifth)
//                .textContentType(.oneTimeCode)
//
//                
//                EnterOtpTextField( otpDigit: $sixthDigit ){newValue in
//                    if newValue.count == 1 {
//                        focusField = .first
//                    }
//                }
//                .focused($focusField,equals: .sixth)
//                .textContentType(.oneTimeCode)
//
//              
//            }
//            .padding(.horizontal,16)
//            .padding(.top,24)
//            
//            
//            Spacer()
//            
//            VStack(spacing:0){
//                LMSCustomButton(
//                    isButtonActive: false,
//                    buttonTitle: "Reset Password"
//                ){
//                            
//                }
//                .padding(.horizontal,16)
//                .padding(.vertical,12)
//                
//                HStack(alignment:.center,spacing:8){
//                    Image("lock-01")
//                    
//                    Text("Don’t worry, we won’t share your details anywhere.")
//                        .font(.custom("Gilroy-SemiBold", size: 12))
//                        .foregroundStyle(Color("textvalue"))
//                }
//                .padding(.top,8)
//                .padding(.bottom,12)
//                
//                
//                
//            }
//        }
//    }
//}
//
//#Preview {
//    VerifyResetPasswordOTPView(
//        selectedTabIs: GetSelectedTab()
//    )
//}
