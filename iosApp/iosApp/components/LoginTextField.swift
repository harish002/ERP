//
//  LoginTextField.swift
//  iosApp
//
//  Created by Tusmit Shah on 29/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LoginTextField: View {
    //Data to be needed
    @Binding var text : String
    let label : String
    
    var keyboardType : UIKeyboardType = .default
    let xOffset : CGFloat
    let yOffset : CGFloat
    
    @Namespace var animation
    
    @State private var isFocused = false
        
    var body: some View {
        
        VStack(alignment:.leading,spacing: 8){
            ZStack{
                    if isFocused || !text.isEmpty {
                        Text(label)
                            .matchedGeometryEffect(id: "label", in: animation)
                            .font(.custom("Gilroy-Medium", size: 14))
                            .foregroundStyle(Color(hex: "#767676"))
                            .padding(6)
                            .background(Color(hex: "#F8F8F8"))
                            .offset(x:-xOffset,y: -yOffset)
                            .zIndex(1)
                        
                    }
                    else {
                        Text(label)
                            .matchedGeometryEffect(id: "label", in: animation)
                            .font(.custom("Gilroy-Medium", size: 14))
                            .foregroundStyle(Color(hex: "#767676"))
                            .padding(6)
                            .offset(x:-xOffset)
                            .zIndex(1)
                            .onTapGesture {
                                withAnimation(.spring(response: 0.5,dampingFraction: 0.8)){
                                    isFocused = true
                                }
                            }
                        
                    }
                     
                    TextField("", text: $text, onEditingChanged: {editing in
                        withAnimation(.spring(response: 0.5,dampingFraction: 0.8)){
                            isFocused = editing
                        }
                    })
                    .padding(.vertical,20)
                    .padding(.horizontal,16)
                    .font(.custom("Gilroy-SemiBold", size: 14))
                    .foregroundStyle(Color(hex: "#4E4E4E"))
                    .background(RoundedRectangle(cornerRadius: 8).stroke(Color(hex: "#D0D0D0"),lineWidth: 1))
                    .keyboardType(keyboardType)
                    .zIndex(0)
                   
            }
            .onTapGesture {
                withAnimation(.spring(response: 0.5,dampingFraction: 0.8)){
                    isFocused = true
                }
            }
            
        }
        
    }
}

#Preview {
    LoginTextField(
        text: .constant(""),
        label: "Enter Username",
        xOffset: 125,
        yOffset: 29
    )
}
