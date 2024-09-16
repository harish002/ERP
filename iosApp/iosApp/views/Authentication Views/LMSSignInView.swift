////
////  LMSSignInView.swift
////  iosApp
////
////  Created by Tusmit Shah on 19/07/24.
////  Copyright © 2024 orgName. All rights reserved.
////
//
//import SwiftUI
//import shared
//
//struct LMSSignInView: View {
//    
//    @Binding var isSignupSheetActive : Bool
//    let width : CGFloat
//    let height : CGFloat
//    
//    @ObservedObject var selectedTabIs : GetSelectedTab
//    @ObservedObject var accessModel : AccessServiceViewModel
//    @ObservedObject var router : Router
//
//    
//    @State private var firstName = ""
//    @State private var lastName = ""
//    @State private var userName = ""
//    @State private var phoneNumber = ""
//    @State private var emailId = ""
//    @State private var password = ""
//    @State private var reEnterPassword = ""
//    
//    var body: some View {
//        VStack(spacing:0){
//            RoundedRectangle(cornerRadius: 2.5)
//                .foregroundStyle(Color(hex: "#3c3c434D"))
//                .frame(width: 36,height: 5)
//                .padding(.top,5)
//            
//            ScrollView(showsIndicators:false){
//                VStack(spacing:0){
//                    
//                    CustomHeader(
//                        isBackButtonNeeded: false,
//                        headerTitle: "Sign Up",
//                        isCloseButtonNeeded: true,
//                        onBackButtontapped: {},
//                        onCloseButtontapped: {
//                            isSignupSheetActive = false
//                        }
//                    )
//                    
//                    VStack(spacing:16){
//                        HStack{
//                            LoginTextField(
//                                text: $firstName,
//                                label: "First Name",
//                                xOffset: width * 0.1,
//                                yOffset: 29
//                            )
//                            
//                            LoginTextField(
//                                text: $lastName,
//                                label: "Last Name",
//                                xOffset: width * 0.1,
//                                yOffset: 29
//                            )
//                        }
//                        .padding(.horizontal,16)
//                        
//                        CustomTextField(
//                            text: $userName,
//                            label: "Enter Username",
//                            xOffset: width * 0.295,
//                            yOffset: 29, 
//                            accessModel: accessModel, 
//                            validationOnlyForSignUp: true
//                        )
//                        .padding(.horizontal,16)
//                        
//                        
//                        
//                        HStack{
//                            
//                            Text("🇮🇳 +91")
//                                .padding(.vertical,16)
//                                .padding(.horizontal,16)
//                                .overlay(content: {
//                                    RoundedRectangle(cornerRadius: 8)
//                                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
//                                })
//                            
//                            CustomTextField(
//                                text: $phoneNumber,
//                                label: "Enter Phone Number",
//                                keyboardType: .numberPad,
//                                xOffset: width * 0.13,
//                                yOffset: 29, 
//                                accessModel: accessModel,
//                                validationOnlyForSignUp: true
//                            )
//                            
//                        }
//                        .padding(.horizontal,16)
//                        
//                        CustomTextField(
//                            text: $emailId,
//                            label: "Enter Email ID",
//                            xOffset: width * 0.31,
//                            yOffset: 29, 
//                            accessModel: accessModel,
//                            validationOnlyForSignUp: true
//                        )
//                        .padding(.horizontal,16)
//                        
//                        
//                        CustomTextField(
//                            text: $password,
//                            label: "Create Password",
//                            xOffset: width * 0.29,
//                            yOffset: 29, 
//                            accessModel: accessModel,
//                            validationOnlyForSignUp: true
//                        )
//                        .padding(.horizontal,16)
//                        
//                        
//                        CustomSecureField(
//                            password: $reEnterPassword,
//                            label: "Re-enter Password",
//                            passwordEntered: password,
//                            keyboardType: .default,
//                            xOffset: width * 0.27,
//                            yOffset: 29
//                        )
//                        .padding(.horizontal,16)
//                        
//                        LMSCustomButton(
//                            isButtonActive: isButtonActive(),
//                            buttonTitle: "Sign Up",
//                            buttonTapped: {
//                                let registerPayload = RegisterRequest(
//                                    username: userName,
//                                    password: password,
//                                    name: firstName,
//                                    surname: lastName,
//                                    email: emailId,
//                                    mobileNumber: phoneNumber,
//                                    gender: "MALE",
//                                    optionalProjectRoles: [],
//                                    optionalDepartmentRoles: [],
//                                    projectId: accessModel.projectId
//                                )
//                                Task.init {
//                                    let result = try await accessModel.registerUser(registerPayload: registerPayload)
//                                    
//                                    if result.verifications.isEmpty{
//                                        withAnimation{
//                                            selectedTabIs._selectedTab = .verifyAccountView
//                                            selectedTabIs.isVerifyAccountTrue["Login"] = false
//                                            selectedTabIs.isVerifyAccountTrue["SignIn"] = true
//                                        }
//                                    }
//                                }
//                               
//                            }
//                        )
//                        .padding(.horizontal,16)
//                        
//                    }
//                    .padding(.top,24)
//                    
//                    HStack(spacing:5){
//                        Text("Already have an account?")
//                            .font(.custom("Gilroy-Medium", size: 14))
//                        
//                        Button(action: {
//                            selectedTabIs._selectedTab = .loginView
//                        }, label: {
//                            Text("Log in")
//                                .font(.custom("Gilroy-Medium", size: 14))
//                                .foregroundStyle(Color(hex: "#3960F6"))
//                        })
//                        
//                    }
//                    .padding(.top,12)
//                    .padding(.bottom,24)
//                    
//                    
//                   
//                   
//                }
//                
//                
//            }
//            
//            Spacer()
//            
//            TermsAndConditionsView()
//            
//        }
//        .background(Color("bottomsheet_background"))
//        
//    }
//    
//    func isButtonActive() -> Bool {
//        if !firstName.isEmpty && !lastName.isEmpty && !userName.isEmpty && !phoneNumber.isEmpty && !emailId.isEmpty && !password.isEmpty && !reEnterPassword.isEmpty {
//            if password == reEnterPassword {
//                return true
//            }
//            else {
//                return false
//            }
//        }
//        else {
//            return false
//        }
//    }
//}
//
//#Preview {
//    GeometryReader { geometry in
//        LMSSignInView(
//            isSignupSheetActive: .constant(false), 
//            width: geometry.size.width,
//            height: geometry.size.height,
//            selectedTabIs: GetSelectedTab(),
//            accessModel: AccessServiceViewModel(),
//            router: Router()
//        )
//    }
//    
//}
//
//
//struct TermsAndConditionsView : View {
//    var body: some View {
//        VStack(spacing:2){
//            HStack(spacing:2){
//                Text("By logging in I accept 1 Click Tech LMS")
//                    .font(.custom("Gilroy-Medium", size: 12))
//                
//                Text("Terms of Service, Privacy")
//                    .font(.custom("Gilroy-SemiBold", size: 12))
//                    .foregroundStyle(Color(hex: "#3960F6"))
//                    
//                
//            }
//            .lineLimit(1)
//            .minimumScaleFactor(0.5)
//            
//            HStack(spacing: 2){
//                Text("Policy,")
//                    .font(.custom("Gilroy-SemiBold", size: 12))
//                    .foregroundStyle(Color(hex: "#3960F6"))
//                   
//                Text("and")
//                    .font(.custom("Gilroy-Medium", size: 12))
//                
//                Text("Honor Code.")
//                    .font(.custom("Gilroy-SemiBold", size: 12))
//                    .foregroundStyle(Color(hex: "#3960F6"))
//                   
//            }
//            .lineLimit(1)
//            .minimumScaleFactor(0.5)
//        }
//        .padding(.horizontal,16)
//        .padding(.top,16)
//        .padding(.bottom,12)
//    }
//}
