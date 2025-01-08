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
    @ObservedObject var router : Router
    
    @State private var policyRateId : String?
    @State private var policyRateData : PolicyRateData?
    let policyRateDetailViewClosed : () -> Void
    
    @State private var loader : Bool = false
    
    
    var body: some View {
        VStack(spacing:0){
            VStack(spacing:0){
                    HStack(spacing:0){
                        
                        Image("back")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                withAnimation{
                                    policyRateDetailViewClosed()
                                }
                            }
                        
                        Spacer()
                        
                        Text("Policy Rates")
                            .font(.custom("Poppins-SemiBold", size: 24))
                            .foregroundStyle(Color.black)
                        
                        Spacer()
                        
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,16)
            }
            .background(
                Color(hex: "#E3FFF6")
                    .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            
            VStack(spacing:0){
                if loader {
                    VStack{
                        Spacer()
                        
                        ProgressView()
                            .progressViewStyle(.circular)
                        
                        Spacer()
                    }
                }
                else {
                    if ((policyRateData?.isEqual(nil)) != nil) {
                        ScrollView(.vertical,showsIndicators: false){
                            VStack(alignment: .leading,spacing: 16){
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("PAYOUT %".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let payout = policyRateData?.payouts {
                                        Text(payout)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("Insurer".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let insurerName = policyRateData?.insurer.name {
                                        Text(insurerName)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("Insurance Type".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let insuranceType = policyRateData?.insurance_type.name {
                                        Text( insuranceType)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("Vehicle Type".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let vehicleModel = policyRateData?.vehicle_model.name {
                                        Text(vehicleModel)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("Renewal Type".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let renewalType = policyRateData?.renewal_type.name {
                                        Text(renewalType)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("Fuel Type".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let fuelType = policyRateData?.fuel_type.name{
                                        Text(fuelType)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("State".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let state = policyRateData?.city.state.name {
                                        Text(state)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("City Category".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let cityCategory = policyRateData?.city_category?.name {
                                        Text(cityCategory)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("City".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let cityName = policyRateData?.city.name {
                                        Text(cityName)
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    else {
                                        Text("N / A")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                VStack(alignment:.leading,spacing:0){
                                    Text("NCB".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if policyRateData?.status == 1 {
                                        Text("Yes")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color.green)
                                    }
                                    else {
                                        Text("No")
                                            .font(.custom("Poppins-SemiBold", size: 20))
                                            .foregroundStyle(Color.red)
                                    }
                                }
                                .padding(.horizontal,16)
                                .padding(.vertical,16)
                                .frame(maxWidth:.infinity,alignment: .leading)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                
                                
                            }
                        }
                    }
                    else {
                        VStack{
                            Spacer()
                            
                            Image("nodata")
                            
                            Text("No data found")
                                .font(.custom("Gilroy-SemiBold", size: 28))
                                .padding(.top,20)
                            
                            Spacer()
                        }
                    }
                }
            }
            .padding(.vertical,16)
            
        }
        .background(
            Color(hex: "#F5F8FF")
        )
        .navigationBarBackButtonHidden()
        .onAppear{
            self.loader = true
            self.policyRateId = accessModel.policyRateId
            print("Policy Rate Id -> \(String(describing: policyRateId))")
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                let policyRateData = accessModel.searchPolicyRatesData

                if !policyRateData.isEmpty {
                    let data = policyRateData.filter{ rate in
                        rate.id == policyRateId
                    }
                    self.loader = false
                    self.policyRateData = data.first
                }
                else {
                    self.loader = false
                    self.policyRateData = nil
                }
            })
        }
    }
}

//#Preview {
//    PolicyRateDetailView(accessModel: AccessServiceViewModel(), snackBar: SnackbarModel(),policyRateId: ""){
//        
//    }
//}

//            Task{
//                do
//                {
//                    let result = try await accessModel.getSinglePolicyRate(token: token, id: policyRateId)
//                    self.policyRateData = result
//
//                }
//                catch ApiError.networkFailure {
//                    // Handle network failure, e.g., show error Snackbar
//                    snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
//                } catch ApiError.lowInternetConnection {
//                    // Handle low internet connection, e.g., show error Snackbar
//                    snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
//                } catch ApiError.serverError(let status) {
//                    // Handle server errors, e.g., show error Snackbar
//                    snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
//                } catch ApiError.unknownError(let description){
//                    // Handle unknown errors
//                    print("Data Fetching Failed -> \(description)")
//                    snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
//                }
//            }
