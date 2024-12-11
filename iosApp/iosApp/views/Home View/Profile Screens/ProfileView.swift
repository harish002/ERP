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
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var nameInitialis : String = ""

    
    
    var body: some View {
        VStack(spacing:0){
            VStack(spacing:0){
                    HStack(spacing:0){
                        Text("Settings")
                            .font(.custom("Poppins-SemiBold", size: 28))
                            .foregroundStyle(Color.black)
                        
                        Spacer()
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,16)
                }
                .background(
                    Color(hex: "#E3FFF6")
                    .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
                )
                        
            VStack(spacing:0){
                ScrollView{
                    VStack(spacing:0){
                        
                        Text("Account Settings")
                            .foregroundStyle(Color(hex: "#333"))
                            .font(.custom("Poppins-SemiBold", size: 16))
                            .frame(maxWidth: .infinity,alignment: .leading)
                            .padding(.horizontal,16)
                            .padding(.vertical,33)
                        
                        VStack(alignment:.leading,spacing:30){

                            
                            AccountOptions(optionTitle: "Edit Profile", imageName: "user-circle")
                                .onTapGesture {
                                    router.navigateTo(to: .editprofilescreen)
                                }
                            
                            AccountOptions(optionTitle: "Vehicle History", imageName: "history")
                                .onTapGesture {
                                    snackBar.show(message: "Coming Soon", title: "New Feature", type: .info)
                                }
                            
                            AccountOptions(optionTitle: "Update", imageName: "update")
                                .onTapGesture {
                                    snackBar.show(message: "Coming Soon", title: "New Feature", type: .info)
                                }
                                
                         
                        }
                        .padding(.vertical,30)
                        .background(
                            LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                        )
                        .cornerRadius(12, corners: [.allCorners])
                        .padding(.horizontal,16)
                        
                        Text("Network")
                            .foregroundStyle(Color(hex: "#333"))
                            .font(.custom("Poppins-SemiBold", size: 16))
                            .frame(maxWidth: .infinity,alignment: .leading)
                            .padding(.horizontal,16)
                            .padding(.vertical,33)
                        
                        
                        VStack(alignment:.leading,spacing:30){

                            AccountOptions(optionTitle: "Health Cashless", imageName: "health")
                            
                            AccountOptions(optionTitle: "Motor Cashless", imageName: "motor")
                            
                        }
                        .padding(.vertical,30)
                        .background(
                            LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                        )
                        .cornerRadius(12, corners: [.allCorners])
                        .padding(.horizontal,16)
                        
                        
                        Text("Support and About")
                            .foregroundStyle(Color(hex: "#333"))
                            .font(.custom("Poppins-SemiBold", size: 16))
                            .frame(maxWidth: .infinity,alignment: .leading)
                            .padding(.horizontal,16)
                            .padding(.vertical,33)
                        
                        
                        VStack(alignment:.leading,spacing:30){

                            AccountOptions(optionTitle: "Privacy Policy", imageName: "privacy")
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    openLink("https://1clickpolicy.com/privacy-policy")
                                }
                            
                            AccountOptions(optionTitle: "Terms and Conditions", imageName: "terms&conds")
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    openLink("https://1clickpolicy.com/terms-and-conditions")
                                }
                            
                        }
                        .padding(.vertical,30)
                        .background(
                            LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                        )
                        .cornerRadius(12, corners: [.allCorners])
                        .padding(.horizontal,16)
                        
                        Text("Actions")
                            .foregroundStyle(Color(hex: "#333"))
                            .font(.custom("Poppins-SemiBold", size: 16))
                            .frame(maxWidth: .infinity,alignment: .leading)
                            .padding(.horizontal,16)
                            .padding(.vertical,33)
                        
                        VStack(alignment:.leading,spacing:30){
                            
                            AccountOptions(optionTitle: "Report a Problem", imageName: "report")
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    openLink("https://1clickpolicy.com/complaint")
                                }
                            
                            AccountOptions(optionTitle: "Log out", imageName: "logout")
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    clearToken()
                                    navigationState.reset()
                                    router.navigateTo(to: .startscreen)
                                }
                            
                        }
                        .padding(.vertical,30)
                        .background(
                            LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                        )
                        .cornerRadius(12, corners: [.allCorners])
                        .padding(.horizontal,16)
                        
                     
                    }
                    
                }
                
            }
            .frame(maxWidth: .infinity)
        }
        .background(
            Color(hex: "#F5F8FF")
        )


    }
    
    @ViewBuilder
    func AccountOptions(optionTitle : String , imageName : String) -> some View {
        HStack(alignment:.center,spacing:30){
            
            Image(imageName)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundStyle(Color(hex: "#333"))
                .frame(width: 24 ,height: 24)
                .bold()
            
            Text(optionTitle)
                .font(.custom("Gilroy-Bold", size: 16))
                .foregroundStyle(Color(hex: "#333"))
            
            Spacer()
            
        }
        .padding(.horizontal,20)
        .contentShape(Rectangle())
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
    

}

#Preview {
    ProfileView(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState(),
        snackBar: SnackbarModel()
    )
}


//VStack(alignment:.leading,spacing:20){
//    HStack{
//        Text("Profile")
//            .foregroundStyle(Color.black)
//            .font(.custom("Gilroy-Bold", size: 20))
//        
//        Spacer()
//    }
//    
//    
//    AboutSection()
//    
//}
//.padding(.horizontal,16)
//.padding(.top,16)

//VStack(spacing:24){
//    Circle()
//        .frame(width: 90,height: 90)
//        .foregroundStyle(Color(hex: "#D9D9D9"))
//        .overlay(content: {
//            Text(nameInitialis)
//                .font(.custom("Gilroy-Medium", size: 40))
//        })
//    
//    Text(fullName.capitalized)
//        .foregroundStyle(Color(hex: "4E4E4E"))
//        .font(.custom("Gilroy-Medium", size: 24))
//    
//}
//.padding(.vertical,20)
