//
//  ApplyFiltersView.swift
//  iosApp
//
//  Created by Tusmit Shah on 13/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ApplyFiltersView: View {
    
    let applyFiltersViewClosed : () -> Void
    
    @State private var dropDownViewSelected : [String : Bool] = [
       "Vehicle Type" : false,
       "Fuel Type" : false,
       "NCB" : false,
       "State" : false,
       "City Category" : false,
       "City" : false,
       "Insurance Type" : false,
       "Renewal Type" : false,
       "Insurer" : false
    ]
    @State private var selectedValue : [String : String] = [
        "Vehicle Type" : "",
        "Fuel Type" : "",
        "NCB" : "",
        "State" : "",
        "City Category" : "",
        "City" : "",
        "Insurance Type" : "",
        "Renewal Type" : "",
        "Insurer" : ""
    ]
    
    let vehicleTypes = ["HE", "Car", "Truck"]
    
    var body: some View {
        VStack(alignment:.leading,spacing:0){
            HStack(spacing:0){
                Text("Filters")
                    .font(.custom("Gilroy-Bold", size: 32))
                
                Spacer()
                
                Image("close-button")
                    .contentShape(Circle())
                    .onTapGesture {
                        applyFiltersViewClosed()
                    }
            }
            .padding(.horizontal,16)
            .padding(.bottom,12)
            
            ScrollView{
                VStack(alignment:.leading,spacing:0){
                    Text("Vehicle Details")
                        .font(.custom("Gilroy-Bold", size: 22))
                        .padding(.bottom,16)
                        .padding(.top,20)
                    
                    VStack(spacing:16){
                        selectionView(selectionTitle: "Vehicle Type", staticValue: "Select Vehicle Type")
                        
                        selectionView(selectionTitle: "Fuel Type", staticValue: "Select Fuel Type")
                        
                        selectionView(selectionTitle: "NCB", staticValue: "Select Status")
                    }
                    
                    Text("Location Details")
                        .font(.custom("Gilroy-Bold", size: 22))
                        .padding(.bottom,16)
                        .padding(.top,20)
                    
                    VStack(spacing:16){
                        selectionView(selectionTitle: "State", staticValue: "Select State")
                        
                        selectionView(selectionTitle: "City Category", staticValue: "Select City Category")
                        
                        selectionView(selectionTitle: "City", staticValue: "Select City")
                    }
                    
                    
                    Text("Policy Details")
                        .font(.custom("Gilroy-Bold", size: 22))
                        .padding(.bottom,16)
                        .padding(.top,20)
                    
                    VStack(spacing:16){
                        selectionView(selectionTitle: "Insurance Type", staticValue: "Select Insurance Type")
                        
                        selectionView(selectionTitle: "Renewal Type", staticValue: "Select Renewal Type")
                        
                        selectionView(selectionTitle: "Insurer", staticValue: "Select Insurer")
                    }
                    
                    
                    LMSCustomButton(
                        isButtonActive: true,
                        buttonTitle: "Apply Filters"
                    ){
                        
                    }
                    .padding(.vertical,16)
                    
                    
                }
                .padding(.horizontal,16)
            }
        }
    }
    
    
    @ViewBuilder
    func selectionView(selectionTitle : String, staticValue : String) -> some View {
        VStack(alignment:.leading,spacing:8){
            Text(selectionTitle)
                .font(.custom("Gilroy-SemiBold", size: 16))
                .padding(.leading,5)
            
            VStack(alignment:.leading,spacing:0){
                HStack(spacing:0){
                    
                    if selectedValue[selectionTitle] == "" {
                        Text(staticValue)
                            .font(.custom("Gilroy-Medium", size: 14))
                    }
                    else {
                        if let value = selectedValue[selectionTitle]  {
                            Text(value)
                                .font(.custom("Gilroy-Medium", size: 14))
                        }
                       
                    }
                    
                    Spacer()
                    
                    Image("union-1")
                        .padding(.top,5)
                }
                
                
                if dropDownViewSelected[selectionTitle] ?? false {
                    VStack(alignment:.leading,spacing:16){
                        HStack{
                            Text(staticValue)
                                .font(.custom("Gilroy-Medium", size: 14))
                           
                            Spacer()
                            
                            if selectedValue[selectionTitle] == "" {
                                Circle()
                                    .fill(Color(hex: "#3960F6"))
                                    .frame(width: 9, height: 9)
                                    .overlay(content: {
                                        Circle()
                                            .stroke(Color(hex: "#3960F6"),lineWidth: 1)
                                            .frame(width: 16,height: 16)
                                    })
                                    
                            }
                        }
                        .padding(.top,8)
                        .contentShape(Rectangle())
                        .onTapGesture {
                            withAnimation{
                                selectedValue[selectionTitle] = ""
                                dropDownViewSelected[selectionTitle] = false
                                print("\(String(describing: selectedValue[selectionTitle]))")
                            }
                        }
                        
                        ForEach(vehicleTypes,id: \.self){i in
                            HStack{
                                Text(i)
                                    .font(.custom("Gilroy-Medium", size: 14))
                                
                                Spacer()
                                
                                
                                if selectedValue[selectionTitle] == i {
                                    Circle()
                                        .fill(Color(hex: "#3960F6"))
                                        .frame(width: 9, height: 9)
                                        .overlay(content: {
                                            Circle()
                                                .stroke(Color(hex: "#3960F6"),lineWidth: 1)
                                                .frame(width: 16,height: 16)
                                        })
                                }
                            }
                            .contentShape(Rectangle())
                            .onTapGesture {
                                withAnimation{
                                    selectedValue[selectionTitle] = i
                                    dropDownViewSelected[selectionTitle] = false
                                    print("\(String(describing: selectedValue[selectionTitle]))")
                                }
                            }
                        }
                        
                    }
                    .padding(.top,8)
                    
                }
                
            }
            .padding(.horizontal,16)
            .padding(.vertical,16)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color(hex: "#949494"),lineWidth: 1.2)
                    .shadow(color: Color(hex: "#000004"), radius: 0.3)
            )
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation{
                    dropDownViewSelected[selectionTitle]?.toggle()
                }
            }
        }
    }
}

#Preview {
    ApplyFiltersView(){
        
    }
}
