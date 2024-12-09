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

            VStack(spacing:12){
                Image(imageName)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24,height: 24)
                    .foregroundStyle(isActive ? Color(hex: "#1F2ADC") : Color(hex: "#D3D3D3"))
                
                
                Text(name)
                    .font(.custom("Gilroy-Medium", size: 10))
                    .foregroundStyle(isActive ? Color(hex: "#1F2ADC") : Color(hex: "#D3D3D3"))
                    .minimumScaleFactor(0.5)
                    .lineLimit(1)
                    
            }
            .frame(maxWidth: .infinity,alignment: .center)
            .padding(.top,12)
    }
}

#Preview {
    BottomBarButtonView(
        name: "Policy Rates",
        imageName: "rates",
        isActive: false)
}
