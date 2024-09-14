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
                
                VStack(alignment:.center, spacing: 16) {
                    Spacer()
                    
                    Image("Policy Logo")
                        .resizable()
                        .frame(width: 160, height: 160)
                        .matchedGeometryEffect(id: "logo", in: namespace)
                        
                       
                    Text("1 Click Policy ERP")
                        .matchedGeometryEffect(id: "title", in: namespace)
                        .font(Font.custom("Gilroy-SemiBold", size: 36))
                        
                    
                    Spacer()
                    
                    Text("A product of 1 Click Global family")
                        .font(Font.custom("Gilroy-Medium", size: 12))
                        .foregroundStyle(Color(hex: "#F8F8F8"))
                       
                }
                
            }
            else {
                VStack(alignment:.center) {
                    Spacer()
                    
                    Image("Policy Logo")
                        .resizable()
                        .frame(width: 160, height: 160)
                        .matchedGeometryEffect(id: "logo", in: namespace)
                       
                    Text("1 Click Policy ERP")
                        .matchedGeometryEffect(id: "title", in: namespace)
                        .font(Font.custom("Gilroy-SemiBold", size: 36))
                                           
                    Text("Simplifying Sales for Smarter Policy Agents.")
                        .font(Font.custom("Gilroy-SemiBold", size: 16))
                        .multilineTextAlignment(.center)
                        .padding(.horizontal,16)
                        .padding(.top,2)
                    
                    
                    Spacer()
                    
                    VStack(alignment:.center,spacing: 14){
                        Text("Log in")
                            .padding(20)
                            .font(Font.custom("Gilroy-SemiBold", size: 18))
                            .frame(maxWidth: .infinity)
                            .foregroundStyle(Color.white)
                            .background(Color(hex: "#3960F6"))
                            .cornerRadius(8, corners: [.allCorners])
                            .padding(.horizontal,16)
                            .onTapGesture {
                                onLoginButtonTapped()
                            }
                        
                            
                    }
                    
                    Text("A product of 1 Click Global family")
                        .font(Font.custom("Gilroy-Medium", size: 12))
                        .padding(.top,12)
                       
                }
                
            }
            
        }
        .navigationBarBackButtonHidden()
        .frame(maxWidth: .infinity)
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
