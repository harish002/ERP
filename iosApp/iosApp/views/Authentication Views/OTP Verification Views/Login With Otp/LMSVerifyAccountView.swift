//
//  VerifyAccountView.swift
//  iosApp
//
//  Created by Tusmit Shah on 22/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LMSVerifyAccountView: View {
    
    let width : CGFloat
    let height : CGFloat
    
    @State private var emailId : String = ""
    @State private var phoneNumber : String = ""
    @State private var userId : String = ""
    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel

    @ObservedObject var snackBar : SnackbarModel

    var body: some View {
        VStack(spacing:0){
            
            RoundedRectangle(cornerRadius: 2.5)
                .foregroundStyle(Color(hex: "#3c3c434D"))
                .frame(width: 36,height: 5)
                .padding(.top,5)
            
            CustomHeader(
                isBackButtonNeeded: true,
                headerTitle: "",
                isCloseButtonNeeded: false,
                onBackButtontapped: {
                    if selectedTabIs.isVerifyAccountTrue["Login"] == true {
                        selectedTabIs._selectedTab = .loginView
                    }
                    else if selectedTabIs.isVerifyAccountTrue["SignIn"] == true {
                        selectedTabIs._selectedTab = .signInView
                    }
                   
                },
                onCloseButtontapped: {}
            )
            
            Spacer()
            
            VStack{
                Image("verification")
                
                Text("Verify your account")
                    .font(.custom("Gilroy-SemiBold", size: 36))
                    .foregroundStyle(Color(hex: "#4E4E4E"))
                    
            }
            .padding(.horizontal,16)
            .padding(.bottom,12)
            
            VStack(alignment:.center,spacing:5){
                Text("We’ll send a verification code to either")
                    .font(.custom("Gilroy-SemiBold", size: 14))
                    .foregroundStyle(Color(hex: "#4E4E4E"))
                
                HStack(spacing:5){
                    
                    Text(emailId)
                        .font(.custom("Gilroy-SemiBold", size: 14))
                        .foregroundStyle(Color(hex: "#3960F6"))
                    
                    Text("or")
                        .font(.custom("Gilroy-Medium", size: 14))
                        .foregroundStyle(Color(hex: "#4E4E4E"))
                    
                    Text("(+91) \(phoneNumber)")
                        .font(.custom("Gilroy-SemiBold", size: 14))
                        .foregroundStyle(Color(hex: "#3960F6"))
                }
                
                Text("Please select the platform below where you want us to send the verification code.")
                    .font(.custom("Gilroy-SemiBold", size: 14))
                    .foregroundStyle(Color(hex: "#4E4E4E"))
                    .multilineTextAlignment(.center)
                    
                
            }
            .padding(.horizontal,16)
            
            
           
            Spacer()
            
            VStack(spacing:0){
                LMSAuthorizationButton(buttonTitle: "Get OTP on Email ID"){
                    withAnimation(.spring(response: 0.5, dampingFraction: 0.6)){
                        selectedTabIs._selectedTab = .verifyOtpView
                    }
                }
                .padding(.top,12)
                .padding(.bottom,12)
                
                LMSAuthorizationButton(buttonTitle: "Get OTP on text message"){
                    Task {
                        if !userId.isEmpty{
                            let result = try await accessModel.sendOtp(userId: self.userId)
                            if result {
                                snackBar.show(message: "We've sent a verification code.", title: "OTP Verification", type: .info)
                                withAnimation(.spring(response: 0.5, dampingFraction: 0.6)){
                                    selectedTabIs._selectedTab = .verifyOtpView
                                }
                            }
                            else {
                                snackBar.show(message: "Failed to Send OTP", title: "Error", type: .error)
                            }
                        }
                        else {
                            print("UserId is Empty!")
                        }
                    }
                   
                }
                .padding(.bottom,48)
            }
            
        }
        .frame(maxWidth: .infinity)
        .background(Color("bottomsheet_background"))
        .onReceive(accessModel.$userSpecs, perform: { userData in
            guard let number = userData?.mobileNumber else {
                return
            }
            guard let email = userData?.email else {
                return
            }
            guard let id = userData?.id else {
                return
            }
            
            self.phoneNumber = number
            self.emailId = email
            self.userId = id
        })
   
    }
}

#Preview {
    
    GeometryReader { geometry in
        LMSVerifyAccountView(
            width: geometry.size.width,
            height: geometry.size.height,
            selectedTabIs: GetSelectedTab(),
            accessModel: AccessServiceViewModel(), 
            snackBar: SnackbarModel()
        )
    }
}
