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
                Circle()
                    .fill(
                        Color.white
                    )
                    .frame(width: 46,height: 46)
                    .overlay(content: {
                        Image(imageName)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24,height: 24)
                    })
                    .overlay(content: {
                        if isActive {
                            Circle()
                                .stroke(Color("bgColor1", bundle: nil), lineWidth: 3)
                                .frame(width: 56,height: 56)
                        }
                    })
                
                
                Text(name)
                    .font(.custom("Gilroy-Medium", size: 10))
                    .foregroundStyle(Color.white)
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
        imageName: "1Asset 13x",
        isActive: false)
}
