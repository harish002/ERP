//
//  HomeView.swift
//  iosApp
//
//  Created by Tusmit Shah on 12/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

enum SubDestination {
    case categories
}

import SwiftUI
import shared

struct HomeView: View {
    
    @ObservedObject var router : Router
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var navigationState : NavigationState
    

    
    var body: some View {
            ZStack{
                VStack(spacing:0){
                    
                    // In-App Navigation
                    if let (viewName,_) = navigationState.currentView {
                        switch viewName {
                            
                        case "Policy Rates" :
                            ExploreView(
                                accessModel: accessModel,
                                snackBar: snackBar,
                                router: router,
                                navigationState: navigationState
                            )
                            .transition(.trailingToLeading)
                            
                        case "Vehicle Number" :
                            UploadVehicleImageAndNumber()
                            

                            
                        case "Profile" :
                            ProfileView(
                                accessModel: accessModel,
                                router: router, 
                                navigationState: navigationState
                            )
                            .transition(.trailingToLeading)

                          
                            
                        default:
                            Text("Unknown view")
                        }
                    }
                    else {
                        Text("View Not Found")
                    }
                   
                    Spacer()
                    
                    VStack(spacing:0){
                        Rectangle()
                            .fill(Color(hex: "#D0D0D0"))
                            .frame(height: 1)
                        Bottombar(
                            navigationState: navigationState
                        )
                    }
                    
                }
                .zIndex(0)
                .background(Color(hex: "#F8F8F8"))
                
            }
//            .animation(.easeInOut(duration: 0.3), value: selectedBottomTab)
            .navigationBarBackButtonHidden()
            .onAppear{
                print("Current Active View -> \(navigationState.activeViewName) ")
                if navigationState.navigationStack.isEmpty {
                    navigationState.push(viewName: "Policy Rates")
                }
                else {
                    print("NavigationStack is not Empty!")
                }
                
                let token = retrieveToken() ?? ""
                
                vehicleTypes(token: token)
                fuelTypes(token: token)
                allStates(token: token)
                cityCategories(token: token)

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
    

    
    
}


#Preview {
    HomeView(
        router: Router(),
        accessModel: AccessServiceViewModel(),
        snackBar: SnackbarModel(),
        navigationState: NavigationState()
    )
}


//if selectedBottomTab == .explore {
// 
//}
//
//
//if selectedBottomTab == .learn {
//
//}
//
//if selectedBottomTab == .progress {
//
//}
//
//if selectedBottomTab == .profile {
//
//}
//
//if selectedBottomTab == .subTabs {
//    SeeAllView(
//        selectedBottomTab: $selectedBottomTab,
//        accessModel: accessModel,
//        router: router,
//        snackBar: snackBar,
//        navigationState: navigationState
//    )
//}
