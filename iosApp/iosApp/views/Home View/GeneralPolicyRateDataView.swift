//
//  GeneralPolicyRateDataView.swift
//  iosApp
//
//  Created by Tusmit Shah on 14/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct GeneralPolicyRateDataView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var router : Router
    
    @State private var policyRateId : String?
    @State private var policyRateData : GeneralPolicyRateItem?
    
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
                                    Text("Points".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    let payoutsValue = Double(policyRateData?.payouts ?? "0.0") ?? 0.0 // Convert String to Double with a fallback to 0.0
                                    let points = String(format: "%.2f", payoutsValue * 0.1)
                                    
                                    if let _ = policyRateData?.payouts {
                                        Text(points)
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
                                    Text("Slab".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let slabName = policyRateData?.slab.name {
                                        Text(slabName)
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
                                    Text("Product".uppercased())
                                        .font(.custom("Poppins-Medium", size: 16))
                                        .foregroundStyle(Color("title", bundle: nil))
                                    
                                    Spacer()
                                    
                                    if let productName = policyRateData?.product.name {
                                        Text(productName)
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
            self.policyRateId = accessModel.generalPolicyRateId
            print("General Policy Rate Id -> \(String(describing: policyRateId))")
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                let policyRateData = accessModel.generalPolicyRatesData?.items
                
                if !(policyRateData?.isEmpty ?? false) {
                    let data = policyRateData?.filter{ rate in
                        rate.id == policyRateId
                    }
                    self.loader = false
                    self.policyRateData = data?.first
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
//    GeneralPolicyRateDataView()
//}
