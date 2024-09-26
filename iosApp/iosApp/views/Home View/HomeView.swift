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
            ZStack{
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
                
                Task.init{
                    // Get User Data who is Logged In
                    do
                    {
                        let data = try await accessModel.getUserData(token: token)

                        let name = data.name.capitalized
                        let surName = data.surname.capitalized

                        extractInitialsAndName(name: name, surname: surName)
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
                
                getPolicyRatesList(token: token)
                
                // After login success
               requestNotificationAuthorization()

            }
            .onChange(of: granted){value in
                if value {
                    snackBar.show(message: "Permission Granted", title: "Success", type: .success)
                }
                else {
                    snackBar.show(message: "Permission Denied! To receive push notifications, please enable permissions from your device's Settings.", title: "Notification Permission", type: .warning)
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
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { success, _ in
            guard success else {
                print("Authorization denied")
                return
            }
            DispatchQueue.main.async {
                UIApplication.shared.registerForRemoteNotifications()
            }
            print("Notifications permission granted")
            self.granted = success
        }
        
    }
    
    // Get Policy Rates
    func getPolicyRatesList(token : String){
        Task.init{
            
            do
            {
                let response = try await accessModel.getPolicyRates(token: token)
              
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
    
    
    // Register Device for Push Notification
    func registerDeviceWithKMM(fcmToken: String, accessModel : AccessServiceViewModel) {
        guard let id = accessModel.userSpecs?.id else {
            print("UserId is empty while registering device..")
            return
        }
        let token = retrieveToken() ?? ""
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
