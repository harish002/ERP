//
//  Router.swift
//  iosApp
//
//  Created by Tusmit Shah on 10/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

class Router : ObservableObject {
    
   public enum Destination : Codable, Hashable {
       
       case startscreen, homescreen, notificationscreen
       
   }
    
    @Published var navPath = NavigationPath()
    @Published var subNavPath = NavigationPath()
    
    func navigateTo(to destination : Destination){
        navPath.append(destination)
    }
    
    func navigateBack() {
        navPath.removeLast()
    }
    
    func navigateToRoot() {
        navPath.removeLast(navPath.count)
    }
    
}

class NavigationState: ObservableObject {
    @Published var navigationStack: [(String, Any?)] = []
    @Published var activeViewName: String = "Explore"
    
    func push(viewName: String, data: Any? = nil) {
        navigationStack.append((viewName, data))
        activeViewName = viewName // Update active view name
    }
    
    func pop() {
        // Logic for popping back to the previous view
        if !navigationStack.isEmpty {
            navigationStack.removeLast()
            
            activeViewName = navigationStack.last?.0 ?? "Explore"
        }
    }
    
    var currentView: (String, Any?)? {
        return navigationStack.last
    }
    
    func reset() {
        navigationStack.removeAll()
    }
}

