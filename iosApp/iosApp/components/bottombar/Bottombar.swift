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
        
        HStack(spacing:8){
            
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
                        imageName: "1Asset 13x",
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
                        imageName: "1Asset 23x",
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
                        imageName: "1Asset 33x",
                        isActive: navigationState.activeViewName == "Profile"
                    )
                    
                }
            }
        
            
            // HelpLine -----
            Button{
                withAnimation{
                    dialPhoneNumber(phoneNumber: "8055875587")
                }
            }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Helpline",
                        imageName: "1Asset 43x",
                        isActive: navigationState.activeViewName == "Helpline"
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
                        imageName: "1Asset 7_13x",
                        isActive: navigationState.activeViewName == "Notification"
                    )
                    
                    
                }
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal,16)
        .background(
            LinearGradient(
                gradient: Gradient(colors: [Color("bgColor2", bundle: nil), Color("bgColor2", bundle: nil), Color("bgColor1", bundle: nil)]),
                startPoint: .leading,
                endPoint: .trailing
            )
            .ignoresSafeArea(edges: .bottom) // Extend the gradient to ignore the safe at the bottom
        )
       
    }
    
    func dialPhoneNumber(phoneNumber: String) {
            let phoneNumberString = "tel://\(phoneNumber)"
            if let url = URL(string: phoneNumberString), UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            } else {
                print("No dialer found")
                // You can display a SwiftUI alert if needed
            }
    }
}

#Preview {
    Bottombar(
        navigationState: NavigationState()
    )
}
