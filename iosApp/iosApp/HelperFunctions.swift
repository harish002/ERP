//
//  HelperFunctions.swift
//  iosApp
//
//  Created by Tusmit Shah on 11/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import UIKit


func openLink(_ urlString: String) {
       if let url = URL(string: urlString) {
           UIApplication.shared.open(url, options: [:], completionHandler: nil)
       }
}
