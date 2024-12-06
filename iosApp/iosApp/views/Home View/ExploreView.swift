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
        
        ZStack(alignment: .center){
            
                VStack(spacing:0){
                    VStack(spacing:0){
                        
                        HStack(spacing:0){
                            Text("Sales Tools")
                                .matchedGeometryEffect(id: "header", in: nameSpace)
                                .font(.custom("Gilroy-Bold", size: 28))
                                .foregroundStyle(Color("bgColor1", bundle: nil))
                            
                            Spacer()
                            
                            Image("1Filter3x")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 22, height: 22)
                                .padding(.vertical,8)
                                .padding(.horizontal,12)
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    withAnimation{
                                        isFilterSelected = true
                                    }
                                }
                        }
                        .padding(.horizontal,16)
                        .padding(.vertical,12)
                    }
                    .background(
                        LinearGradient(
                            gradient: Gradient(colors: [Color("bgColor2", bundle: nil), Color("bgColor2", bundle: nil), Color("bgColor1", bundle: nil)]),
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                        .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
                    )
                    
                    VStack(spacing:8){
                        ScrollView(.vertical,showsIndicators: false){
                                
                                if loader {
                                    ProgressView()
                                        .padding(.top,20)
                                }
                                else {
                                    if !showAllPolicyRates.isEmpty {
                                        ForEach(showAllPolicyRates, id: \.self){policyRate in
                                            
                                            HStack(spacing:16){
                                                
                                                VStack(alignment:.leading,spacing:4){
                                                    Text(policyRate.insurer.name)
                                                        .font(.custom("Gilroy-Bold", size: 18))
                                                        .foregroundStyle(Color("title", bundle: nil))
                                                        .lineLimit(1)
                                                    
                                                    
                                                    
                                                    Text(policyRate.insurance_type.name)
                                                        .font(.custom("Gilroy-Medium", size: 14))
                                                        .foregroundStyle(Color("subtitle", bundle: nil))
                                                        .lineLimit(1)
                                                    
                                                }
                                                .frame(maxWidth:.infinity,alignment:.leading)
                                                
                                                Spacer()
                                                    .frame(width:24)
                                                
                                                
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
                                            .background(Color("cardbackground", bundle: nil))
                                            .contentShape(Rectangle())
                                            .onTapGesture {
                                                print("Policy Rate Selected")
                                                withAnimation{
                                                    isPolicyRateSelected = true
                                                    self.policyRateId = policyRate.id
                                                }
                                            }
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
                                status: "", page: 1, size: 20
                            )
                            
                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                searchPolicyRates(token: token, payload: payload)
                            }
                        })
                    }
                
            }
            .zIndex(0)
            .blur(radius: isPolicyRateSelected ? 4 : 0)
           
            
            let data = showAllPolicyRates.filter{ policy in
                policy.id == policyRateId
            }
            
            if isPolicyRateSelected {
                if let policyRateData = data.first {
                    PolicyRateDetailView(accessModel: accessModel,snackBar: snackBar, policyRateId: policyRateId, policyRateData: policyRateData){
                        withAnimation{
                            isPolicyRateSelected = false
                        }
                    }
                    .zIndex(1)
                }
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
                status: "", page: 1, size: 20
            )
            searchPolicyRates(token: token, payload: payload)
            
                    
            DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: {
                allCities(token: token)
                insuranceTypes(token : token)
                renewalTypes(token : token)
                insurerTypes(token : token)
                vehicleTypes(token: token)
                fuelTypes(token: token)
                allStates(token: token)
                cityCategories(token: token)
            })
            
        }
        .onReceive(appDelegate.$fcmToken, perform: {token in
            print("FCM Token After Authorization -> \(token)")
            let isRegistered = retrieveDeviceRegistration() ?? false
            if !(isRegistered) {
                registerDeviceWithKMM(fcmToken: token, accessModel: accessModel)
            }
        })
        .onReceive(accessModel.$policyRatesData, perform: { policy in
            if !policy.isEmpty {
                self.showAllPolicyRates = policy
            }
            else {
                self.showAllPolicyRates = []
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
                let _ =  try await accessModel.registerDeviceForNotification(userId: id, token: token, projectId: projectId, deviceToken: fcmToken)
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



//HStack(spacing:0){
//    HStack(spacing:10){
//        Circle()
//            .foregroundStyle(Color(hex: "#F8F8F8"))
//            .frame(width: 42,height: 42)
//            .overlay(content: {
//                Text(nameInitials)
//                    .font(.custom("Gilroy-SemiBold", size: 13))
//            })
//        
//        VStack(alignment:.leading,spacing:6){
//            Text("Welcome ")
//                .font(.custom("Gilroy-SemiBold", size: 12))
//            
//            Text("\(userName)")
//                .font(.custom("Gilroy-Bold", size: 15))
//            
//        }
//    }
//    
//    Spacer()
//    
//    NotificationBellView(){
//        withAnimation{
//            router.navigateTo(to: .notificationscreen)
//        }
//    }
//    
//}
//.padding(.bottom,23)
//.padding(.horizontal,16)
//.padding(.top,12)

//
//HStack(alignment:.top,spacing:12){
//    Circle()
//        .foregroundStyle(Color(hex: "#D9D9D9"))
//        .frame(width: 35,height: 35)
//        .overlay(content: {
//            Image(systemName: "doc")
//                .resizable()
//                .aspectRatio(contentMode: .fit)
//                .frame(width: 12, height: 12)
//            
//        })
//    
//    HStack(alignment:.top,spacing:16){
//        VStack(alignment:.leading,spacing:4){
//            Text("PAYOUT %")
//                .font(.custom("Gilroy-Medium", size: 12))
//            
//            Text(policyRate.payouts)
//                .font(.custom("Gilroy-Bold", size: 14))
//        }
//        
//        
//        VStack(alignment:.leading,spacing:4){
//            Text("INSURER")
//                .font(.custom("Gilroy-Medium", size: 12))
//            
//            Text(policyRate.insurer.name)
//                .font(.custom("Gilroy-Bold", size: 14))
//        }
//        
//        VStack(alignment:.leading,spacing:4){
//            Text("INSURANCE TYPE")
//                .font(.custom("Gilroy-Medium", size: 12))
//            
//            Text(policyRate.insurance_type.name)
//                .font(.custom("Gilroy-Bold", size: 14))
//        }
//        
//        
//    }
//    .offset(y:2)
//    
//    Spacer()
//    
//    Image(systemName: "arrowshape.right.fill")
//}
