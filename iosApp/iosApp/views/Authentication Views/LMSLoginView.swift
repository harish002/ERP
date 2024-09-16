//
//  LMSLoginView.swift
//  iosApp
//
//  Created by Tusmit Shah on 23/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LMSLoginView: View {
    
    @Binding var isSignupSheetActive : Bool
    let width : CGFloat
    let height : CGFloat

    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var username = ""
    @State private var password = ""
    @State private var isLoginButtonActive = true
    
    // Error Message Display
    @State private var errorStatus = false
    @State private var inValidCredentialMessageDisplay = ""
    
    @State private var isUserVerified = false
    
    var body: some View {
        VStack(spacing:0){
            
            RoundedRectangle(cornerRadius: 2.5)
                .foregroundStyle(Color(hex: "#3c3c434D"))
                .frame(width: 36,height: 5)
                .padding(.top,5)
            
            ScrollView(showsIndicators: false){
                
                VStack(spacing:0){
                    
                    CustomHeader(
                        isBackButtonNeeded: false,
                        headerTitle: "Log in",
                        isCloseButtonNeeded: true,
                        onBackButtontapped: {},
                        onCloseButtontapped: {
                            isSignupSheetActive = false
                        }
                    )
                    
                    
                    VStack(alignment: .leading, spacing:16){
                        
                        LoginTextField(
                            text: $username,
                            label: "Enter Username",
                            xOffset: width * 0.295,
                            yOffset: 29
                        )
                        .padding(.horizontal,16)
                        
                        CustomSecureField(
                            password: $password,
                            label: "Enter Password",
                            passwordEntered: "",
                            keyboardType: .default,
                            xOffset: width * 0.295,
                            yOffset: 29
                        )
                        .padding(.horizontal,16)
                        
                        if errorStatus {
                            Text(inValidCredentialMessageDisplay)
                                .font(.custom("Gilroy-Medium", size: 12))
                                .foregroundStyle(Color(hex: "#F63939"))
                                .multilineTextAlignment(.leading)
                                .padding(.horizontal,16)
                        }
                        
                    }
                    .padding(.top,36)
                    
                    LMSCustomButton(
                        isButtonActive: !(username.isEmpty) && !(password.isEmpty),
                        buttonTitle: "Log In",
                        buttonTapped: {
                            let user = UserDetails(
                                password: password,
                                username: username
                            )
                            Task.init{
                                let (status,message) = try await accessModel.login(user: user)
                                if status {
                                    if isUserVerified {
                                        errorStatus = false
                                        snackBar.show(message: "LOGIN SUCCESSFUL", title: "Success", type: .success)
                                        isSignupSheetActive = false
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.01, execute: {
                                            withAnimation{
                                                router.navigateTo(to: .homescreen)
                                            }
                                        })
                                       
                                    }
                                    else {
                                        withAnimation{
                                            errorStatus = false
                                            selectedTabIs.isVerifyAccountTrue["Login"] = true
                                            selectedTabIs.isVerifyAccountTrue["SignIn"] = false
                                            selectedTabIs._selectedTab = .verifyAccountView
                                        }
                                    }
                                 
                                }
                                else {
                                    withAnimation{
                                        errorStatus = true
                                        self.inValidCredentialMessageDisplay = message
                                    }
                                }
                            }
                            
                           
                        }
                    )
                    .padding(.horizontal,16)
                    .padding(.vertical,16)
                    
                    Text("Forgot Username or Password?")
                        .font(.custom("Gilroy-SemiBold", size: 14))
                        .foregroundStyle(Color(hex: "#3960F6"))
                        .contentShape(Rectangle())
                        .onTapGesture {
                            selectedTabIs._selectedTab = .forgotPasswordView
                        }
                    
                    HStack(spacing:0){
                        
                        Rectangle()
                            .fill(
                                LinearGradient(gradient: Gradient(colors: [Color(hex: "#767676"), .white]), startPoint: .trailing, endPoint: .leading)
                            )
                            .frame(width: 100.5, height: 1)
                        
                        Text("OR")
                            .font(.custom("Gilroy-SemiBold", size: 14))
                            .foregroundStyle(Color("textvalue"))
                            .padding(.horizontal,12)
                        
                        Rectangle()
                            .fill(
                                LinearGradient(gradient: Gradient(colors: [Color(hex: "#767676"), .white]), startPoint: .leading, endPoint: .trailing)
                            )
                            .frame(width: 100.5, height: 1)
                        
                    }
                    .padding(.vertical,16)
                    .padding(.horizontal,16)
                    .padding(.vertical,8)
                    
                    
                    LMSAuthorizationButtonWithIcon(
                        buttonTitle: "Log In with OTP",
                        imageName: "phone-02",
                        authorizationButtonTapped: {
                            withAnimation{
                                selectedTabIs._selectedTab = .loginWithOtpView 
                            }
                        }
                    )
                    .padding(.bottom,24)
                    
                }
                
            }
            
            Spacer()
            
            TermsAndConditionsView()
            
        }
        .background(Color("bottomsheet_background"))
        .onReceive(accessModel.$userSpecs, perform: { user in
            guard let verify = user?.verifications else {
                return
            }
            if verify.isEmpty {
                isUserVerified = false
            }
            else {
                
                isUserVerified = true
                
            }
        
        })
        

        
    }
}

#Preview {
    GeometryReader { geometry in
        LMSLoginView(
            isSignupSheetActive: .constant(false),
            width: geometry.size.width,
            height: geometry.size.height,
            selectedTabIs: GetSelectedTab(), 
            accessModel: AccessServiceViewModel(),
            router: Router(), 
            snackBar: SnackbarModel()
        )
    }
}


struct TermsAndConditionsView : View {
    var body: some View {
        VStack(spacing:2){
            HStack(spacing:2){
                Text("By logging in I accept 1 Click Policy ERP")
                    .font(.custom("Gilroy-Medium", size: 12))
                
                Text("Terms of Service, Privacy")
                    .font(.custom("Gilroy-SemiBold", size: 12))
                    .foregroundStyle(Color(hex: "#3960F6"))
                    
                
            }
            .lineLimit(1)
            .minimumScaleFactor(0.5)
            
            HStack(spacing: 2){
                Text("Policy,")
                    .font(.custom("Gilroy-SemiBold", size: 12))
                    .foregroundStyle(Color(hex: "#3960F6"))
                   
                Text("and")
                    .font(.custom("Gilroy-Medium", size: 12))
                
                Text("Honor Code.")
                    .font(.custom("Gilroy-SemiBold", size: 12))
                    .foregroundStyle(Color(hex: "#3960F6"))
                   
            }
            .lineLimit(1)
            .minimumScaleFactor(0.5)
        }
        .padding(.horizontal,16)
        .padding(.top,16)
        .padding(.bottom,12)
    }
}
