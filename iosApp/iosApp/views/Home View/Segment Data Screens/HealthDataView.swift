//
//  HealthDataView.swift
//  iosApp
//
//  Created by Tusmit Shah on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HealthDataView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var showAllPolicyRates : [GeneralPolicyRateItem] = []

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
                    
                    if !(showAllPolicyRates.isEmpty) {
                        ScrollView(.vertical,showsIndicators: false){
                            
                            ForEach(showAllPolicyRates, id: \.self){policyRate in
                                
                                HStack(spacing:16){
                                    
                                    Rectangle()
                                        .frame(width: 54,height: 54,alignment: .center)
                                        .foregroundStyle(Color(hex: "#ffffff"))
                                        .overlay(alignment:.leading,content: {
                                            let imageURL = URL(string: policyRate.insurer.media_url)
                                            AsyncImage(url: imageURL) { phase in
                                                if let image = phase.image {
                                                    image
                                                        .resizable()
                                                        .frame(width: 54,height: 54)
                                                        .padding(2)
                                                        
                                                }
                                                else if phase.error != nil {
                                                    Image("dummy-image1")
                                                        .resizable()
                                                        .aspectRatio(contentMode: .fit)
                                                        .frame(width: 54, height: 54, alignment: .center)
                                                    
                                                }
                                                else {
                                                    Image("dummy-image1")
                                                        .resizable()
                                                        .aspectRatio(contentMode: .fit)
                                                        .frame(width: 54, height: 54, alignment: .center)
                                                }
                                            }
                                        })
                                    
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
                                    
                                    
                                    let payoutsValue = Double(policyRate.payouts) ?? 0.0 // Convert String to Double with a fallback to 0.0
                                    let points = String(format: "%.1f", payoutsValue * 0.1)
                                    
                                    VStack(spacing:2){
                                        Text("\(points)")
                                                .font(.custom("Gilroy-Bold", size: 16))
                                                .foregroundStyle(Color("title", bundle: nil))
                                        
                                        Text("Points")
                                                .font(.custom("Gilroy-Bold", size: 16))
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
                                        accessModel.generalPolicyRateId = id
                                        router.navigateTo(to: .generalpolicyratedetailview)
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
                            let payload = GeneralPolicyRatePayload(
                                insurer_id: "",
                                renewal_type_id: "",
                                insurance_type_id: "",
                                policy_segment_id: "3b554917-a340-4682-9eac-3ffe13ec660f",
                                slab_id: "",
                                product_id: "",
                                ppt_id: "",
                                insurer_group_id:"",
                                payouts: "",
                                payins: "",
                                remarks: "",
                                description: ""
                            )
                            
                            searchGeneralPolicyRates(token: token, payload: payload)
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
        .onReceive(accessModel.$generalPolicyRatesData, perform: {value in
            if !(value?.items?.isEmpty ?? false) {
                self.showAllPolicyRates = value?.items ?? []
            }
            else {
                self.showAllPolicyRates = []
            }
        })
    }
    
    func searchGeneralPolicyRates(token : String, payload : GeneralPolicyRatePayload){
        Task.init{
            do
            {
                let result = try await accessModel.searchGeneralPolicyRates(token: token, searchPayload: payload)
                
                if !(result.items?.isEmpty ?? false) {
                    self.loader = false
                    self.showAllPolicyRates = result.items ?? []
                }
                else {
                    self.loader = false
                    self.showAllPolicyRates = []
                }
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
                snackBar.show(message: description, title: "Error", type: .error)
            }

        }
    }
    
    
}

#Preview {
    HealthDataView(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState(),
        snackBar: SnackbarModel()
    )
}
