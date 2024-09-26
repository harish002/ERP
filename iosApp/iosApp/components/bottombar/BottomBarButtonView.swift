//
//  BottomBarButtonView.swift
//  iosApp
//
//  Created by Tusmit Shah on 06/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct BottomBarButtonView :View {
    
    let name : String
    let imageName : String
    let isActive : Bool
    
    var body: some View {

            VStack(spacing:8){
                Rectangle()
                    .frame(height: 0)
                
                Image(systemName: imageName)
                    .resizable()
                    .frame(width: 24,height: 24)
                    .foregroundStyle(isActive ? Color(hex: "#3960F6") : Color(hex: "#949494"))
                
                Text(name)
                    .font(.custom("Gilroy-Medium", size: 12))
                    .foregroundStyle(isActive ? Color(hex: "#3960F6") : Color(hex: "#949494"))
            }
            .padding(.top,5)
            .padding(.bottom,11)
        
    }
}

#Preview {
    BottomBarButtonView(
        name: "Policy Rates",
        imageName: "book.pages",
        isActive: false)
}
