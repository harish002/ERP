//
//  RatingView.swift
//  iosApp
//
//  Created by Tusmit Shah on 26/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct RatingView: View {
    let value : Float
    var body: some View {

            HStack(alignment:.center,spacing:10){
                let formattedValue = String(format: "%.1f", value)
                Text("\(formattedValue)")
                    .font(.custom("Gilroy-SemiBold", size: 16))
                    .foregroundStyle(Color(hex: "#3960F6"))
                    .offset(y:2)
                
                ForEach(0..<5){index in
                    Image(systemName: imageName(for: index, value: value))
                        .foregroundColor(.orange)
                        .frame(width: 20,height: 20)
                }
            }
        
    }
    
    func imageName(for starIndex: Int, value: Float) -> String {
      // Version A
      if value >= Float(starIndex + 1){
        return "star.fill"
      }
        else if Float(value) >= Float(starIndex) + Float(0.5) {
        return "star.leadinghalf.filled"
      }
      else {
        return "star"
      }
            
    }
}

#Preview {
    RatingView(value: 4.6)
}
