//
//  LMSOTPSentView.swift
//  iosApp
//
//  Created by Tusmit Shah on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LMSOTPSentView: View {
    
    @ObservedObject var selectedTabIs : GetSelectedTab
    
    var body: some View {
        VStack(spacing:0){
            
            RoundedRectangle(cornerRadius: 2.5)
                .foregroundStyle(Color(hex: "#3c3c434D"))
                .frame(width: 36,height: 5)
                .padding(.top,5)
            
            VStack(spacing:0){
                
                Spacer()
                
                VStack(spacing:0){
                    Image("sentCode")
                        .padding(.bottom,32)
                    
                    Text("We’ve sent the OTP")
                        .font(.custom("Gilroy-Bold", size: 36))
                        .foregroundStyle(Color("textvalue"))
                        .padding(.bottom,12)
                        .padding(.horizontal,16)
                    
                    VStack(){
                        Text("We’ve sent the verification code to")
                            .font(.custom("Gilroy-Medium", size: 14))
                            .foregroundStyle(Color("textvalue"))
                        
                        Text("akaxxx@xxxxxxx.club")
                            .font(.custom("Gilroy-SemiBold", size: 14))
                            .foregroundStyle(Color(hex: "#3960F6"))
                        
                        Text("Do check for the code and enter it on the next screen.")
                            .font(.custom("Gilroy-Medium", size: 14))
                            .foregroundStyle(Color("textvalue"))
                            .multilineTextAlignment(.center)
                        
                    }
                    .padding(.horizontal,16)
                    
                }
                
                Spacer()
                
                VStack{
                    LMSAuthorizationButton(
                        buttonTitle: "Got the verification code",
                        authorizationButtonTapped: {
                            withAnimation{
                                selectedTabIs._selectedTab = .verifyOtpView
                            }
                        }
                    )
                    
                    Text("I didn’t get it")
                        .font(.custom("Gilroy-Medium", size: 14))
                        .foregroundStyle(Color("textvalue"))
                        .padding(.horizontal,16)
                        .padding(.top,14)
                        .padding(.bottom,48)
                        .contentShape(Rectangle())
                        .onTapGesture {
                            if selectedTabIs.isOtpSentFrom["LoginWithOtp"] == true {
                                withAnimation{
                                    selectedTabIs._selectedTab = .loginWithOtpView
                                }
                            }
                            
                            if selectedTabIs.isOtpSentFrom["ForgotPassword"] == true {
                                withAnimation{
                                    selectedTabIs._selectedTab = .forgotPasswordView
                                }
                            }
                        }
                        
                    
                }
            }
            
        }
        .background(Color("bottomsheet_background"))
    }
}

#Preview {
    LMSOTPSentView(
        selectedTabIs: GetSelectedTab()
    )
}
