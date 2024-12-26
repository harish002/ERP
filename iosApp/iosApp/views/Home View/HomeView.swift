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
    
    @State private var isSegmentSheetActive = false
    
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
                            UploadVehicleImageAndNumber(
                                accessModel: accessModel,
                                snackBar: snackBar,
                                router: router
                            )
                            
                            
                        case "Profile" :
                            ProfileView(
                                accessModel: accessModel,
                                router: router, 
                                navigationState: navigationState,
                                snackBar: snackBar
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
                        ){
                            withAnimation{
                                self.isSegmentSheetActive = true
                            }
                        }
                    }
                    
                }
                .zIndex(0)
                .background(Color(hex: "#F8F8F8"))
                .sheet(isPresented:$isSegmentSheetActive , content: {
                    PolicySegmentsView(
                        accessModel: accessModel,
                        snackBar: snackBar,
                        router: router,
                        isSheetClosed: $isSegmentSheetActive
                    )
                    .presentationDetents([.height(500)])
                    
                })
               
                .navigationBarBackButtonHidden()
                .onAppear{
                    print("Current Active View -> \(navigationState.activeViewName) ")
                    if navigationState.navigationStack.isEmpty {
                        navigationState.push(viewName: "Policy Rates")
                    }
                    else {
                        print("NavigationStack is not Empty!")
                    }
                    
                    let isRegistered = retrieveDeviceRegisteredForPushNotification() ?? false
                    requestNotificationAuthorization(isActive: isRegistered)
                    
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
    
    // Requesting Permission for Notifications
    func requestNotificationAuthorization(isActive: Bool) {
        if !isActive {
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { success, error in
                handleNotificationAuthorization(success: success, error: error)
            }
        }
        else {
            UNUserNotificationCenter.current().getNotificationSettings { settings in
                DispatchQueue.main.async {
                    switch settings.authorizationStatus {
                    case .notDetermined:
                        // Re-request permission if not determined
                        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { success, error in
                            handleNotificationAuthorization(success: success, error: error)
                        }
                    case .denied:
                        // Show settings alert for denied permissions
                        showSettingsAlert()
                        
                    case .authorized, .provisional, .ephemeral:
                        // Permission already granted
                        print("Notifications already authorized")
                        
                    @unknown default:
                        break
                    }
                }
            }
        }
    }

    func handleNotificationAuthorization(success: Bool, error: Error?) {
        if success {
            print("Permission Granted")
            DispatchQueue.main.async {
                UIApplication.shared.registerForRemoteNotifications()
            }
            Task {
                await registerDeviceForNotificationsIfNeeded()
            }
        } else {
            print("Permission Denied")
            showSettingsAlert()
            saveDeviceRegisteredForPushNotification(isRegistered: false)
        }
    }

    func registerDeviceForNotificationsIfNeeded() async {
        let token = retrieveToken() ?? ""
        let fcmToken = appDelepgate.fcmToken
        let userId = retrieveUserId() ?? ""

        do {
            let _ = try await accessModel.registerDeviceForNotification(
                token: token,
                projectId: "d0f634d2-0862-491c-accd-662a2e06b106",
                userId: userId,
                deviceToken: fcmToken
            )
        }
        catch {
            print("Error registering device: \(error)")
            snackBar.show(message: "Failed to register device. Please try again later.", title: "Error", type: .error)
        }
    }
    
    func showSettingsAlert() {
        let alert = UIAlertController(
            title: "Notifications Disabled",
            message: "To receive notifications, please enable permissions in the Settings.",
            preferredStyle: .alert
        )
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        alert.addAction(UIAlertAction(title: "Open Settings", style: .default, handler: { _ in
            if let appSettings = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(appSettings, options: [:], completionHandler: nil)
            }
        }))
        
        DispatchQueue.main.async {
            if let rootVC = UIApplication.shared.windows.first?.rootViewController {
                rootVC.present(alert, animated: true, completion: nil)
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

//let token = retrieveToken() ?? ""
//let userId = retrieveUserId() ?? ""
//
//Task.init{
//    // Get User Data who is Logged In
//    do
//    {
//        _ = try await accessModel.getUserData(token: token, userId: userId)
//
//    }
//    catch ApiError.networkFailure {
//        // Handle network failure, e.g., show error Snackbar
//        snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
//    } catch ApiError.lowInternetConnection {
//        // Handle low internet connection, e.g., show error Snackbar
//        snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
//    } catch ApiError.serverError(let status) {
//        // Handle server errors, e.g., show error Snackbar
//        snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
//    } catch ApiError.unknownError(let description){
//        // Handle unknown errors
//        print("Data Fetching Failed -> \(description)")
//        snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
//    }
//}
