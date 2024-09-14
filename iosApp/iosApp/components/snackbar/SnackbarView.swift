//
//  SnackbarView.swift
//  iosApp
//
//  Created by Tusmit Shah on 14/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

enum ToastStyle {
    case error
    
    case success
    
    case info
    
    case warning
}

extension ToastStyle {
    var themeColor : Color {
        switch self {
        case .error : return Color.red
            
        case .success : return Color.green
            
        case .info : return Color.blue
            
        case .warning : return Color.orange
        }
    }
    
    var iconFileName : String {
        switch self {
        case .success : return "checkmark.circle.fill"
            
        case .error : return "xmark.circle.fill"
            
        case .info : return "info.circle.fill"
            
        case .warning: return "exclamationmark.triangle.fill"
        
        }
    }
}

struct SnackbarView: View {
    
    var type: ToastStyle
    var title: String
    var message: String
    
    var body: some View {
        HStack{
            HStack(spacing:12){
                Rectangle()
                    .frame(width: 10)
                    .foregroundStyle(type.themeColor)
                
                HStack(alignment:.top,spacing:12){
                    Image(systemName: type.iconFileName)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                            .foregroundStyle(type.themeColor)
                        
                    VStack(alignment:.leading,spacing:4){
                            Text(title)
                                .font(.custom("Gilroy-Bold", size: 16))
                            
                            Text(message)
                                .font(.custom("Gilroy-SemiBold", size: 12))
                                .minimumScaleFactor(0.6)
                        }
                    }
                
            }
            
            Spacer()
        }
        .frame(maxWidth:.infinity, maxHeight: 55)
        .background(Color.white)
        .cornerRadius(10, corners: [.allCorners])
        .padding(.horizontal,16)
        .shadow(color: .black, radius: 1)
    }
}

#Preview {
    SnackbarView(
        type: .info,
        title: "Info",
        message: "Otp Sent Successfully."
    )
}
