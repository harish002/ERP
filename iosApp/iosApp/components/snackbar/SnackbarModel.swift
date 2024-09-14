//
//  SnackbarModel.swift
//  iosApp
//
//  Created by Tusmit Shah on 14/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

class SnackbarModel : ObservableObject {
    @Published var showSnackbar : Bool = false
    @Published var snackBarMessage  = ""
    @Published var snackBarTitle  = ""
    @Published var snackBarType : ToastStyle = .success
 
    func show (message : String, title : String, type: ToastStyle) {
        self.showSnackbar = true
        self.snackBarMessage = message
        self.snackBarTitle = title
        self.snackBarType = type
    }
}
