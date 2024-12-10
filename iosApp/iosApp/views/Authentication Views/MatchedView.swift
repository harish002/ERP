//
//  MatchedView.swift
//  iosApp
//
//  Created by Tusmit Shah on 12/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct MatchedView: View {
    
    @Namespace var namespace
    @State var show = false
    let onLoginButtonTapped : () -> Void
    let onSignInButtonTapped : () -> Void
    
    @ObservedObject var router : Router
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    
    var body: some View {
        ZStack {
            if !show {
                
                VStack(alignment:.center, spacing: 33) {
                    Spacer()
                    
                    Image("appstore")
                        .resizable()
                        .frame(width: 114, height: 114)
                        .cornerRadius(50, corners: [.allCorners])
                        .matchedGeometryEffect(id: "logo", in: namespace)
                        .padding(.bottom,33)
                        
                       
                    Text("Hamesha Aapke Sath")
                        .matchedGeometryEffect(id: "title", in: namespace)
                        .font(Font.custom("Gilroy-SemiBold", size: 32))
                        .foregroundStyle(Color(hex: "#FBFAFD"))
                        
                    
                    Spacer()
                    
                    Text("Product of 1Click Policy")
                        .font(Font.custom("Gilroy-Medium", size: 12))
                        .foregroundStyle(Color(hex: "#FBFAFD"))
                       
                }
                
            }
            else {
                VStack(alignment:.center,spacing:0) {
                    Spacer()
                        .frame(height: 200)
                    
                    Image("appstore")
                        .resizable()
                        .frame(width: 114, height: 114)
                        .cornerRadius(50, corners: [.allCorners])
                        .matchedGeometryEffect(id: "logo", in: namespace)
                        .padding(.bottom,33)
                    
                       
                    Text("Hamesha Aapke Sath")
                        .matchedGeometryEffect(id: "title", in: namespace)
                        .font(Font.custom("Gilroy-SemiBold", size: 32))
                        .foregroundStyle(Color(hex: "#FBFAFD"))
                    
                    Spacer()
                        .frame(height: 121)
                                           
                    Text("Simplifying Sales for Smarter Policy Partners.")
                        .font(Font.custom("Gilroy-SemiBold", size: 16))
                        .foregroundStyle(Color(hex: "#FBFAFD"))
                        .multilineTextAlignment(.center)
                        .frame(width: 290,height: 52,alignment: .center)
                        .padding(.bottom,32)
                        .lineSpacing(4)
                    
                    
                    HStack(alignment:.center,spacing: 14){
                        HStack{
                            Text("Log in")
                                .font(Font.custom("Gilroy-SemiBold", size: 18))
                                .foregroundStyle(Color(hex: "#1630C2"))
                                .padding(20)
                                .frame(width: 136,height: 44,alignment: .center)
                                .background(Color.white)
                                .cornerRadius(8, corners: [.allCorners])
                                .onTapGesture {
                                    onLoginButtonTapped()
                                }
                        }
                    }
                    
                    Spacer()
                    
                    Text("Product of 1Click Policy")
                        .font(Font.custom("Gilroy-Medium", size: 12))
                        .foregroundStyle(Color(hex: "#FBFAFD"))
                        .padding(.top,12)
                    
                    Spacer()
                        .frame(height:50)
                       
                }
                
            }
            
        }
        .navigationBarBackButtonHidden()
        .frame(maxWidth: .infinity)
        .background(
            Image("SPLASH-BG")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(maxWidth:.infinity,maxHeight: .infinity)
        )
        .ignoresSafeArea()
        .onAppear{
            let token = retrieveToken()
           
            DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: {
                if ((token?.isEmpty) != nil) {
                    withAnimation{
                        router.navigateTo(to: .homescreen)
                    }
                } 
                else {
                    withAnimation(.spring(response: 0.6, dampingFraction: 0.8)){
                        self.show = true
                    }
                }
               
            })
        }
    
        
    }
}

#Preview {
    MatchedView(
        onLoginButtonTapped: {},
        onSignInButtonTapped: {},
        router: Router(),
        accessModel: AccessServiceViewModel(),
        snackBar: SnackbarModel()
    )
    
}
