import SwiftUI

@main
struct iOSApp: App {
    
    // Initialize the Firebase when the app launches
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    @ObservedObject var router = Router()
    @ObservedObject var selectedTab = GetSelectedTab()
    @StateObject var accessViewModel = AccessServiceViewModel()
    @ObservedObject var snackBarManager = SnackbarModel()
    @StateObject var navigationState = NavigationState()
    @State private var isSignUpSheetActive = false
    
	var body: some Scene {
        WindowGroup {
            ZStack{
                NavigationStack(path: $router.navPath){
                    GeometryReader{geometry in
                        MainView(
                            width:geometry.size.width,
                            height:geometry.size.height,
                            router: router,
                            selectedTab: selectedTab,
                            accessViewModel: accessViewModel,
                            isSignUpSheetActive: $isSignUpSheetActive,
                            snackBar: snackBarManager
                        )
                        .navigationDestination(for: Router.Destination.self){ destination in
                            
                            switch destination {
                                
                            case .startscreen :
                                MatchedView(
                                    onLoginButtonTapped: {
                                        selectedTab._selectedTab = .loginView
                                        isSignUpSheetActive = true
                                    },
                                    onSignInButtonTapped: {
                                        selectedTab._selectedTab = .signInView
                                        isSignUpSheetActive = true
                                    },
                                    router: router,
                                    accessModel: accessViewModel,
                                    snackBar: snackBarManager
                                )
                                
                            case .homescreen :
                                HomeView(
                                    router: router,
                                    accessModel: accessViewModel,
                                    snackBar: snackBarManager,
                                    navigationState: navigationState
                                )
                            
                                
                            case .notificationscreen:
                                NotificationView(
                                    accessModel: accessViewModel,
                                    navigationState: navigationState,
                                    snackBar: snackBarManager
                                    
                                )
                                
                            case .editprofilescreen:
                                EditProfileView(
                                    accessModel: accessViewModel,
                                    router: router,
                                    navigationState: navigationState
                                ){
                                    router.navigateBack()
                                }
                                
                            case .policyratedetailview:
                                PolicyRateDetailView(
                                    accessModel: accessViewModel,
                                    snackBar: snackBarManager,
                                    router: router,
                                    policyRateDetailViewClosed: {
                                        router.navigateBack()
                                    })
                            
                            case .vehicledataview :
                                VehicleDataView(
                                    accessModel: accessViewModel,
                                    router: router,
                                    navigationState: navigationState,
                                    snackBar: snackBarManager,
                                    onBackButtonTap: {
                                        router.navigateBack()
                                    })
                                
                            case .motorview :
                                UploadVehicleImageAndNumber(
                                    accessModel: accessViewModel,
                                    snackBar: snackBarManager,
                                    router: router
                                )
                            
                            case .heathDataView:
                                EmptyView()
                            
                            case .healthview:
                                HealthView(
                                    accessModel: accessViewModel,
                                    snackBar: snackBarManager,
                                    router: router
                                )
                            
                            case .lifeview:
                                LifeView(
                                    accessModel: accessViewModel,
                                    snackBar: snackBarManager,
                                    router: router
                                )
                                
                            case .smeview:
                                EmptyView()
                                
                            }
                            
                        }
                       
                        
                    }
                    
                }
                
                
            }
            .snackbar(isPresented: $snackBarManager.showSnackbar, type: snackBarManager.snackBarType, title: snackBarManager.snackBarTitle, message: snackBarManager.snackBarMessage)
            .environmentObject(appDelegate)
            
        }
	}
}
