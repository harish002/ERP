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
    
    @ObservedObject var appDelepgate = AppDelegate()
    @ObservedObject var router : Router
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var navigationState : NavigationState
    
    @State private var nameInitials = ""
    @State private var userName = ""

    @State private var granted = false
    
    var body: some View {
                VStack(spacing:0){
                    
                    // In-App Navigation
                    if let (viewName,_) = navigationState.currentView {
                        switch viewName {
                            
                        case "Policy Rates" :
                            ExploreView(
                                userName:userName,
                                nameInitials: nameInitials,
                                accessModel: accessModel,
                                snackBar: snackBar,
                                router: router,
                                navigationState: navigationState
                            )
                            .transition(.trailingToLeading)
                            
                        case "Vehicle Number" :
                            UploadVehicleImageAndNumber(accessModel: accessModel, snackBar: snackBar)
                            
                            
                        case "Profile" :
                            ProfileView(
                                accessModel: accessModel,
                                router: router, 
                                navigationState: navigationState
                            )
                            .transition(.trailingToLeading)
                            
                        case "Notification" :
                            NotificationView(
                                accessModel: accessModel,
                                navigationState: navigationState,
                                snackBar: snackBar
                            )
                            

                        default:
                            Text("Unknown view")
                        }
                    }
                    else {
                        Text("View Not Found")
                    }
                   
                    Spacer()
                    
                    VStack(spacing:0){
                        Bottombar(
                            navigationState: navigationState
                        )
                    }
                    
                }
                .zIndex(0)
                .background(Color(hex: "#F8F8F8"))
            
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
                let userId = retrieveUserId() ?? ""
                
                
                Task.init{
                    // Get User Data who is Logged In
                    do
                    {
                        _ = try await accessModel.getUserData(token: token, userId: userId)

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
                
                
                // After login success
                requestNotificationAuthorization()
                
                let name = retrieveName() ?? "Full Name"
                self.userName = name
                
                let initial = retrieveInitials() ?? "?"
                self.nameInitials = initial

            }
            .onChange(of: granted){value in
                if !(value) {
                    snackBar.show(message: "To receive notifications, please enable permissions from your device's settings.", title: "Permission Denied", type: .warning)
                }
            }
        }
    
    
    func extractInitialsAndName(name:String, surname:String){
        
        let nameOfUser = "\(name) \(surname)"
        
        guard let nameInitial = name.first else {
            return
        }
        guard let surnameInitial = surname.first else {
            return
        }
        self.nameInitials = "\(nameInitial)\(surnameInitial)"
        self.userName = nameOfUser
    }
    
    func requestNotificationAuthorization(){
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { success, error in
            if success {
                print("Permission Granted")
                self.granted = success
                DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                }
                
            }
            else {
                print("Permission Denied")
                self.granted = success
            }
        }
        
    }
    
    // Get Policy Rates
    func getPolicyRatesList(token : String){
        Task.init{
            
            do
            {
                let _ = try await accessModel.getPolicyRates(token: token)
              
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
