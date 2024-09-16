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
                            
                        case "Explore" :
                            ExploreView(
                                accessModel: accessModel,
                                snackBar: snackBar,
                                router: router,
                                navigationState: navigationState
                            )
                            .transition(.trailingToLeading)
                            

                            
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
                    navigationState.push(viewName: "Explore")
                }
                else {
                    print("NavigationStack is not Empty!")
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
