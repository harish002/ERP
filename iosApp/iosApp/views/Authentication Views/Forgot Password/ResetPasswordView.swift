//
//  ResetPasswordView.swift
//  iosApp
//
//  Created by Tusmit Shah on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ResetPasswordView: View {
    
    @Binding var isSignupSheetActive : Bool
    let width : CGFloat
    let height : CGFloat
    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel
    
    @State private var password = ""
    @State private var reEnterPassword = ""
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
                        headerTitle: "Reset Your Password",
                        isCloseButtonNeeded: true,
                        onBackButtontapped: {
                            selectedTabIs._selectedTab = .loginView
                        },
                        onCloseButtontapped: {
                            isSignupSheetActive = false
                        }
                    )
                    
                    VStack(alignment:.center,spacing:6){
                        Text("Please set a new password")
                            .font(.custom("Gilroy-SemiBold", size: 20))
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                        
                        Text("Set password using 8-20 characters, at least 1 number, 1 letter and 1 special character.")
                            .font(.custom("Gilroy-SemiBold", size: 12))
                            .foregroundStyle(Color(hex: "#949494"))
                            .multilineTextAlignment(.center)
                    }
                    .padding(.horizontal,16)
                    .padding(.top,36)
                    .padding(.bottom,24)
                    
                    
                    VStack(spacing:16){
                        
                        CustomTextField(
                            text: $password ,
                            label: "Create Password",
                            xOffset: width * 0.265,
                            yOffset: 27, 
                            accessModel: accessModel, 
                            validationOnlyForSignUp: true
                        )
                        .padding(.horizontal,16)
                        
                        CustomSecureField(
                            password: $reEnterPassword,
                            label: "Re-enter Password", 
                            passwordEntered: password,
                            keyboardType: .default,
                            xOffset: width * 0.25,
                            yOffset: 29
                        )
                        .padding(.horizontal,16)
                        
                        
                    }
                    .padding(.bottom,24)
                    
                    
                    VStack{
                        LMSCustomButton(
                            isButtonActive: true,
                            buttonTitle: "Next",
                            buttonTapped: {
                                selectedTabIs._selectedTab = .loginView
                            }
                        )
                        
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,12)
                    
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
        ResetPasswordView(
            isSignupSheetActive: .constant(false),
            width:  geometry.size.width,
            height:  geometry.size.height,
            selectedTabIs: GetSelectedTab(), 
            accessModel: AccessServiceViewModel()
        )
    }
}
