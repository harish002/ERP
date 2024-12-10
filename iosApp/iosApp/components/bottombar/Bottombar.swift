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
    let onTapOfCenterIcon : () -> Void
    
    var body: some View {
        
        HStack(spacing:8){
            
            // Poilcy Rates -----
            Button{
                    withAnimation{
                        navigationState.push(viewName: "Policy Rates")
                    }
                }
            label: {
                ZStack{
                    BottomBarButtonView(
                        name: "Policy Rates",
                        imageName: "rates",
                        isActive: navigationState.activeViewName == "Policy Rates"
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
            
            
            // Vehicle Number -----
            Button{
                    withAnimation{
                        onTapOfCenterIcon()
                    }
                }
            label: {
                ZStack{
                    Circle()
                        .fill(Color(hex: "#1F2ADC"))
                        .frame(width: 62,height: 62)
                        .overlay(content: {
                            Image("Button4x")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 24, height: 24)
                                .foregroundStyle(Color.white)
                                
                        })
                }
                .padding(.top, 12)
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
                        imageName: "notification",
                        isActive: navigationState.activeViewName == "Notification"
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
                    VStack(spacing:12){
                        Image("help")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24,height: 24)
                            .foregroundStyle(Color(hex: "#04C98B"))
                        
                        
                        Text("Helpline")
                            .font(.custom("Gilroy-Medium", size: 10))
                            .foregroundStyle(Color(hex: "#04C98B"))
                            .minimumScaleFactor(0.5)
                            .lineLimit(1)
                            
                    }
                    .frame(maxWidth: .infinity,alignment: .center)
                    .padding(.top,12)
                }
            }
            
         
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal,16)
        .background(
            Color(hex: "#FFFFFF")
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
    ){
        
    }
}
