//
//  PolicyRateDetailView.swift
//  iosApp
//
//  Created by Tusmit Shah on 13/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PolicyRateDetailView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    let policyRateId : String
    
    let policyRateDetailViewClosed : () -> Void
    let columns = [
           GridItem(.flexible()),
           GridItem(.flexible()),
    ]
    @State private var policyRateData : PolicyRateData?
    
    var body: some View {
        VStack(spacing:0){
            HStack(spacing:0){
                Text("Policy Rates")
                    .font(.custom("Gilroy-Bold", size: 32))
                
                Spacer()
                
                Image("close-button")
                    .contentShape(Circle())
                    .onTapGesture {
                        policyRateDetailViewClosed()
                    }
            }
            .padding(.horizontal,16)
            
            LazyVGrid(columns: columns,alignment: .leading,spacing: 16){
                VStack(alignment:.leading,spacing:8){
                    Text("PAYOUT %".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    if let payout = policyRateData?.payouts {
                        Text(payout)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Insurer".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    if let insurerName = policyRateData?.insurer.name {
                        Text(insurerName)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                   
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Insurance Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    if let insuranceType = policyRateData?.insurance_type.name {
                        Text( insuranceType)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Vehicle Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    if let vehicleModel = policyRateData?.vehicle_model.name {
                        Text(vehicleModel)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                  
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Renewal Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    if let renewalType = policyRateData?.renewal_type.name {
                        Text(renewalType)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                  
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Fuel Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    if let fuelType = policyRateData?.fuel_type.name{
                        Text(fuelType)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                 
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("State".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    if let state = policyRateData?.city.state.name {
                        Text(state)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                   
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("City Category".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    if let cityCategory = policyRateData?.city.city_category.name {
                        Text(cityCategory)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                   
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("City".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    if let cityName = policyRateData?.city.name {
                        Text(cityName)
                            .font(.custom("Gilroy-Bold", size: 16))
                    }
                    else {
                        ProgressView()
                    }
                   
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("NCB".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    if policyRateData?.status == 1 {
                        Text("Yes")
                            .font(.custom("Gilroy-Bold", size: 16))
                            .foregroundStyle(Color.green)
                    }
                    else {
                        Text("No")
                            .font(.custom("Gilroy-Bold", size: 16))
                            .foregroundStyle(Color.red)
                    }
                    
                      
                }

            }
            .padding(.horizontal,16)
            .padding(.top,16)
            
        }
        .padding(.vertical,12)
        .overlay(content: {
            RoundedRectangle(cornerRadius: 5)
                .stroke(.black, lineWidth: 1)
        })
        .padding(.all,8)
        .background(Color.white)
        .onAppear{
            let token = retrieveToken() ?? ""
            print("Policy Rate Id -> \(policyRateId)")
            Task{
                do
                {
                    let result = try await accessModel.getSinglePolicyRate(token: token, id: policyRateId)
                    self.policyRateData = result
                    
                }
                catch ApiError.networkFailure {
                    // Handle network failure, e.g., show error Snackbar
                    snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
                } catch ApiError.lowInternetConnection {
                    // Handle low internet connection, e.g., show error Snackbar
                    snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
                } catch ApiError.serverError(let status) {
                    // Handle server errors, e.g., show error Snackbar
                    snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
                } catch ApiError.unknownError(let description){
                    // Handle unknown errors
                    print("Data Fetching Failed -> \(description)")
                    snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
                }
            }
        }
       
    }
}

#Preview {
    PolicyRateDetailView(accessModel: AccessServiceViewModel(), snackBar: SnackbarModel(),policyRateId: ""){
        
    }
}
