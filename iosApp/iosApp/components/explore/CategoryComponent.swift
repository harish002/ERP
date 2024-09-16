//
//  CategoryComponent.swift
//  iosApp
//
//  Created by Tusmit Shah on 07/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CategoryComponentPlain: View {
    let name : String
    var body: some View {
        VStack(spacing:0){
            Text(name.capitalized)
                .font(.custom("Gilroy-SemiBold", size: 12))
                .foregroundStyle(Color(hex: "4E4E4E"))
                .padding(.all,12)
                .background(Color(hex: "#E6E6E6"))
                .cornerRadius(5, corners: [.allCorners])
                
        }
    }
}

struct CategoryComponentWhiteBackground: View {
    var body: some View {
        VStack(spacing:0){
            Text("See All")
                .font(.custom("Gilroy-Bold", size: 12))
                .foregroundStyle(Color(hex: "4E4E4E"))
                .padding(.all,12)
                .background(Color(hex: "#FFFFFF"))
                .cornerRadius(5, corners: [.allCorners])
                .overlay(content: {
                    RoundedRectangle(cornerRadius: 5)
                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                       
                })
            
        }
    }
}

#Preview {
//    CategoryComponentPlain(name: "Humanities")
    CategoryComponentWhiteBackground()
}
