//
//  SMEDataView.swift
//  iosApp
//
//  Created by Tusmit Shah on 11/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SMEDataView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var showAllPolicyRates : [PolicyRateData] = []

    @State private var loader = false
    
    var body: some View {
        VStack(alignment:.leading,spacing:0){
            VStack(spacing:0){
                    HStack(spacing:0){
                        
                        Image("back")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                withAnimation{
                                    router.navigateBack()
                                }
                            }
                        
                        Spacer()
                        
                        Text("Vehicle Data")
                            .font(.custom("Poppins-SemiBold", size: 24))
                            .foregroundStyle(Color.black)
                        
                        Spacer()
                        
                        Rectangle()
                            .frame(width: 20,height: 0)
                        
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,16)
            }
            .background(
                Color(hex: "#E3FFF6")
                    .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            
            VStack(spacing:16){
                if loader {
                    
                    VStack{
                        Spacer()
                        
                        ProgressView()
                            .progressViewStyle(.circular)
                        
                        Spacer()
                    }
                    
                }
                else {
                    
                    if !showAllPolicyRates.isEmpty {
                        ScrollView(.vertical,showsIndicators: false){
                            
                            ForEach(showAllPolicyRates, id: \.self){policyRate in
                                
                                HStack(spacing:16){
                                    
                                    VStack(alignment:.leading,spacing:4){
                                        
                                        Text(policyRate.insurance_type.name)
                                            .font(.custom("Poppins-Medium", size: 12))
                                            .foregroundStyle(Color("subtitle", bundle: nil))
                                            .lineLimit(1)
                                        
                                        Text(policyRate.insurer.name)
                                            .font(.custom("Poppins-SemiBold", size: 16))
                                            .foregroundStyle(Color("title", bundle: nil))
                                            .lineLimit(1)
                                        
                                    }
                                    .frame(maxWidth:.infinity,alignment:.leading)
                                    
                                    
                                    
                                    HStack(spacing:2){
                                        Text("\(policyRate.payouts)")
                                            .font(.custom("Gilroy-Bold", size: 32))
                                            .foregroundStyle(Color("title", bundle: nil))
                                        
                                        Text("%")
                                            .font(.custom("Gilroy-Bold", size: 32))
                                            .foregroundStyle(Color("title", bundle: nil))
                                    }
                                    .frame(maxWidth:.infinity,alignment:.trailing)
                                    
                                }
                                .frame(maxWidth:.infinity,alignment:.leading)
                                .padding(.vertical,20)
                                .padding(.horizontal,20)
                                .background(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .cornerRadius(12, corners: [.allCorners])
                                .padding(.horizontal,16)
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    print("Policy Rate Selected")
                                    withAnimation{
                                        let id = policyRate.id
                                        DispatchQueue.main.async {
                                            accessModel.policyRateId = id
                                        }
                                        router.navigateTo(to: .policyratedetailview)
                                    }
                                }
                            }
                            .onAppear{
                                withAnimation{
                                    self.loader = false
                                }
                            }
                        }
                        .refreshable(action: {
                            let token = retrieveToken() ?? ""
                            let payload = SearchPolicyRatePayload(
                                state_id: "",
                                city_id: "",
                                city_category_id: "",
                                vehicle_type_id: "",
                                vehicle_model_id: "",
                                renewal_type_id: "",
                                insurance_type_id: "",
                                insurer_id: "",
                                fuel_type_id: "",
                                status: "", page: 1, size: 50
                            )
                            
                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                searchPolicyRates(token: token, payload: payload)
                            }
                        })
                    }
                    else {
                        VStack{
                            
                            Spacer()
                            
                            Image("nodata")
                            
                            Text("No data found")
                                .font(.custom("Gilroy-SemiBold", size: 28))
                                .padding(.top,20)
                                .onAppear{
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 1.5){
                                        withAnimation{
                                            self.loader = false
                                        }
                                    }
                                }
                            
                            Spacer()
                        }
                    }
                }
            }
            .padding(.top,16)
            
            
        }
        .frame(maxWidth:.infinity,maxHeight: .infinity,alignment: .top)
        .background(
            Color(hex: "#F5F8FF")
        )
        .navigationBarBackButtonHidden()
        .onReceive(accessModel.$policyRatesData, perform: { policy in
            if !policy.isEmpty {
                self.showAllPolicyRates = policy
            }
            else {
                self.showAllPolicyRates = []
            }
            
        })
        .onReceive(accessModel.$generalPolicyRatesData, perform: {value in
            if !(value?.items.isEmpty ?? false) {
                print("Health Policy Rate -> \(value?.items)")
            }
        })
    }
    
    
    func searchPolicyRates(token : String, payload : SearchPolicyRatePayload){
        Task.init{
            do
            {
                let (result,response) = try await accessModel.searchPolicyRates(token: token, searchPayload: payload)
                
                if result {
                    self.loader = false
                    self.showAllPolicyRates = response
                }
                else {
                    self.loader = false
                    self.showAllPolicyRates = response
                }
            }
            catch ApiError.networkFailure {
                // Handle network failure, e.g., show error Snackbar
                self.loader = false
                snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
            } catch ApiError.lowInternetConnection {
                // Handle low internet connection, e.g., show error Snackbar
                self.loader = false
                snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
            } catch ApiError.serverError(let status) {
                // Handle server errors, e.g., show error Snackbar
                self.loader = false
                snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
            } catch ApiError.unknownError(let description){
                // Handle unknown errors
                self.loader = false
                print("Data Fetching Failed -> \(description)")
                snackBar.show(message: description, title: "Error", type: .error)
            }
            
        }
    }
    
    
}

#Preview {
    SMEDataView(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState(),
        snackBar: SnackbarModel()
    )
}
