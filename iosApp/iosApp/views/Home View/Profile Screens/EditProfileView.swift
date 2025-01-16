//
//  EditProfileView.swift
//  iosApp
//
//  Created by Tusmit Shah on 08/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct EditProfileView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    let onBackButtonTap : () -> Void
    
    @State private var imageUrl : String?
    
    // Profile Details
    @State private var emailId : String = ""
    @State private var fullName : String = ""
    @State private var userName : String = ""
    @State private var gender : String = ""
    @State private var birthday : String = ""
    @State private var mobileNumber : String = ""
    @State private var profileImage : String = ""
    
    var body: some View {
        VStack(spacing:0){
            VStack(spacing:0){
                    HStack(spacing:0){
                        
                        Image("back")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                withAnimation{
                                    onBackButtonTap()
                                }
                            }
                        
                        Spacer()
                        
                        Text("Profile")
                            .font(.custom("Poppins-SemiBold", size: 24))
                            .foregroundStyle(Color.black)
                        
                        Spacer()
                        
                        Rectangle()
                            .frame(width: 20, height: 0)
                        
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,16)
            }
            .background(
                Color(hex: "#E3FFF6")
                    .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            
            ScrollView(.vertical,showsIndicators: false){
                VStack(spacing:0){
                    ZStack{
                        Circle()
                            .stroke(.black, style: .init(lineWidth: 0.5))
                            .frame(width: 127,height: 127)
                            .overlay(content: {
                                let imageURL = URL(string: imageUrl ?? "")
                                AsyncImage(url: imageURL) { phase in
                                    if let image = phase.image {
                                        image
                                            .resizable()
                                            .scaledToFill()
                                            .clipShape(Circle())
                                    }
                                    else if phase.error != nil {
                                        Image("appstore")
                                            .resizable()
                                            .scaledToFill()
                                            .clipShape(Circle())
                                    }
                                    else {
                                        Image("appstore")
                                            .resizable()
                                            .scaledToFill()
                                            .clipShape(Circle())
                                    }
                                }
                                
                            })
                            .zIndex(0)
                        
                        Circle()
                            .fill(Color(hex: "#04C98B"))
                            .frame(width: 40,height: 40)
                            .overlay(content: {
                                Image("camera")
                                    .foregroundStyle(Color.white)
                            })
                            .offset(x: 45, y: 45)
                            .zIndex(1)
                        
                    }
                }
                .padding(.vertical,24)
            
                AboutSection()
                    .padding(.bottom,24)
            }
            
            
            
        }
        .frame(maxWidth:.infinity,maxHeight: .infinity,alignment: .top)
        .background(
            Color(hex: "#F5F8FF")
        )
        .navigationBarBackButtonHidden()
        .onAppear{
            
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
                
                self.imageUrl = user.profileImageId ?? ""
            }
        })
    }
    
    @ViewBuilder
    func AboutSection() -> some View {
        HStack(spacing:10){
            VStack(alignment:.leading, spacing: 20){
                VStack(alignment:.leading,spacing:11){
                    Text("Full Name")
                        .foregroundStyle(Color.black)
                        .font(.custom("Poppins-SemiBold", size: 16))
                    
                    VStack(spacing:0){
                        TextField("",text: $fullName)
                            .padding(.leading,16)
                            .padding(.vertical,12)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex:"#544C4C"))
                            .disabled(true)
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
                            })
                        
                    }
                }
                
                VStack(alignment:.leading,spacing:11){
                    Text("Mobile Number")
                        .foregroundStyle(Color.black)
                        .font(.custom("Poppins-SemiBold", size: 16))
                    
                    VStack(spacing:0){
                        TextField("",text: $mobileNumber)
                            .padding(.leading,16)
                            .padding(.vertical,12)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex:"#544C4C"))
                            .disabled(true)
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
                            })
                        
                    }
                }
                
                VStack(alignment:.leading,spacing:11){
                    Text("Username")
                        .foregroundStyle(Color.black)
                        .font(.custom("Poppins-SemiBold", size: 16))
                    
                    VStack(spacing:0){
                        TextField("",text: $userName)
                            .padding(.leading,16)
                            .padding(.vertical,12)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex:"#544C4C"))
                            .disabled(true)
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
                            })
                            
                        Divider()
                    }
                }
                
                VStack(alignment:.leading,spacing:11){
                    Text("Email-Id")
                        .foregroundStyle(Color.black)
                        .font(.custom("Poppins-SemiBold", size: 16))
                    
                    VStack(spacing:0){
                        TextField("",text: $emailId)
                            .padding(.leading,16)
                            .padding(.vertical,12)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex:"#544C4C"))
                            .disabled(true)
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
                            })
                            
                        Divider()
                    }
                }
                
                VStack(alignment:.leading,spacing:11){
                    Text("Gender")
                        .foregroundStyle(Color.black)
                        .font(.custom("Poppins-SemiBold", size: 16))
                    
                    VStack(spacing:0){
                        TextField("",text: $gender)
                            .padding(.leading,16)
                            .padding(.vertical,12)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex:"#544C4C"))
                            .disabled(true)
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
                            })
                            
                    }
                }
                
                VStack(alignment:.leading,spacing:11){
                    Text("Date of Birth")
                        .foregroundStyle(Color.black)
                        .font(.custom("Poppins-SemiBold", size: 16))
                    
                    VStack(spacing:0){
                        TextField("",text: $birthday)
                            .padding(.leading,16)
                            .padding(.vertical,12)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex:"#544C4C"))
                            .disabled(true)
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 6)
                                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
                            })
                           
                    }
                }
                
            }
        }
        .padding(.horizontal,16)
        .padding(.vertical,16)
        .background(
            LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
        )
        .cornerRadius(12, corners: [.allCorners])
        .padding(.horizontal,16)
        
    }
    

}

#Preview {
    EditProfileView(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState(),
        onBackButtonTap: {
            
        })
}
