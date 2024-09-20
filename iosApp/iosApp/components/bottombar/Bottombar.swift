//
//  Bottombar.swift
//  iosApp
//
//  Created by Tusmit Shah on 06/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

enum BottomBarSelectedTab : Int {
    case profile = 0
    case explore = 1
    case notification = 2
   
}

struct Bottombar: View {
    
    @ObservedObject var navigationState : NavigationState
    
    var body: some View {
        
        HStack{
            
            // Explore -----
            Button{
                    withAnimation{
                        navigationState.push(viewName: "Policy Rates")
                    }
                }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Policy Rates",
                        imageName: "layout-alt-02",
                        isActive: navigationState.activeViewName == "Policy Rates"
                    )
                }
            }

            
            // Notification -----
            Button{
                    withAnimation{
                        navigationState.push(viewName: "Vehicle Number")
                    }
                }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Vehicle Number",
                        imageName: "bell-02",
                        isActive: navigationState.activeViewName == "Vehicle Number"
                    )
                }
            }
            

            
            // Profile -----
            Button{
                withAnimation{
                    navigationState.push(viewName: "Profile")
                }
            }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Profile",
                        imageName: "user-circle",
                        isActive: navigationState.activeViewName == "Profile"
                    )
                }
            }
        }
        .padding(.horizontal,24)
        .background(Color(hex: "#F8F8F8"))
    }
}

#Preview {
    Bottombar(
        navigationState: NavigationState()
    )
}
