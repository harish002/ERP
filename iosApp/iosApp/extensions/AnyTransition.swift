//
//  AnyTransition.swift
//  iosApp
//
//  Created by Tusmit Shah on 22/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

extension AnyTransition {
    static var trailingToLeading: AnyTransition {
        AnyTransition.asymmetric(
            insertion: .move(edge: .trailing),
            removal: .move(edge: .leading)
        )
    }
    
    static var leadingTotrailing: AnyTransition {
        AnyTransition.asymmetric(
            insertion: .move(edge: .leading),
            removal: .move(edge: .trailing)
        )
    }
}
