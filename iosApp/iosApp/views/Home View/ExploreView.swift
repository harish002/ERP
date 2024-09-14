//
//  ExploreView.swift
//  iosApp
//
//  Created by Tusmit Shah on 06/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ExploreView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    
    @State private var exploreTransition = false
    @Namespace var nameSpace
    @State private var yOffset = 0.0
    let columns = [
           GridItem(.flexible()),
           GridItem(.flexible()),
    ]
    
    @State private var categoryList : [AllCourseCategoryResponse] = []
    @State private var coursesPublished : [PublishedCourseResponse] = []
    
    @State private var userName : String = ""
    @State private var nameInitials  = ""
    
    @State private var isPolicyRateSelected = false
    @State private var isFilterSelected = false
    
    var body: some View {
        
        ZStack{
            
            VStack(spacing:0){
                    ZStack{
                        VStack(spacing:0){
                            
                            HStack(spacing:0){
                                HStack(spacing:10){
                                    Circle()
                                        .foregroundStyle(Color(hex: "#F8F8F8"))
                                        .frame(width: 42,height: 42)
                                        .overlay(content: {
                                            Text(nameInitials)
                                                .font(.custom("Gilroy-SemiBold", size: 13))
                                        })
                                    
                                    VStack(alignment:.leading,spacing:8){
                                        Text("Welcome, \(userName)")
                                            .font(.custom("Gilroy-SemiBold", size: 13))
                                        
                                        Text("Edit Profile")
                                            .font(.custom("Gilroy-SemiBold", size: 13))
                                            .foregroundStyle(Color(hex:"#3960F6"))
                                        
                                    }
                                }
                                
                                Spacer()
                                
                                NotificationBellView(){
                                    withAnimation{
                                        router.navigateTo(to: .notificationscreen)
                                    }
                                }
                                
                            }
                            .padding(.bottom,23)
                            .padding(.horizontal,16)
                            .padding(.top,12)
                            
                            HStack(spacing:0){
                                Text("Sales Tools")
                                    .matchedGeometryEffect(id: "header", in: nameSpace)
                                    .font(.custom("Gilroy-Bold", size: 28))
                                
                                Spacer()
                                
                                Text("Apply Filter")
                                    .font(.custom("Gilroy-Bold", size: 12))
                                    .padding(.vertical,8)
                                    .padding(.horizontal,12)
                                    .overlay(content: {
                                        RoundedRectangle(cornerRadius: 5)
                                            .stroke(.black, lineWidth: 1)
                                    })
                                    .contentShape(Rectangle())
                                    .onTapGesture {
                                        withAnimation{
                                            isFilterSelected = true
                                        }
                                    }

                                
                            }
                            .padding(.horizontal,16)
                            .padding(.bottom,12)
                            
                        }
                        .background(Color(hex: "#D9D9D9"))
                    }

                
                VStack(spacing:0){
                    
                    ScrollView{
                            
                            ForEach(1..<20){i in
                                
                                HStack(alignment:.top,spacing:12){
                                    Circle()
                                        .foregroundStyle(Color(hex: "#D9D9D9"))
                                        .frame(width: 42,height: 42)
                                        .overlay(content: {
                                            Text("\(i)")
                                                .font(.custom("Gilroy-SemiBold", size: 16))
                                        })
                                    
                                    HStack(alignment:.top,spacing:16){
                                        VStack(alignment:.leading,spacing:4){
                                            Text("PAYOUT %")
                                                .font(.custom("Gilroy-Medium", size: 12))
                                            
                                            Text("27")
                                                .font(.custom("Gilroy-Bold", size: 14))
                                        }
                                        
                                        
                                        VStack(alignment:.leading,spacing:4){
                                            Text("INSURER")
                                                .font(.custom("Gilroy-Medium", size: 12))
                                            
                                            Text("CARE")
                                                .font(.custom("Gilroy-Bold", size: 14))
                                        }
                                        
                                        VStack(alignment:.leading,spacing:4){
                                            Text("INSURANCE TYPE")
                                                .font(.custom("Gilroy-Medium", size: 12))
                                            
                                            Text("Comprehensive")
                                                .font(.custom("Gilroy-Bold", size: 14))
                                        }
                                        
                                        
                                    }
                                    .offset(y:2)
                                    
                                    Spacer()
                                    
                                    Image(systemName: "arrowshape.right.fill")
                                }
                                .padding(.vertical,12)
                                .padding(.horizontal,16)
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    print("Policy Rate Selected")
                                    withAnimation{
                                        isPolicyRateSelected = true
                                    }
                                }
                               
                                
                                Divider()
                            }
                    }
                    
                }
                
            }
            .zIndex(0)
            
            if isPolicyRateSelected {
                PolicyRateDetailView(){
                    withAnimation{
                        isPolicyRateSelected = false
                    }
                }
                .zIndex(1)
            }
            
        }
        .sheet(isPresented: $isFilterSelected, content: {
            ApplyFiltersView{
                withAnimation{
                    isFilterSelected = false
                }
            }
        })
        .background(Color(hex: "#F8F8F8"))
        .onAppear{
            let token = retrieveToken() ?? ""
            Task.init{
                do
                {
                    let data = try await accessModel.getUserData(token: token)

                    let name = data.name.capitalized
                    let surName = data.surname.capitalized

                    extractInitialsAndName(name: name, surname: surName)
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
    
    func extractInitialsAndName(name:String, surname:String){
        
        let nameOfUser = "\(name) \(surname)"
        
        guard let nameInitial = name.first else {
            return
        }
        guard let surnameInitial = surname.first else {
            return
        }
        self.nameInitials = "\(nameInitial)\(surnameInitial)"
        self.userName = nameOfUser
    }
}

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}

struct NotificationBellView : View {
    
    let onTapOfBell : () -> Void
    
    var body: some View {
        ZStack{
            Image("bell-02")
                .zIndex(0)
            
            Circle()
                .fill(Color(hex: "#F63939"))
                .frame(width: 6,height: 6)
                .offset(x: 6,y: -11)
        }
        .padding(.vertical,12)
        .padding(.horizontal,5)
        .contentShape(Circle())
        .onTapGesture {
            onTapOfBell()
        }
    }
}

#Preview {
    ExploreView(
        accessModel: AccessServiceViewModel(),
        snackBar: SnackbarModel(),
        router: Router(), 
        navigationState: NavigationState()
    )
}


