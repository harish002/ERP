//
//  CustomHeader.swift
//  iosApp
//
//  Created by Tusmit Shah on 19/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CustomHeader: View {
    
    let isBackButtonNeeded : Bool
    let headerTitle : String
    let isCloseButtonNeeded : Bool
    let onBackButtontapped : () -> Void
    let onCloseButtontapped : () -> Void
    
    var body: some View {
        HStack{
            
            Image("chevron-left-normally")
                .opacity(isBackButtonNeeded ? 1 : 0)
                .contentShape(Circle())
                .onTapGesture {
                    if isBackButtonNeeded {
                        onBackButtontapped()
                    }
                }
            
            Spacer()
            
            Text(headerTitle)
                .font(.custom("Gilroy-SemiBold", size: 20))
                .foregroundStyle(Color(hex: "#262626"))
            
            Spacer()
            
            Image("close-button")
                .opacity(isCloseButtonNeeded ? 1 : 0)
                .contentShape(Circle())
                .onTapGesture {
                    if isCloseButtonNeeded {
                        onCloseButtontapped()
                    }
                }
                
        }
        .padding(.horizontal,16)
        .padding(.top,26)
        .padding(.bottom,16)
    }
}

#Preview {
      CustomHeader(
        isBackButtonNeeded: true,
        headerTitle: "Sign Up",
        isCloseButtonNeeded: true,
        onBackButtontapped: {
            
        },
        onCloseButtontapped:  {
            
        }
      )
}
