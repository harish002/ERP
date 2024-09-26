//
//  ProfileView.swift
//  iosApp
//
//  Created by Tusmit Shah on 29/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ProfileView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    
    @State private var fullName : String = ""
    @State private var nameInitialis : String = ""
    @State private var emailId : String = ""
    
    var body: some View {
        VStack(spacing:0){
            HStack(alignment:.center,spacing:0){
                Spacer()
                
                Text("Account")
                    .font(.custom("Gilroy-Bold", size: 16))
                    .foregroundStyle(Color(hex: "4E4E4E"))
                   
                
                Spacer()
            }
            .padding(.vertical,12)
            
            Divider()
            
            VStack(spacing:0){
                ScrollView{
                    VStack(spacing:0){
                        VStack(spacing:24){
                            Circle()
                                .frame(width: 110,height: 110)
                                .foregroundStyle(Color(hex: "#D9D9D9"))
                                .overlay(content: {
                                    Text(nameInitialis)
                                        .font(.custom("Gilroy-Medium", size: 40))
                                })
                            
                            Text(fullName)
                                .foregroundStyle(Color(hex: "4E4E4E"))
                                .font(.custom("Gilroy-Medium", size: 24))
                            
                            HStack(alignment:.center,spacing: 10){
                                Image(systemName: "envelope")
                                    .resizable()
                                    .frame(width: 30,height: 24)
                                
                                Text(emailId)
                                    .foregroundStyle(Color(hex: "4E4E4E"))
                                    .font(.custom("Gilroy-Medium", size: 16))
                            }
                            
                        }
                        .padding(.vertical,28)
                        
                        VStack(alignment:.leading,spacing:24){
                            Text("Support")
                                .foregroundStyle(Color.black)
                                .font(.custom("Gilroy-Bold", size: 14))
                                .padding(.horizontal,16)

                            
                            AccountOptions(optionTitle: "About 1Click Policy ERP")
                            
                            Text("Help and Support")
                                .font(.custom("Gilroy-SemiBold", size: 16))
                                .foregroundStyle(Color(hex: "4E4E4E"))
                                .padding(.horizontal,16)
                            
                            Text("Share the 1 Click Policy ERP app")
                                .font(.custom("Gilroy-SemiBold", size: 16))
                                .foregroundStyle(Color(hex: "4E4E4E"))
                                .padding(.horizontal,16)
                            
                         
                        }
                        .frame(maxWidth: .infinity,alignment: .leading)
                        .padding(.vertical,12)
                     
                        
                        
                        
                    }
                    
                }
                
                Spacer()
                
                Text("Sign out")
                    .font(.custom("Gilroy-SemiBold", size: 20))
                    .foregroundStyle(Color(hex: "#3960F6"))
                    .contentShape(Rectangle())
                    .onTapGesture {
                        clearToken()
                        navigationState.reset()
                        router.navigateTo(to: .startscreen)
                    }
                
                Text("1Click Policy ERP v1.0.0")
                    .font(.custom("Gilroy-Medium", size: 14))
                    .padding(.vertical,12)
               
                
            }
            .frame(maxWidth: .infinity)
        }
        .onReceive(accessModel.$userSpecs, perform: {user in
            if let user = user {
                let name = user.name.capitalized
                let surname = user.surname.capitalized
                
                let email = user.email
                self.emailId = email ?? "abc@gmail.com"
                
                extractInitialsAndName(name: name, surname: surname)
            }
        })
    }
    
    @ViewBuilder
    func AccountOptions(optionTitle : String) -> some View {
        HStack(alignment:.center,spacing:0){
            Text(optionTitle)
                .font(.custom("Gilroy-SemiBold", size: 16))
                .foregroundStyle(Color(hex: "4E4E4E"))
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundStyle(Color(hex: "4E4E4E"))
                .frame(width: 16 ,height: 16)
                .bold()
            
        }
        .padding(.horizontal,16)
        .contentShape(Rectangle())
        .onTapGesture {
            withAnimation{
                
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
        self.nameInitialis = "\(nameInitial)\(surnameInitial)"
        self.fullName = nameOfUser
    }
}

#Preview {
    ProfileView(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState()
    )
}
