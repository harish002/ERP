//
//  CustomButtons.swift
//  iosApp
//
//  Created by Tusmit Shah on 16/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LMSCustomButton: View {
    
    let isButtonActive : Bool
    let buttonTitle : String
    let buttonTapped : () -> Void
    
    var body: some View {
        VStack{
            Button(action: {
                if isButtonActive {
                    print("\(buttonTitle) button tapped!")
                    buttonTapped()
                }
            },
            label: {
                ZStack{
                    RoundedRectangle(cornerRadius: 5)
                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                        .frame(maxWidth:.infinity, maxHeight: 51 )
                        .overlay(content: {
                            RoundedRectangle(cornerRadius: 5)
                                .foregroundStyle(Color("button_background_2")).opacity( isButtonActive ? 1 : 0.6)
                                .shadow(color: isButtonActive ? Color(hex: "#E4E4E4") : Color(hex: "#EEEEEE"), radius: 3)
                        })
                    
                    Text(buttonTitle)
                        .padding(.vertical,20)
                        .font(.custom("Gilroy-SemiBold", size: 16))
                        .foregroundStyle(Color(hex: "#FFFFFF"))
                }
                
            })
        }
                       
    }
}

#Preview {
      LMSCustomButton(
        isButtonActive: true,
        buttonTitle: "Sign Up",
        buttonTapped: {
          
      })
}
