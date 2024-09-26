//
//  ExploreView.swift
//  iosApp
//
//  Created by Tusmit Shah on 06/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ExploreView: View {
    
    let userName : String
    let nameInitials : String
    
    @EnvironmentObject var appDelegate: AppDelegate
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    
    @State private var exploreTransition = false
    @Namespace var nameSpace
    @State private var yOffset = 0.0
    let columns = [
           GridItem(.flexible()),
           GridItem(.flexible()),
    ]
    
    @State private var isPolicyRateSelected = false
    @State private var isFilterSelected = false
    
    @State private var showAllPolicyRates : [PolicyRateData] = []
    @State private var policyRateId : String = ""
    
    @State private var loader = false
    
    @State private var granted = false
    
    @State private var fcmToken = ""
    
    var body: some View {
        
        ZStack{
            
            VStack(spacing:0){
                    ZStack{
                        VStack(spacing:0){
                            
                            HStack(spacing:0){
                                HStack(spacing:10){
                                    Circle()
                                        .foregroundStyle(Color(hex: "#F8F8F8"))
                                        .frame(width: 42,height: 42)
                                        .overlay(content: {
                                            Text(nameInitials)
                                                .font(.custom("Gilroy-SemiBold", size: 13))
                                        })
                                    
                                    VStack(alignment:.leading,spacing:6){
                                        Text("Welcome ")
                                            .font(.custom("Gilroy-SemiBold", size: 13))
                                        
                                        Text("\(userName)")
                                            .font(.custom("Gilroy-SemiBold", size: 13))
                                        
                                    }
                                }
                                
                                Spacer()
                                
                                NotificationBellView(){
                                    withAnimation{
                                        router.navigateTo(to: .notificationscreen)
                                    }
                                }
                                
                            }
                            .padding(.bottom,23)
                            .padding(.horizontal,16)
                            .padding(.top,12)
                            
                            HStack(spacing:0){
                                Text("Sales Tools")
                                    .matchedGeometryEffect(id: "header", in: nameSpace)
                                    .font(.custom("Gilroy-Bold", size: 28))
                                
                                Spacer()
                                
                                Text("Apply Filter")
                                    .font(.custom("Gilroy-Bold", size: 12))
                                    .padding(.vertical,8)
                                    .padding(.horizontal,12)
                                    .overlay(content: {
                                        RoundedRectangle(cornerRadius: 5)
                                            .stroke(.black, lineWidth: 1)
                                    })
                                    .contentShape(Rectangle())
                                    .onTapGesture {
                                        withAnimation{
                                            isFilterSelected = true
                                        }
                                    }

                                
                            }
                            .padding(.horizontal,16)
                            .padding(.bottom,12)
                            
                        }
                        .background(Color(hex: "#D9D9D9"))
                    }

                
                VStack(spacing:0){
                    
                    ScrollView{
                        if loader {
                            ProgressView()
                                .padding(.top,20)
                        }
                        else {
                            if !showAllPolicyRates.isEmpty {
                                ForEach(showAllPolicyRates, id: \.self){policyRate in
                                    
                                    HStack(alignment:.top,spacing:12){
                                        Circle()
                                            .foregroundStyle(Color(hex: "#D9D9D9"))
                                            .frame(width: 35,height: 35)
                                            .overlay(content: {
                                                Image(systemName: "doc")
                                                    .resizable()
                                                    .aspectRatio(contentMode: .fit)
                                                    .frame(width: 12, height: 12)
                                                
                                            })
                                        
                                        HStack(alignment:.top,spacing:16){
                                            VStack(alignment:.leading,spacing:4){
                                                Text("PAYOUT %")
                                                    .font(.custom("Gilroy-Medium", size: 12))
                                                
                                                Text(policyRate.payouts)
                                                    .font(.custom("Gilroy-Bold", size: 14))
                                            }
                                            
                                            
                                            VStack(alignment:.leading,spacing:4){
                                                Text("INSURER")
                                                    .font(.custom("Gilroy-Medium", size: 12))
                                                
                                                Text(policyRate.insurer.name)
                                                    .font(.custom("Gilroy-Bold", size: 14))
                                            }
                                            
                                            VStack(alignment:.leading,spacing:4){
                                                Text("INSURANCE TYPE")
                                                    .font(.custom("Gilroy-Medium", size: 12))
                                                
                                                Text(policyRate.insurance_type.name)
                                                    .font(.custom("Gilroy-Bold", size: 14))
                                            }
                                            
                                            
                                        }
                                        .offset(y:2)
                                        
                                        Spacer()
                                        
                                        Image(systemName: "arrowshape.right.fill")
                                    }
                                    .padding(.vertical,12)
                                    .padding(.horizontal,16)
                                    .contentShape(Rectangle())
                                    .onTapGesture {
                                        print("Policy Rate Selected")
                                        withAnimation{
                                            isPolicyRateSelected = true
                                            self.policyRateId = policyRate.id
                                        }
                                    }
                                    
                                    
                                    Divider()
                                }
                                .onAppear{
                                    withAnimation{
                                        self.loader = false
                                    }
                                }
                               
                            }
                            else {
                                Text("No Data Found.")
                                    .font(.custom("Gilroy-SemiBold", size: 28))
                                    .padding(.top,20)
                                    .onAppear{
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5){
                                            withAnimation{
                                                self.loader = false
                                            }
                                        }
                                    }
                            }
                        }
                    }
                    .refreshable(action: {
                        let token = retrieveToken() ?? ""
                        getPolicyRatesList(token: token)
                    }) 
                }
                
            }
            .zIndex(0)
            
            if isPolicyRateSelected {
                PolicyRateDetailView(accessModel: accessModel,snackBar: snackBar, policyRateId: policyRateId){
                    withAnimation{
                        isPolicyRateSelected = false
                    }
                }
                .zIndex(1)
            }
            
        }
        .sheet(isPresented: $isFilterSelected, content: {
            ApplyFiltersView(accessModel: accessModel, snackBar: snackBar){
                withAnimation{
                    isFilterSelected = false
                }
            }
        })
        .background(Color(hex: "#F8F8F8"))
        .onAppear{
            
            let token = retrieveToken() ?? ""
            self.loader = true
                    
            
            allCities(token: token)
            insuranceTypes(token : token)
            renewalTypes(token : token)
            insurerTypes(token : token)
            vehicleTypes(token: token)
            fuelTypes(token: token)
            allStates(token: token)
            cityCategories(token: token)
            
        }
        .onReceive(accessModel.$policyRatesData){data in
            if !data.isEmpty {
                self.loader = false
                showAllPolicyRates = data
            }
        }
        .onReceive(appDelegate.$fcmToken, perform: {token in
            print("FCM Token After Authorization -> \(token)")
            if !token.isEmpty{
                registerDeviceWithKMM(fcmToken: token, accessModel: accessModel)
            }
        })
    

    }
    
    // Register Device for Push Notification
    func registerDeviceWithKMM(fcmToken: String, accessModel : AccessServiceViewModel) {
    
        let token = retrieveToken() ?? ""
        let id = retrieveUserId() ?? ""
        let projectId = "0d98736c-5f90-41b4-b689-1b1935aab762"
        
        Task{
            do
            {
                try await accessModel.registerDeviceForNotification(userId: id, token: token, projectId: projectId, deviceToken: fcmToken)
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
        
    // Get Policy Rates
    func getPolicyRatesList(token : String){
        Task.init{
            self.loader = true
            do
            {
                let response = try await accessModel.getPolicyRates(token: token)
                if !response.isEmpty {
                    self.loader = false
                    self.showAllPolicyRates = response
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
                snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
            }
        }
    }
 
    func vehicleTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getVehicleTypes(token: token)
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
    
    func fuelTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getFuelTypes(token: token)
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
    
    func allStates(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllStates(token: token)
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
    
    func cityCategories(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllCityCategories(token: token)
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
 
    
    func insuranceTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllInsuranceTypes(token: token)
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
    
    func renewalTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllRenewalTypes(token: token)
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
    
    func insurerTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllInsurerTypes(token: token)
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
    
    func allCities(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllCitiesData(token: token)
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

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}

struct NotificationBellView : View {
    
    let onTapOfBell : () -> Void
    
    var body: some View {
        ZStack{
            Image("bell-02")
                .zIndex(0)
            
            Circle()
                .fill(Color(hex: "#F63939"))
                .frame(width: 6,height: 6)
                .offset(x: 6,y: -11)
        }
        .padding(.vertical,12)
        .padding(.horizontal,5)
        .contentShape(Circle())
        .onTapGesture {
            onTapOfBell()
        }
    }
}

#Preview {
    ExploreView(
        userName:"userName",
        nameInitials: "nameInitials",
        accessModel: AccessServiceViewModel(),
        snackBar: SnackbarModel(),
        router: Router(),
        navigationState: NavigationState()
    )
}


