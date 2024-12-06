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

//Locally Saving the UserId and use it to extend the time span of the token

func saveUserId(userId:String){
    clearUserId()
    UserDefaults.standard.set(userId, forKey: "UserId")
}

func retrieveUserId() -> String? {
    let id = UserDefaults.standard.string(forKey: "UserId")
    return id
}

func clearUserId(){
    UserDefaults.standard.removeObject(forKey: "UserId")
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


// Save Initails and User's Name
func saveInitials(name:String){
    clearInitials()
    UserDefaults.standard.set(name, forKey: "Initials")
}

func retrieveInitials() -> String? {
    let tab = UserDefaults.standard.string(forKey: "Initials")
    return tab
}

func clearInitials(){
    UserDefaults.standard.removeObject(forKey: "Initials")
}


// User's Full Name
func saveName(name:String){
    clearName()
    UserDefaults.standard.set(name, forKey: "name")
}

func retrieveName() -> String? {
    let tab = UserDefaults.standard.string(forKey: "name")
    return tab
}

func clearName(){
    UserDefaults.standard.removeObject(forKey: "name")
}


// Device Registered for remote Notification
func saveDeviceRegistration(isRegistered : Bool){
    clearDeviceRegistration()
    UserDefaults.standard.set(isRegistered, forKey: "deviceRegistration")
}

func retrieveDeviceRegistration() -> Bool? {
    let tab = UserDefaults.standard.bool(forKey: "deviceRegistration")
    return tab
}

func clearDeviceRegistration(){
    UserDefaults.standard.removeObject(forKey: "deviceRegistration")
}


