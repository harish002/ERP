//
//  TrialPage.swift
//  iosApp
//
//  Created by Tusmit Shah on 07/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI


struct TrialPage: View {
    @State private var scrollOffset: CGFloat = 0

    var body: some View {
        VStack {
            SearchBarX()
                .padding()
                .background(Color.white)
                .offset(y: scrollOffset < -50 ? -scrollOffset - 50 : 0) // Adjust the search bar position
                .animation(.easeInOut, value: scrollOffset) // Animate the transition
                .zIndex(1) // Ensure the search bar stays on top

            ScrollView {
                VStack {
                    ForEach(0..<30) { index in
                        Text("Item \(index)")
                            .padding()
                            .background(Color.blue)
                            .cornerRadius(8)
                            .padding(.vertical, 4)
                            .frame(maxWidth: .infinity)
                    }
                }
                .background(
                    GeometryReader { geometry in
                        Color.clear
                            .preference(key: ScrollOffsetPreferenceKey.self, value: geometry.frame(in: .global).minY)
                    }
                )
            }
            .onPreferenceChange(ScrollOffsetPreferenceKey.self) { value in
                self.scrollOffset = value
            }
        }
    }
}

struct SearchBarX: View {
    @State private var searchText: String = ""

    var body: some View {
        TextField("Search...", text: $searchText)
            .padding()
            .background(Color(.systemGray6))
            .cornerRadius(10)
            .padding(.horizontal)
    }
}

struct ScrollOffsetPreferenceKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue: CGFloat = 0

    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}


#Preview {
    TrialPage()
}
