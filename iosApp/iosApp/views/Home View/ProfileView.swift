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
    
    @State private var nameInitialis : String = ""
    
    @State private var emailId : String = ""
    @State private var fullName : String = ""
    @State private var userName : String = ""
    @State private var gender : String = ""
    @State private var birthday : String = ""
    @State private var mobileNumber : String = ""

    
    
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
                                .frame(width: 90,height: 90)
                                .foregroundStyle(Color(hex: "#D9D9D9"))
                                .overlay(content: {
                                    Text(nameInitialis)
                                        .font(.custom("Gilroy-Medium", size: 40))
                                })
                            
                            Text(fullName.capitalized)
                                .foregroundStyle(Color(hex: "4E4E4E"))
                                .font(.custom("Gilroy-Medium", size: 24))
                            
                        }
                        .padding(.vertical,20)
                        
                        Divider()
                        
                        VStack(alignment:.leading,spacing:20){
                            HStack{
                                Text("Profile")
                                    .foregroundStyle(Color.black)
                                    .font(.custom("Gilroy-Bold", size: 20))
                                
                                Spacer()
                            }
                            
                            
                            AboutSection()
                            
                        }
                        .padding(.horizontal,16)
                        .padding(.top,16)
                        
                        Divider()
                        
                        VStack(alignment:.leading,spacing:24){
                            Text("Network")
                                .foregroundStyle(Color("bgColor1", bundle: nil))
                                .font(.custom("Gilroy-Bold", size: 20))
                                .padding(.horizontal,16)

                            
                            AccountOptions(optionTitle: "Health Cashless", url: "https://1clickpolicy.com/about-us")
                            
                            AccountOptions(optionTitle: "Motor Cashless", url: "https://1clickpolicy.com/contact-us")
                         
                        }
                        .frame(maxWidth: .infinity,alignment: .leading)
                        .padding(.vertical,16)
                        
                        Divider()
                        
                        VStack(alignment:.leading,spacing:24){
                            Text("Support")
                                .foregroundStyle(Color("bgColor1", bundle: nil))
                                .font(.custom("Gilroy-Bold", size: 20))
                                .padding(.horizontal,16)

                            
                            AccountOptions(optionTitle: "About Us", url: "https://1clickpolicy.com/about-us")
                            
                            AccountOptions(optionTitle: "Help and Support", url: "https://1clickpolicy.com/contact-us")
                            
                            AccountOptions(optionTitle: "Privacy Policy", url: "https://1clickpolicy.com/privacy-policy")
                            
                         
                        }
                        .frame(maxWidth: .infinity,alignment: .leading)
                        .padding(.vertical,16)
                        
                        Spacer()
                        
                        VStack(alignment:.leading){
                            Text("Sign out")
                                .font(.custom("Gilroy-SemiBold", size: 20))
                                .foregroundStyle(Color.red)
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    clearToken()
                                    navigationState.reset()
                                    router.navigateTo(to: .startscreen)
                                }
                            
                            Text("1 Click Policy ERP v1.0.0")
                                .font(.custom("Gilroy-Medium", size: 14))
                                .padding(.vertical,12)
                        }
                        .frame(maxWidth: .infinity,alignment: .leading)
                        .padding(.horizontal,16)
                     
                        
                    }
                    
                }
                
            }
            .frame(maxWidth: .infinity)
        }
        .onAppear{
            let name = retrieveName() ?? "Full Name"
            self.fullName = name
            
            let initial = retrieveInitials() ?? "?"
            self.nameInitialis = initial
        }
        .onReceive(accessModel.$userSpecs, perform: {user in
            if let user = user {
                self.fullName = user.name ?? "Name of the User"
                
                self.mobileNumber = user.mobileNumber ?? "0000000000"
                
                self.gender = user.gender ?? "MALE"
                
                self.birthday = user.birthDate ?? "dd/MM/yyyy"
                
                self.userName = user.username ?? "username"
                
                let email = user.email
                self.emailId = email ?? "abc@gmail.com"
            }
        })
    }
    
    @ViewBuilder
    func AccountOptions(optionTitle : String , url : String) -> some View {
        HStack(alignment:.center,spacing:0){
            Text(optionTitle)
                .font(.custom("Gilroy-SemiBold", size: 16))
                .foregroundStyle(Color("bgColor2", bundle: nil))
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundStyle(Color("bgColor2", bundle: nil))
                .frame(width: 16 ,height: 16)
                .bold()
            
        }
        .padding(.horizontal,16)
        .contentShape(Rectangle())
        .onTapGesture {
            withAnimation{
                openLink(url)
            }
        }
    }
    
    private func openLink(_ urlString: String) {
           if let url = URL(string: urlString) {
               UIApplication.shared.open(url, options: [:], completionHandler: nil)
           }
    }
    
    @ViewBuilder
    func AboutSection() -> some View {
        HStack(spacing:10){
            VStack(alignment:.leading, spacing: 20){
                HStack(alignment:.top){
                    Text("Full Name")
                        .foregroundStyle(Color.black)
                        .font(.custom("Gilroy-Medium", size: 14))
                        .frame(width: 110,alignment: .leading)
                    
                    VStack(spacing:0){
                        TextField("Full Name",text: $fullName)
                            .padding(.bottom,8)
                            .font(.custom("Gilroy-Bold", size: 16))
                            .disabled(true)
                        
                        Divider()
                            
                       
                    }
                }
                
                HStack(alignment:.top){
                    Text("Mobile Number")
                        .foregroundStyle(Color.black)
                        .font(.custom("Gilroy-Medium", size: 14))
                        .frame(width: 110,alignment: .leading)
                    
                    VStack(spacing:0){
                        TextField("Full Name",text: $mobileNumber)
                            .padding(.bottom,8)
                            .font(.custom("Gilroy-Bold", size: 16))
                            .disabled(true)
                        
                        Divider()
                    }
                }
                
                HStack(alignment:.top){
                    Text("Username")
                        .foregroundStyle(Color.black)
                        .font(.custom("Gilroy-Medium", size: 14))
                        .frame(width: 110,alignment: .leading)
                    
                    VStack(spacing:0){
                        TextField("Full Name",text: $userName)
                            .padding(.bottom,8)
                            .font(.custom("Gilroy-Bold", size: 16))
                            .disabled(true)
                            
                        Divider()
                    }
                }
                
                HStack(alignment:.top){
                    Text("Email-Id")
                        .foregroundStyle(Color.black)
                        .font(.custom("Gilroy-Medium", size: 14))
                        .frame(width: 110,alignment: .leading)
                    
                    VStack(spacing:0){
                        TextField("Full Name",text: $emailId)
                            .padding(.bottom,8)
                            .font(.custom("Gilroy-Bold", size: 16))
                            .disabled(true)
                            
                        Divider()
                    }
                }
                
                HStack(alignment:.top){
                    Text("Gender")
                        .foregroundStyle(Color.black)
                        .font(.custom("Gilroy-Medium", size: 14))
                        .frame(width: 110,alignment: .leading)
                    
                    VStack(spacing:0){
                        TextField("Full Name",text: $gender)
                            .padding(.bottom,8)
                            .font(.custom("Gilroy-Bold", size: 16))
                            .disabled(true)
                            
                        Divider()
                    }
                }
                
                HStack(alignment:.top){
                    Text("Birthdate")
                        .foregroundStyle(Color.black)
                        .font(.custom("Gilroy-Medium", size: 14))
                        .frame(width: 110,alignment: .leading)
                    
                    VStack(spacing:0){
                        TextField("Full Name",text: $birthday)
                            .padding(.bottom,8)
                            .font(.custom("Gilroy-Bold", size: 16))
                            .disabled(true)
                           
                            
                            
                        Divider()
                    }
                }
                
            }
        }
    }
    
    // Function to format the date string
    func formattedDate(from dateString: String) -> String {
        let inputFormatter = DateFormatter()
        inputFormatter.dateFormat = "yyyy-MM-dd"  // Input format
        
        if let date = inputFormatter.date(from: dateString) {
            let outputFormatter = DateFormatter()
            outputFormatter.dateFormat = "dd/MM/yyyy"  // Output format
            return outputFormatter.string(from: date)  // Return formatted date
        }
        else {
            return "Invalid Date"  // Handle invalid date format
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
