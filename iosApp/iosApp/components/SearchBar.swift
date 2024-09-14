//
//  SearchBar.swift
//  iosApp
//
//  Created by Tusmit Shah on 06/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct SearchBar: View {
    
    @Binding var value : String
    @Binding var isFocused : Bool
    @FocusState private var searchFieldIsFocused: Bool
    
    var body: some View {
        HStack{
            TextField("Search for your courses", text: $value, onEditingChanged: {focused in
                    withAnimation{
                        isFocused = focused
                    }
            })
                .focused($searchFieldIsFocused) // Bind focus state to @FocusState
                .padding(.vertical,12)
                .padding(.horizontal,12)
                .padding(.leading,30)
                .font(.custom("Gilroy-Medium", size: 14))
                .foregroundStyle(Color(hex: "#949494"))
                .background(Color(hex: "#F8F8F8"))
                .cornerRadius(8, corners: [.allCorners])
                .overlay(alignment: .leading, content: {
                    Image("search-md")
                        .padding(.leading,12)
                    
                })
                .overlay(content: {
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                })
                
        }
        .onChange(of: searchFieldIsFocused) { newValue in
            withAnimation {
                isFocused = newValue
            }
        }
        .onAppear {
            searchFieldIsFocused = isFocused
        }
    }
}

#Preview {
    SearchBar(
        value: .constant(""),
        isFocused: .constant(true)
    )
}
