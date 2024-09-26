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
                        imageName: "book.pages",
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
                    
                    VStack(spacing:8){
                        Rectangle()
                            .frame(height: 0)
                        
                        Image(systemName: "licenseplate")
                            .resizable()
                            .frame(width: 32,height: 24)
                            .foregroundStyle(navigationState.activeViewName == "Vehicle Number" ? Color(hex: "#3960F6") : Color(hex: "#949494"))
                        
                        Text("Vehicle Number")
                            .font(.custom("Gilroy-Medium", size: 12))
                            .foregroundStyle(navigationState.activeViewName == "Vehicle Number" ? Color(hex: "#3960F6") : Color(hex: "#949494"))
                    }
                    .padding(.top,5)
                    .padding(.bottom,11)
                    
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
                        imageName: "person.circle",
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
