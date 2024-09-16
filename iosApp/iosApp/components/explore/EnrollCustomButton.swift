//
//  EnrollCustomButton.swift
//  iosApp
//
//  Created by Tusmit Shah on 07/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct EnrollCustomButton: View {
    
    let buttonTitle : String
    let buttonTapped : () -> Void
    let buttonHorizontalPadding : CGFloat
    let buttonVerticalPadding : CGFloat
    let imageName : String
    let isLengthCardButton : Bool
    
    var body: some View {
        VStack{
            Button(action: {
                    print("\(buttonTitle) button tapped!")
                    buttonTapped()
            },
            label: {
                ZStack{
                    RoundedRectangle(cornerRadius: 8)
                        .foregroundStyle(Color("button_background_2"))
                        .frame(maxWidth:.infinity, maxHeight: 43 )
                        .padding(.horizontal,1.6)
                        .overlay(content: {
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color(hex: "#D0D0D0"), lineWidth: 1)
                                .padding(.horizontal,1.6)
                        })
                        .zIndex(1)
                    
                    RoundedRectangle(cornerRadius: 9)
                        .stroke(Color(hex: "#F1F1F1"),lineWidth: 4)
                        .frame(maxWidth:.infinity, maxHeight: 47 )
                        .zIndex(0)
                  
                    
                    
                    HStack(alignment:.center,spacing: 8){
                        Text(buttonTitle)
                            .font(.custom("Gilroy-Bold", size: 12))
                            .foregroundStyle(Color(hex: "#FFFFFF"))
                        
                        if isLengthCardButton {
                            Image(imageName)
                                .foregroundStyle(Color(hex: "#FFFFFF"))
                        }
                    }
                    .padding(.horizontal,buttonHorizontalPadding)
                    .padding(.vertical,buttonVerticalPadding)
                    .zIndex(2)
                }
                
            })
        }
                       
    }
}



struct GetDetailsButton: View {
    
    let buttonTitle : String
    let imageName : String
    let getDetailsButtonTapped : () -> Void
    
    var body: some View {
        VStack{
            Button(action: {
                getDetailsButtonTapped()
            }, label: {
                ZStack{
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                        
                        .frame(maxWidth:.infinity, maxHeight: 43 )
                        .padding(.horizontal,1.6)
                        .zIndex(1)
                    
                    RoundedRectangle(cornerRadius: 9)
                        .stroke(Color(hex: "#F1F1F1"),lineWidth: 4)
                        .frame(maxWidth:.infinity, maxHeight: 47 )
                        .zIndex(0)
                
                    
                    HStack(alignment:.center,spacing: 8){
                        
                        Text(buttonTitle)
                            .padding(.vertical,16)
                            .font(.custom("Gilroy-Bold", size: 12))
                            .foregroundStyle(Color("textvalue"))
                        
                        Image(imageName)
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                    }
                    
                }
                
            })
        }
    }
}

#Preview {
    
    EnrollCustomButton(
        buttonTitle: "Enroll for this course",
        buttonTapped: {
            
        },
        buttonHorizontalPadding: 16,
        buttonVerticalPadding: 14,
        imageName: "union", 
        isLengthCardButton: false
    )
    
//   GetDetailsButton(
//    buttonTitle: "Get Details",
//    imageName: "union",
//    getDetailsButtonTapped: {
//        
//    }
//   )
}
