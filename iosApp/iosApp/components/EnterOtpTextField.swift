//
//  EnterOtpTextField.swift
//  iosApp
//
//  Created by Tusmit Shah on 22/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct EnterOtpTextField: View {
    
    @Binding var otpDigit : String
    var onChangeOfValue : (String) -> Void
    var onDeleteBackward: () -> Void
    
    var body: some View {
        ZStack{
            TextField("", text: $otpDigit)
                .padding(.vertical,20)
                .padding(.horizontal,16)
                .font(.custom("Gilroy-SemiBold", size: 16))
                .foregroundStyle(Color(hex: "#4E4E4E"))
                .multilineTextAlignment(.center)
                .frame(width: 46,height: 51)
                .background(RoundedRectangle(cornerRadius: 8).stroke(Color(hex: "#D0D0D0"),lineWidth: 1))
                .keyboardType(.numberPad)
                .onChange(of:otpDigit){newValue in
                    if newValue.isEmpty {
                       onDeleteBackward()
                    } else if newValue.count > 1 {
                        otpDigit = String(newValue.prefix(1))
                    } else {
                        onChangeOfValue(newValue)
                    }
                }
                
            
        }
    }
}

#Preview {
    EnterOtpTextField(
        otpDigit: .constant(""),
        onChangeOfValue: {_ in 
            
        }, onDeleteBackward: {
            
        }
    )
}
