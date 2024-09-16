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
                        navigationState.push(viewName: "Explore")
                    }
                }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Explore",
                        imageName: "layout-alt-02",
                        isActive: navigationState.activeViewName == "Explore"
                    )
                }
            }

            
            // Notification -----
            Button{
                    withAnimation{
                        navigationState.push(viewName: "Notification")
                    }
                }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Notification",
                        imageName: "bell-02",
                        isActive: navigationState.activeViewName == "Notification"
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
