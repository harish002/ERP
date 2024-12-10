//
//  ForgotPasswordSelectiveView.swift
//  iosApp
//
//  Created by Tusmit Shah on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Lottie

struct ForgotPasswordSelectiveView: View {
    
    @Binding var isSignupSheetActive : Bool
    let width : CGFloat
    let height : CGFloat
    @ObservedObject var selectedTabIs : GetSelectedTab
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var credentialValue = ""
    @State private var placeHolder = "Enter Email ID"
    
    @State private var isPasswordMailGenerated = false
    
    var body: some View {
        ZStack{
            VStack(spacing:0){
                
                RoundedRectangle(cornerRadius: 2.5)
                    .foregroundStyle(Color(hex: "#3c3c434D"))
                    .frame(width: 36,height: 5)
                    .padding(.top,5)
                
                ScrollView{
                    
                    VStack(spacing:0){
                        CustomHeader(
                            isBackButtonNeeded: true,
                            headerTitle: "Forgot Password",
                            isCloseButtonNeeded: true,
                            onBackButtontapped: {
                                selectedTabIs._selectedTab = .loginView
                            },
                            onCloseButtontapped: {
                                isSignupSheetActive = false
                            }
                        )
                        
                        VStack(alignment:.center,spacing:6){
                            Text("Please enter your credentials")
                                .font(.custom("Gilroy-SemiBold", size: 20))
                                .foregroundStyle(Color(hex: "#4E4E4E"))
                            
                            Text("Below you can choose where you would like to receive your OTP for logging in.")
                                .font(.custom("Gilroy-SemiBold", size: 12))
                                .foregroundStyle(Color(hex: "#949494"))
                                .multilineTextAlignment(.center)
                        }
                        .padding(.horizontal,16)
                        .padding(.top,36)
                        .padding(.bottom,24)
                        
                        
                        VStack(spacing:16){
                            SelectionView(label: $placeHolder)
                            
                            CustomTextField(
                                text: $credentialValue ,
                                label: placeHolder,
                                xOffset: placeHolder == "Enter Phone Number" ? width * 0.25 : width * 0.295,
                                yOffset: 27,
                                accessModel: accessModel,
                                validationOnlyForSignUp: true
                            )
                            .padding(.horizontal,16)
                            
                        }
                        .padding(.bottom,24)
                        
                        
                        LMSCustomButton(
                            isButtonActive: true,
                            buttonTitle: "Get Reset Password Link",
                            buttonTapped: {
                                Task.init{
                                    do
                                    {
                                        let result = try await accessModel.resetPassword(email: credentialValue)
                                        
                                        if !result.isEmpty {
                                            snackBar.show(message: result, title: "Success", type: .success)
                                            withAnimation{
                                                self.isPasswordMailGenerated = true
                                            }
                                        }
                                        else {
                                            snackBar.show(message: "Failed to Send the Mail!", title: "Error", type: .error)
                                        }
                                        
                                    }
                                }
                                
                                //                            selectedTabIs._selectedTab = .verifyOtpView
                                //                            selectedTabIs.isResetPasswordViewActive = true
                                //                            selectedTabIs.isOtpSentFrom["ForgotPassword"] = true
                                //                            selectedTabIs.isOtpSentFrom["LoginWithOtp"] = false
                                
                            }
                        )
                        .padding(.horizontal,16)
                        
                    }
                }
                
                Spacer()
                
                HStack(spacing:8){
                    Image("lock-01")
                    
                    Text("Don’t worry, we won’t share your details anywhere.")
                        .font(.custom("Gilroy-Medium", size: 12))
                        .foregroundStyle(Color("textvalue"))
                    
                }
            }
            .zIndex(0)
            
            if isPasswordMailGenerated {
                MailSendPopUp(email: credentialValue){
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1){
                        self.isPasswordMailGenerated = false
                        selectedTabIs._selectedTab = .loginView
                    }
                }
            }
            
            
        }
        .background(Color("bottomsheet_background"))
    }
}

#Preview {
    GeometryReader{geonetry in
        ForgotPasswordSelectiveView(
            isSignupSheetActive: .constant(false),
            width: geonetry.size.width,
            height: geonetry.size.height,
            selectedTabIs: GetSelectedTab(), 
            accessModel: AccessServiceViewModel(),
            snackBar: SnackbarModel()
        )
    }
//    MailSendPopUp(email: "harish.chouhan@1click.tech")
    
//    LottieView(animationName: "tickJson")
}

struct MailSendPopUp : View {
    let email : String
    let onAnimationEnd : () -> Void
    var body: some View {
        VStack{
            LottieView(animationName: "tickJson"){
                onAnimationEnd()
            }
                .frame(width: 150,height: 150)
            
            Text("Reset Password link sent")
                .font(.custom("Gilroy-Bold", size: 20))
                .padding(.bottom,8)
            
            HStack{
                Text("Please check your inbox")
                    .font(.custom("Gilroy-Medium", size: 14))
                    .foregroundStyle(Color.black).opacity(0.5)
                
                Text("\(email)")
                    .font(.custom("Gilroy-Medium", size: 14))
                    .foregroundStyle(Color.black)
                    
            }
            .padding(.bottom,8)
        }
        .padding(.horizontal,16)
        .padding(.vertical,16)
        .background(Color.white)
        .cornerRadius(8, corners: [.allCorners])
    }
}



struct LottieView: UIViewRepresentable {
    var animationName : String
    var loopMode : LottieLoopMode = .playOnce
    var onAnimationEnd: (() -> Void)? = nil
    
    func makeUIView(context: Context) -> UIView {
        let view = UIViewType(frame: .zero)
        let animationView = LottieAnimationView(name : animationName)
        animationView.contentMode = .scaleAspectFit
        animationView.loopMode = loopMode
        animationView.play { (finished) in
            if finished {
                onAnimationEnd?()
            }
        }
        
        animationView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(animationView)
        
        NSLayoutConstraint.activate([
            animationView.widthAnchor.constraint(equalTo: view.widthAnchor),
            animationView.heightAnchor.constraint(equalTo: view.heightAnchor)
        ])
        
        // Debug output
        print("LottieView created with animation: \(animationName)")
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {}
    
}
