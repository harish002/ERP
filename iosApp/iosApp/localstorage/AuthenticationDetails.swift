//
//  AuthenticationDetails.swift
//  iosApp
//
//  Created by Tusmit Shah on 05/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

//Locally Saving the Token and Use Overall in the Project

func saveToken(token:String){
    clearToken()
    UserDefaults.standard.set(token, forKey: "AuthToken")
}

func retrieveToken() -> String? {
    let token = UserDefaults.standard.string(forKey: "AuthToken")
    return token
}

func clearToken(){
    UserDefaults.standard.removeObject(forKey: "AuthToken")
}


//Locally Saving the RefreshToken and use it to extend the time span of the token

func saveRefreshToken(refreshToken:String){
    clearRefreshToken()
    UserDefaults.standard.set(refreshToken, forKey: "RefreshToken")
}

func retrieveRefreshToken() -> String? {
    let token = UserDefaults.standard.string(forKey: "RefreshToken")
    return token
}

func clearRefreshToken(){
    UserDefaults.standard.removeObject(forKey: "RefreshToken")
}


// In-App Navigation Screening

func saveTabName(name:String){
    clearTabName()
    UserDefaults.standard.set(name, forKey: "TabName")
}

func retrieveTabName() -> String? {
    let tab = UserDefaults.standard.string(forKey: "TabName")
    return tab
}

func clearTabName(){
    UserDefaults.standard.removeObject(forKey: "TabName")
}



