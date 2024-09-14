//
//  CustomSecureField.swift
//  iosApp
//
//  Created by Tusmit Shah on 16/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

enum focusedField {
    case secure, unsecure
}

struct CustomSecureField: View {
    
    //Data to be needed
    @Binding var password : String
    let label : String
    let passwordEntered : String
    let keyboardType : UIKeyboardType
    let xOffset : CGFloat
    let yOffset : CGFloat
    
    @Namespace var animation
    
    @State private var isFocused = false
    @State private var showPassword : Bool = false
    @State private var errorMessage = ""
    @State private var  errorStatus : [String : Bool] = [:]

    var body: some View {
        VStack(alignment:.leading,spacing: 8){
            ZStack{
                    if isFocused || !password.isEmpty {
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
                        
                    }
                
                Group{
                    if showPassword {
                        TextField("", text: $password, onEditingChanged: {editing in
                            withAnimation(.spring(response: 0.5,dampingFraction: 0.8)){
                                isFocused = editing
                            }
                        })
                        .matchedGeometryEffect(id: "customTextField", in:animation )
                        .padding(.vertical,20)
                        .padding(.horizontal,16)
                        .font(.custom("Gilroy-SemiBold", size: 16))
                        .foregroundStyle(Color(hex: "#4E4E4E"))
                        .background(RoundedRectangle(cornerRadius: 8).stroke(errorStatus[label] ?? false ? Color(hex: "#F63939") : Color(hex: "#D0D0D0"),lineWidth: 1))
                        .keyboardType(keyboardType)
                        .zIndex(0)
                    }
                    else {
                        SecureField("", text: $password)
                            .matchedGeometryEffect(id: "customTextField", in:animation )
                            .padding(.vertical,20)
                            .padding(.horizontal,16)
                            .font(.custom("Gilroy-SemiBold", size: 16))
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                            .background(RoundedRectangle(cornerRadius: 8).stroke(errorStatus[label] ?? false ? Color(hex: "#F63939") : Color(hex: "#D0D0D0"),lineWidth: 1))
                            .keyboardType(keyboardType)
                            .onTapGesture {
                                withAnimation(.spring(response: 0.6,dampingFraction: 0.8)){
                                    isFocused = true
                                }
                            }
                            .zIndex(0)
                    }
                    
                }
                .overlay(alignment: .trailing, content: {
                    Button(action: {
                            showPassword.toggle()
                        
                    }, label: {
                        Image(systemName: showPassword ? "eye.fill" : "eye.slash.fill")
                            .frame(width: 18, height: 18)
                            .foregroundStyle(Color(hex: "#767676"))
                            .padding(.trailing,16)
                           
                    })
                })
                .onChange(of: password){newValue in
                    
                    switch label {
                        case "Re-enter Password" :
                        if newValue == passwordEntered {
                            errorMessage = ""
                            withAnimation{
                                errorStatus[label] = false
                            }
                        }
                        else {
                            withAnimation{
                                errorStatus[label] = true
                            }
                            errorMessage = "The password you’ve entered doesn’t match. Try again."
                        }
                        
                    default : errorStatus[label] = false
                    }
                   
                }
                

            }
            
            if !password.isEmpty {
                if errorStatus[label] ?? false {
                    Text(errorMessage)
                        .font(.custom("Gilroy-Medium", size: 12))
                        .foregroundStyle(Color(hex: "#F63939"))
                }
            }
           
            
        }

    }
}

#Preview {
  CustomSecureField(
    password: .constant(""),
    label: "Enter Password", 
    passwordEntered: "",
    keyboardType: .default,
    xOffset: 125,
    yOffset: 29)
}
