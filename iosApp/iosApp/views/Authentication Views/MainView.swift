//
//  MainView.swift
//  iosApp
//
//  Created by Tusmit Shah on 05/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct MainView: View {
    let width : CGFloat
    let height : CGFloat
    @ObservedObject var router : Router
    @ObservedObject var selectedTab : GetSelectedTab
    @StateObject var accessViewModel : AccessServiceViewModel
    @Binding  var isSignUpSheetActive : Bool
    @ObservedObject var snackBar : SnackbarModel
    
    var body: some View {
        MatchedView(
            onLoginButtonTapped: {
                selectedTab._selectedTab = .loginView
                isSignUpSheetActive = true
            },
            onSignInButtonTapped: {
                selectedTab._selectedTab = .signInView
                isSignUpSheetActive = true
            }, 
            router: router,
            accessModel: accessViewModel,
            snackBar: snackBar
        )
        .sheet(isPresented: $isSignUpSheetActive, content: {
            AuthenticationView(
                isSignUpSheetActive: $isSignUpSheetActive,
                width: width,
                height: height,
                selectedTabIs: selectedTab,
                accessModel: accessViewModel, 
                router: router, 
                snackBar: snackBar
            )
        })
        
    }
}

#Preview {
    GeometryReader {geometry in
        MainView(
            width: geometry.size.width,
            height: geometry.size.height,
            router: Router(),
            selectedTab: GetSelectedTab(),
            accessViewModel: AccessServiceViewModel(),
            isSignUpSheetActive: .constant(false), 
            snackBar: SnackbarModel()
        )
    }
}
