//
//  LMSAuthorizationButton.swift
//  iosApp
//
//  Created by Tusmit Shah on 17/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LMSAuthorizationButton: View {
    
    let buttonTitle : String
    let imageName : String = "Google"
    let authorizationButtonTapped : () -> Void
    
    var body: some View {
        VStack{
            Button(action: {
                authorizationButtonTapped()
            }, label: {
                ZStack{
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                        .frame(maxWidth:.infinity, maxHeight: 51 )
                        
                        .overlay(content: {
                            RoundedRectangle(cornerRadius: 8)
                                .foregroundStyle(Color("button_background"))
                                .shadow(color: Color(hex: "#000004"), radius: 1.5)
                        })
                    
                    HStack(alignment:.center,spacing: 16){
                        
                        Text(buttonTitle)
                            .padding(.vertical,20)
                            .font(.custom("Gilroy-SemiBold", size: 16))
                            .foregroundStyle(Color("textvalue"))
                    }
                    
                }
                
            })
        }
        .padding(.horizontal,16)
    }
}


struct LMSAuthorizationButtonWithIcon: View {
    
    let buttonTitle : String
    let imageName : String
    let authorizationButtonTapped : () -> Void
    
    var body: some View {
        VStack{
            Button(action: {
                authorizationButtonTapped()
            }, label: {
                ZStack{
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                        
                        .frame(maxWidth:.infinity, maxHeight: 51 )
                        .padding(.horizontal,1.6)
                        .zIndex(1)
                    
                    RoundedRectangle(cornerRadius: 9)
                        .stroke(Color(hex: "#F1F1F1"),lineWidth: 4)
                        .frame(maxWidth:.infinity, maxHeight: 54 )
                        .zIndex(0)
                
                    
                    HStack(alignment:.center,spacing: 16){
                        
                        Image(imageName)
                        
                        Text(buttonTitle)
                            .padding(.vertical,20)
                            .font(.custom("Gilroy-SemiBold", size: 16))
                            .foregroundStyle(Color("textvalue"))
                    }
                    
                }
                
            })
        }
        .padding(.horizontal,16)
    }
}

#Preview {
    LMSAuthorizationButtonWithIcon(
        buttonTitle: "Get OTP on Email ID",
        imageName: "phone-02"
    ){
        
    }
}
