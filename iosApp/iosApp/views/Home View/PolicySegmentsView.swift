//
//  PolicySegmentsView.swift
//  iosApp
//
//  Created by Tusmit Shah on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct Segment : Hashable {
    let name : String
    let iconName : String
}

struct PolicySegmentsView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var router : Router
    
    @Binding var isSheetClosed : Bool
    
    let segmentsList = [
        Segment(name: "Motor", iconName: "motor"),
        Segment(name: "Health", iconName: "health"),
        Segment(name: "Life", iconName: "life"),
        Segment(name: "SME", iconName: "sme")
    ]
    
    let columns = [
           GridItem(.flexible()),
           GridItem(.flexible()),
    ]
    
    var body: some View {
        VStack(spacing:0){
            VStack(spacing:0){
                    
                    HStack(spacing:0){
                        Text("Policy Segments")
                            .font(.custom("Poppins-SemiBold", size: 24))
                            .foregroundStyle(Color.black)
                        
                        Spacer()
                        
                        Image("cross")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24,height: 24)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                self.isSheetClosed = false
                            }
                    
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,20)
            }
            .background(
                Color(hex: "#E3FFF6")
                .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            
            LazyVGrid(columns: columns, spacing: 16) {
                ForEach(segmentsList, id: \.self) {segment in
                    Button(action: {
                        // Button action
                        switch segment.name {
                        case "Motor":
                            print("Motor Page Executed")
                            withAnimation{
                                self.isSheetClosed = false
                            }
                            router.navigateTo(to: .motorview)
                            
                            
                        case "Health":
                            print("Health Page Executed")
                            withAnimation{
                                self.isSheetClosed = false
                            }
                            router.navigateTo(to: .healthview)
                            
                        case "Life":
                            print("Life Page Executed")
                            
                        case "SME":
                            print("SME Page Executed")
                            
                        default:
                            print("No Such Page Found")
                        }
                    })
                    {
                        GeometryReader { geometry in
                            let size = geometry.size.width // Use width for both dimensions

                            RoundedRectangle(cornerRadius: 12)
                                .fill(
                                    LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                                )
                                .frame(width: size, height: size) // Set as a square
                                .overlay(
                                    VStack(spacing: 16) {
                                        Image(segment.iconName)
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: size * 0.3, height: size * 0.3) // Scaled proportionally
                                            .foregroundStyle(Color.black)
                                        
                                        Text(segment.name)
                                            .font(.custom("Poppins-SemiBold", size: size * 0.1)) // Dynamic text size
                                            .foregroundStyle(Color(hex: "#000"))
                                    }
                                )
                                .overlay(
                                    RoundedRectangle(cornerRadius: 12)
                                        .stroke(Color(hex: "#544C4C"), lineWidth: 2)
                                )
                        }
                        .aspectRatio(1, contentMode: .fit) // Ensure square aspect ratio
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)

        }
        .frame(maxWidth:.infinity,maxHeight: .infinity,alignment: .top)
        .background(
            Color(hex:"#F5F8FF")
        )
    }
}

#Preview {
    PolicySegmentsView(accessModel: AccessServiceViewModel(), snackBar: SnackbarModel(), router: Router(), isSheetClosed: .constant(true))
}
