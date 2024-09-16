//
//  OTPSentFaliureView.swift
//  iosApp
//
//  Created by Tusmit Shah on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct OTPSentFaliureView: View {
    
    @ObservedObject var selectedTabIs : GetSelectedTab
    
    var body: some View {
        VStack(spacing:0){
            RoundedRectangle(cornerRadius: 2.5)
                .foregroundStyle(Color(hex: "#3c3c434D"))
                .frame(width: 36,height: 5)
                .padding(.top,5)
            
            VStack(spacing:0){
                CustomHeader(
                    isBackButtonNeeded: true,
                    headerTitle: "",
                    isCloseButtonNeeded: false,
                    onBackButtontapped: {
                        selectedTabIs._selectedTab = .loginWithOtpView
                    },
                    onCloseButtontapped: {}
                )
                
                Spacer()
                
                VStack(spacing:0){
                    Image("sentCode")
                        .padding(.bottom,32)
                    
                    Text("We weren’t able to find your credentials")
                        .font(.custom("Gilroy-Bold", size: 36))
                        .foregroundStyle(Color("textvalue"))
                        .multilineTextAlignment(.center)
                        .padding(.bottom,12)
                        .padding(.horizontal,16)
                    
                    VStack(){
                        Text("We’ve think that you haven’t connected your email address or your phone number with us. Unfortunately, you won’t be able to log in through OTP.")
                            .font(.custom("Gilroy-Medium", size: 14))
                            .foregroundStyle(Color("textvalue"))
                            .multilineTextAlignment(.center)
                        
                    }
                    .padding(.horizontal,16)
                    
                    Spacer()
                    
                    LMSAuthorizationButton(
                        buttonTitle: "Go to sign in screen",
                        authorizationButtonTapped: {
                            selectedTabIs._selectedTab = .verifyOtpView
                        }
                    )
                    .padding(.top,12)
                    .padding(.bottom,48)
                    
                    
                }
                
            }
        }
        .background(Color("bottomsheet_background"))
    }
}

#Preview {
    OTPSentFaliureView(selectedTabIs: GetSelectedTab())
}
