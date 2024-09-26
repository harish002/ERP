//
//  NotificationView.swift
//  iosApp
//
//  Created by Tusmit Shah on 02/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct NotificationView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var navigationState : NavigationState
    @ObservedObject var snackBar : SnackbarModel
    let onBackButtonTap : () -> Void
    
    @State private var exploreTransition = false
    @State private var notifications : [GetNotificationsResponse] = []
    
    var body: some View {
        VStack{
            
            HStack(alignment:.center, spacing:0){
                Button(action: {
                    onBackButtonTap()
                },
                label: {
                    Image(systemName: "chevron.backward")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .foregroundStyle(Color(hex: "#3C3C43"))
                        .frame(width: 16,height: 16)
                        .bold()
                })
               
                
                Spacer()
                
                
                Text("Notifications")
                    .font(.custom("Gilroy-SemiBold", size: 16))
                    .foregroundStyle(Color(hex: "#262626"))
                
                
                Spacer()
                
                
                Rectangle()
                    .frame(width: 16,height: 16)
                    .contentShape(Rectangle())
                    .foregroundStyle(Color(hex: "#F8F8F8"))
                    
            }
            .padding(.vertical,12)
            .padding(.horizontal,16)
            
            Divider()
            
            if notifications.isEmpty {
               Spacer()
                VStack(spacing:16){
                    Image(systemName: "envelope.circle")
                        .resizable()
                        .frame(width: 64,height: 64)
                        .foregroundStyle(Color(hex: "#262626"))
                    Text("You have no new notifications")
                        .foregroundStyle(Color(hex: "#262626"))
                        .font(.custom("Gilroy-Bold", size: 24))
                }
                
                Spacer()
            }
            else
            {
                ScrollView{
                    ForEach(notifications,id: \.self){notification in
                        HStack(alignment: .top, spacing:16){
                            Circle()
                                .foregroundStyle(Color(hex: "#D9D9D9"))
                                .frame(width: 48,height: 48)
                                .overlay(alignment: .center, content: {
                                    Image(systemName: "bell")
                                        .resizable()
                                        .frame(width: 24,height: 24)
                                        .foregroundStyle(Color(hex: "#262626"))
                                    
                                })
                            
                            VStack(alignment: .leading, spacing: 8){
                                Text(notification.type)
                                    .foregroundStyle(Color(hex: "#262626"))
                                    .font(.custom("Gilroy-Bold", size: 16))
                                
                                Text(notification.content)
                                    .foregroundStyle(Color(hex: "#262626"))
                                    .font(.custom("Gilroy-SemiBold", size: 12))
                                
                                Text("03/09/2024, 14:35:41")
                                    .foregroundStyle(Color(hex: "#262626"))
                                    .font(.custom("Gilroy-Medium", size: 12))
                                
                            }
                            
                        }
                        .frame(maxWidth: .infinity,alignment: .leading)
                        .padding(.horizontal,16)
                        .padding(.vertical,12)
                        
                        Divider()
                    }
                }
            }
        }
        .navigationBarBackButtonHidden()
        .background(Color(hex: "#F8F8F8"))
        .onAppear{
            let token = retrieveToken() ?? ""
            let id = "c319ab33-dbf1-45e7-b566-521cfecfb3e5"
            
            Task{
                do
                {
                    let result = try await accessModel.getNotifications(token: token, projectID: id)
                    if !result.isEmpty{
                        self.notifications = result
                    }
                    
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
}

#Preview {
    NotificationView(
        accessModel: AccessServiceViewModel(),
        navigationState: NavigationState(), 
        snackBar: SnackbarModel()
    ){
        
    }
}
