//
//  PushNotificationFile.swift
//  iosApp
//
//  Created by Tusmit Shah on 24/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import UserNotifications
import Firebase
import FirebaseMessaging
import Combine


class AppDelegate: NSObject, UIApplicationDelegate,UNUserNotificationCenterDelegate, MessagingDelegate, ObservableObject {
    
  @Published var fcmToken : String = ""
    
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      
      FirebaseApp.configure()
    
      Messaging.messaging().delegate = self
      UNUserNotificationCenter.current().delegate = self
      
      DispatchQueue.main.async {
              application.registerForRemoteNotifications()
      }

    return true
  }
    
    func application(_ application: UIApplication,
                        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
           Messaging.messaging().apnsToken = deviceToken
    }
    
    // These function receives an token which is an unique identifier for the user or device
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        
        print("FCM Token : \(fcmToken ?? "Empty Token")")
        
        if let rcvdToken = fcmToken {
            DispatchQueue.main.async {
                self.fcmToken = rcvdToken
            }
        }
        else {
            print("FCM Token is Empty!")
        }
        
    }
    
   
}
