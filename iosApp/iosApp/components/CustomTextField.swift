//
//  CustomTextField.swift
//  iosApp
//
//  Created by Tusmit Shah on 15/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CustomTextField: View {
    
    //Data to be needed
    @Binding var text : String
    let label : String
    var keyboardType : UIKeyboardType = .default
    let xOffset : CGFloat
    let yOffset : CGFloat
    @ObservedObject var accessModel : AccessServiceViewModel
    let validationOnlyForSignUp : Bool

    @Namespace var animation
    
    @State private var errorMessage : String = ""
    
    // If there is any need of Error Message to show
    @State private var isErrorMessageNeeded : Bool = false
    
    // To Display the Error Signal / Icon on Regex False
    @State private var isValidationTrue : [String : Bool] = [:]
    
    @State private var isFocused = false
    
    @State private var timer : Timer?
    
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
                    .overlay(alignment: .trailing, content: {
                        if validationOnlyForSignUp {
                            if !text.isEmpty {
                                if isValidationTrue[label] ?? true{
                                    Circle()
                                        .frame(width: 18, height: 18)
                                        .foregroundStyle(Color(hex: "#13B15D"))
                                        .padding(.trailing,16)
                                        .overlay(content: {
                                            Image("tick")
                                                .padding(.trailing,16)
                                        })
                                }
                                else {
                                    Circle()
                                        .frame(width: 18, height: 18)
                                        .foregroundStyle(Color(hex: "#F63939"))
                                        .padding(.trailing,16)
                                        .overlay(content: {
                                            Image("cross")
                                                .padding(.trailing,16)
                                        })
                                }
                            }
                        }
                    })
                    .zIndex(0)
                    .onChange(of: text){newValue in
//                        if validationOnlyForSignUp {
//                            handleValidations(label: label, value: newValue)
//                        }
                    }

            }
            .onTapGesture {
                withAnimation(.spring(response: 0.5,dampingFraction: 0.8)){
                    isFocused = true
                }
            }
            if !text.isEmpty{
                if isErrorMessageNeeded {
                    Text(errorMessage)
                        .font(.custom("Gilroy-Medium", size: 12))
                        .foregroundStyle(Color(hex: "#F63939"))
                }
            }
            
        }
        
    }
//    
//    // Validation Function
//    func handleValidations(label : String, value : String){
//        switch label {
//            
//        case "Enter Username" :
//            let validatedValue = isValidUsername(input: value)
//            
//            if validatedValue {
//                timer?.invalidate()
//                timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false, block: {_ in
//                    handleUsernameApiCall(label: label, username: value)
//                })
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = false
//                }
//            }
//           
//        case "Enter Phone Number" : 
//            let validatedValue = isNumberValid(number: value)
//            if validatedValue {
//                timer?.invalidate()
//                timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false, block: {_ in
//                    handlePhoneNumberApiCall(label: label, phoneNumber: value)
//                })
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = false
//                }
//            }
//            
//        case "Enter Email ID" : 
//            let validatedValue = isEmailValid(value)
//            if validatedValue {
//                timer?.invalidate()
//                timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false, block: {_ in
//                    handleEmailApiCall(label: label, email: value)
//                })
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = false
//                }
//            }
//            
//        case "Create Password" :
//            let validateValue = isPasswordValid(value)
//            if validateValue {
//                withAnimation{
//                    isValidationTrue[label] = true
//                    isErrorMessageNeeded = false
//                }
//            
//                errorMessage = ""
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = false
//                    isErrorMessageNeeded = true
//                }
//                errorMessage = "Improve your password using 8-20 characters, at least 1 number, 1 letter and 1 special character."
//            }
//            
//        default: isValidationTrue[label] = true
//        }
//    }
    
    
//    // Api Call for Username Validation
//    func handleUsernameApiCall(label : String,username : String){
//        Task.init{
//            let result = try await accessModel.checkUsername(userName: username)
//            if result == false {
//                withAnimation{
//                    isValidationTrue[label] = false
//                    isErrorMessageNeeded = true
//                }
//                self.errorMessage = "This username is already taken."
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = true
//                    isErrorMessageNeeded = false
//                }
//                self.errorMessage = ""
//            }
//        }
//    }
//
//    // Api Call for Email Validation
//    func handleEmailApiCall(label : String,email : String){
//        Task.init{
//            let result = try await accessModel.checkEmail(email: email)
//            if result == false {
//                withAnimation{
//                    isValidationTrue[label] = false
//                    isErrorMessageNeeded = true
//                }
//                self.errorMessage = "This email-id is already taken."
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = true
//                    isErrorMessageNeeded = false
//                }
//                self.errorMessage = ""
//            }
//        }
//    }
//
//    // Api Call for Phone Number Validation
//    func handlePhoneNumberApiCall(label : String,phoneNumber : String){
//        Task.init{
//            let result = try await accessModel.checkPhoneNumber(number: phoneNumber)
//            if result == false {
//                withAnimation{
//                    isValidationTrue[label] = false
//                    isErrorMessageNeeded = true
//                }
//                self.errorMessage = "This Phone Number is already taken."
//            }
//            else {
//                withAnimation{
//                    isValidationTrue[label] = true
//                    isErrorMessageNeeded = false
//                }
//                self.errorMessage = ""
//            }
//        }
//    }

    
}

#Preview {
    CustomTextField(
        text: .constant(""),
        label: "Enter Username",
        xOffset: 125,
        yOffset: 29,
        accessModel: AccessServiceViewModel(), 
        validationOnlyForSignUp: true
    )
    
    
}
