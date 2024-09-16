//
//  SelectionView.swift
//  iosApp
//
//  Created by Tusmit Shah on 23/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct SelectionView: View {
    
    @Binding var label : String
    
    @Namespace var namespace
    @State private var isContentVisible = false
    @State private var isEmailIdSelected = false
    
    var body: some View {
        VStack{
            
            HStack(alignment:.top){
             
                VStack(alignment:.leading,spacing:8){
                        if isContentVisible {
                            Text("Choose Credential Type")
                                .matchedGeometryEffect(id: "Title", in: namespace)
                                .font(.custom("Gilroy-Medium", size: 15))
                                .foregroundStyle(Color(hex: "#949494"))
                                
                        }
                        else {
                            Text("Choose Credential Type")
                                .matchedGeometryEffect(id: "Title", in: namespace)
                                .font(.custom("Gilroy-Medium", size: 15))
                                .foregroundStyle(Color(hex: "#949494"))
                                
                            if isEmailIdSelected {
                                Text("Email ID")
                                    .matchedGeometryEffect(id: "mail", in: namespace)
                                    .font(.custom("Gilroy-SemiBold", size: 16))
                                    .foregroundStyle(Color("textvalue"))
                                    .padding(.top,5)
                            }
                            else {
                                Text("Phone Number")
                                    .matchedGeometryEffect(id: "phone", in: namespace)
                                    .font(.custom("Gilroy-SemiBold", size: 16))
                                    .foregroundStyle(Color("textvalue"))
                                    .padding(.top,5)
                            }
                            
                        }
                       
                }

                
                Spacer()
                
                    if isContentVisible {
                        Image("union-2")
                            .matchedGeometryEffect(id: "image", in: namespace)
                            .padding(.top,5)
                    }
                    else {
                        Image("union-1")
                            .matchedGeometryEffect(id: "image", in: namespace)
                            .padding(.top,5)
                    }
                
            }
            .padding(.top,5)
            
            if isContentVisible{
                HStack{
                    Text("Email ID")
                        .matchedGeometryEffect(id: "mail", in: namespace)
                        .font(.custom("Gilroy-SemiBold", size: 16))
                        .foregroundStyle(Color("textvalue"))
                    
                    Spacer()
                    
                    Circle()
                        .fill(Color(hex: "#3960F6"))
                        .frame(width: 9, height: 9)
                        .overlay(content: {
                            Circle()
                                .stroke(Color(hex: "#3960F6"),lineWidth: 1)
                                .frame(width: 16,height: 16)
                        })
                        .opacity(isEmailIdSelected ? 1 : 0)
                }
                .padding(.vertical,14)
                .contentShape(Rectangle())
                .onTapGesture {
                    withAnimation{
                        isContentVisible = false
                        isEmailIdSelected = true
                        label = "Enter Email ID"
                    }
                }
                
                
                HStack{
                    Text("Phone Number")
                        .matchedGeometryEffect(id: "phone", in: namespace)
                        .font(.custom("Gilroy-SemiBold", size: 16))
                        .foregroundStyle(Color("textvalue"))
                    
                    Spacer()
                    
                    Circle()
                        .fill(Color(hex: "#3960F6"))
                        .frame(width: 9, height: 9)
                        .overlay(content: {
                            Circle()
                                .stroke(Color(hex: "#3960F6"),lineWidth: 1)
                                .frame(width: 16,height: 16)
                        })
                        .opacity(isEmailIdSelected ? 0 : 1)
                }
                .padding(.vertical,14)
                .contentShape(Rectangle())
                .onTapGesture {
                    withAnimation{
                        isContentVisible = false
                        isEmailIdSelected = false
                        label = "Enter Phone Number"
                    }
                }

            }
            
        }
        .padding(.horizontal,16)
        .padding(.vertical,14)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .stroke(Color(hex: "#949494"),lineWidth: 1.2)
                .shadow(color: Color(hex: "#000004"), radius: 0.3)
        )
        .contentShape(Rectangle())
        .onTapGesture {
            withAnimation{
                isContentVisible.toggle()
            }
        }
        .padding(.horizontal,16)
    }
}

#Preview {
    SelectionView(label: .constant("Enter Phone Number"))
}


//Text("Email ID")
//    .font(.custom("Gilroy-SemiBold", size: 16))
//    .foregroundStyle(Color("textvalue"))
//
//Text("Mobile Number")
//    .font(.custom("Gilroy-SemiBold", size: 16))
//    .foregroundStyle(Color("textvalue"))


