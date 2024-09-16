//
//  SnackbarModifier.swift
//  iosApp
//
//  Created by Tusmit Shah on 14/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import PopupView

struct SnackbarModifier : ViewModifier {
    @Binding var isPresented : Bool
    let message : String
    let title : String
    let type : ToastStyle
    
    func body(content : Content) -> some View {
        content
            .popup(isPresented: $isPresented){
                SnackbarView(type: type, title: title, message: message)
            } customize: {
                $0
                    .type(.floater(verticalPadding: 0, horizontalPadding: 0, useSafeAreaInset: true))
                    .position(.bottom)
                    .animation(.easeInOut)
                    .autohideIn(3)
                    .closeOnTap(true)
                    .closeOnTapOutside(false)
                    .dragToDismiss(true)
                    .useKeyboardSafeArea(false)
            }
    }
    
}


extension View {
    func snackbar(isPresented : Binding<Bool>, type : ToastStyle, title : String, message : String) -> some View{
        self.modifier(SnackbarModifier(isPresented: isPresented, message: message, title: title, type: type))
    }
}
