//
//  AuthenticationView.swift
//  iosApp
//
//  Created by Tusmit Shah on 22/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

public enum AuthenticationDestination : Codable, Hashable {
    
    case signInView, verifyAccountView, verifyOtpView, loginView, loginWithOtpView, otpSentSuccess, otpSentFailure, forgotPasswordView, resetPasswordView
    
}

class GetSelectedTab : ObservableObject {
    
    @Published var _selectedTab : AuthenticationDestination = .signInView
    @Published var isResetPasswordViewActive : Bool = false
    @Published var isVerifyAccountTrue : [String:Bool] = ["Login":false]
    @Published var isOtpSentFrom : [String:Bool] = ["LoginWithOtp":false]
}

struct AuthenticationView: View {
    
    @Binding var isSignUpSheetActive : Bool
    let width : CGFloat
    let height : CGFloat
    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var isSelectedTab : AuthenticationDestination = .signInView
    @State private var selectedTransition : AnyTransition = .trailingToLeading
    @State private var isNavigatingForward : Bool = true
    
    var body: some View {
        ZStack{
            
                // Authentication Views

                
                if isSelectedTab == .loginView {
                    LMSLoginView(
                        isSignupSheetActive: $isSignUpSheetActive,
                        width: width,
                        height: height,
                        selectedTabIs: selectedTabIs, 
                        accessModel: accessModel,
                        router: router, 
                        snackBar: snackBar
                    )
                }
            
                // OTP Verification Views
            
                if isSelectedTab == .verifyAccountView {
                    LMSVerifyAccountView(
                        width: width,
                        height: height,
                        selectedTabIs: selectedTabIs, 
                        accessModel: accessModel,
                        snackBar: snackBar
                    )
                    .transition(.trailingToLeading)

                }
                
                if isSelectedTab == .verifyOtpView {
                    LMSVerifyOtpView(
                        selectedTabIs: selectedTabIs,
                        isSignUpSheetActive: $isSignUpSheetActive,
                        accessModel: accessModel,
                        router: router, 
                        snackBar: snackBar
                    )
                    .transition(.trailingToLeading)

                }
            
                // Login With Otp Flow
            
                if isSelectedTab == .loginWithOtpView{
                    LMSLoginWithOtpView(
                        isSignupSheetActive: $isSignUpSheetActive,
                        width: width,
                        height: height,
                        selectedTabIs: selectedTabIs, 
                        accessModel: accessModel, 
                        snackBar: snackBar
                    )
                    .transition(.trailingToLeading)
                }
                
                if isSelectedTab == .otpSentSuccess {
                    LMSOTPSentView(
                        selectedTabIs: selectedTabIs
                    )
                    .transition(.trailingToLeading)
                }
                
                if isSelectedTab == .otpSentFailure {
                    OTPSentFaliureView(
                        selectedTabIs: selectedTabIs
                    )
                    .transition(.trailingToLeading)
                }
            
            // Forgot password Flow
            
            if isSelectedTab == .forgotPasswordView {
                ForgotPasswordSelectiveView(
                    isSignupSheetActive: $isSignUpSheetActive,
                    width: width,
                    height: height,
                    selectedTabIs: selectedTabIs, 
                    accessModel: accessModel
                )
                .transition(.trailingToLeading)
            }
            
            if isSelectedTab == .resetPasswordView {
                ResetPasswordView(
                    isSignupSheetActive: $isSignUpSheetActive,
                    width: width,
                    height: height,
                    selectedTabIs: selectedTabIs, 
                    accessModel: accessModel
                )
                .transition(.trailingToLeading)
            }
            
            
            
        }
        .animation(.easeInOut(duration: 0.3), value: isSelectedTab)
        .onReceive(selectedTabIs.$_selectedTab, perform: { tab in
            print("Selected Tab -> \(tab)")
            withAnimation{
                isNavigatingForward = determineNavigationDirection(from: isSelectedTab, to: tab)
                isSelectedTab = tab
            }
            
        })
        .snackbar(isPresented: $snackBar.showSnackbar, type: snackBar.snackBarType, title: snackBar.snackBarTitle, message: snackBar.snackBarMessage)
    
    }
}

private func determineNavigationDirection(from oldTab: AuthenticationDestination, to newTab: AuthenticationDestination) -> Bool {
      // Define your own logic to determine if navigation is forward or backward
      let order: [AuthenticationDestination] = [.signInView, .verifyAccountView, .verifyOtpView]
      guard let oldIndex = order.firstIndex(of: oldTab), let newIndex = order.firstIndex(of: newTab) else {
          return true
      }
      return newIndex > oldIndex
}

#Preview {
    GeometryReader{geometry in
        AuthenticationView(
            isSignUpSheetActive: .constant(true),
            width: geometry.size.width,
            height: geometry.size.height,
            selectedTabIs: GetSelectedTab(),
            accessModel: AccessServiceViewModel(), 
            router: Router(), 
            snackBar: SnackbarModel()
        )
    }
}



//                if isSelectedTab == .signInView {
//                    LMSSignInView(
//                        isSignupSheetActive: $isSignUpSheetActive,
//                        width: width,
//                        height: height,
//                        selectedTabIs: selectedTabIs,
//                        accessModel: accessModel,
//                        router: router
//                    )
//                    .transition(.trailingToLeading)
//
//                }
