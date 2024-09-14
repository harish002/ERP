//
//  MyLearningCard.swift
//  iosApp
//
//  Created by Tusmit Shah on 28/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct MyLearningCard: View {
    
    let titleOfCourse : String
    let creatorName : String
    let ratings : String
    let amount : Int32
    let discountedAmount : Int32
    let isCourseFree : Bool
    let thumbNailUrl : String
    let totalSeconds : Double
    let progressHappenedInSeconds : Double
    let onTapOfCard : () -> Void
        
    var body: some View {
        HStack(alignment:.top,spacing:12){
            ImageViewer(imageURl: thumbNailUrl)
            
            VStack(alignment:.leading,spacing:8){
                Text(titleOfCourse)
                    .foregroundStyle(Color(hex: "#262626"))
                    .font(.custom("Gilroy-SemiBold", size: 18))
                
                Text(creatorName)
                    .lineLimit(1)
                    .foregroundStyle(Color(hex: "#262626"))
                    .font(.custom("Gilroy-SemiBold", size: 14))
                
                HStack{
                    
                    HStack(alignment:.center,spacing:4){
                        
                        HStack(alignment:.center,spacing:4){
                            if let floatValue = Float(ratings){
                                Ratings(value: floatValue)
                            }
                        }
                        
                        Spacer()
                        
                        if !isCourseFree {
                            Image(systemName: "indianrupeesign")
                                .resizable()
                                .frame(width: 8, height: 12)
                                .bold()
                            
                            Text("\(amount)")
                                .foregroundStyle(Color(hex: "#262626"))
                                .font(.custom("Gilroy-Bold", size: 14))
                                .padding(.trailing,2)
                            
                            if !(discountedAmount == 0) {
                                ZStack{
                                    Rectangle()
                                        .frame(width: 30,height: 1)
                                    HStack(alignment:.center,spacing: 2){
                                        Image(systemName: "indianrupeesign")
                                            .resizable()
                                            .frame(width: 6, height: 10)
                                            .bold()
                                        
                                        Text("\(discountedAmount)")
                                            .foregroundStyle(Color(hex: "#262626"))
                                            .font(.custom("Gilroy-Medium", size: 12))
                                    }
                                }
                            }
                        }
                        else {
                            Text("Free")
                                .foregroundStyle(Color(hex: "#262626"))
                                .font(.custom("Gilroy-SemiBold", size: 14))
                        }
                       
                    }
                  
                }
                
                VStack(alignment:.leading,spacing:8){
                    
                    let totalHours = totalSeconds / 3600.0
                    
                    let progressHappenedInHours = progressHappenedInSeconds / 3600.0
                    
                    let progress = progressHappenedInHours / totalHours
                    
                    // Progress bar
                    ProgressView(value: progress)
                        .foregroundStyle(Color(hex: "#3960F6"))
                    
                    HStack{
                        let convertedAmount = String(format: "%.2f", progress * 100)
                        
                        Text("\(convertedAmount) % complete")
                            .foregroundStyle(Color(hex: "#3960F6"))
                            .font(.custom("Gilroy-SemiBold", size: 14))
                        
                        Spacer()
                        
                        Text("\(String(format: "%.2f", progressHappenedInHours)) / \(String(format: "%.2f", totalHours)) hours")
                            .foregroundStyle(Color(hex: "#262626"))
                            .font(.custom("Gilroy-SemiBold", size: 14))
                    }
                }
                .padding(.top,8)
                
            }
        }
        .contentShape(Rectangle())
        .onTapGesture {
            onTapOfCard()
        }
        
    }
    
    
    @ViewBuilder
    func ImageViewer(imageURl : String) -> some View {
        let imageUrl = URL(string: imageURl)
        AsyncImage(url: imageUrl){ phase in
            switch phase {
            case .empty:
                RoundedRectangle(cornerRadius: 5)
                    .foregroundStyle(Color(hex: "#E6E6E6"))
                    .frame(width: 64,height: 64)
                    .overlay(content: {
                        ProgressView()
                            .padding(.vertical,12)
                    })
                    .zIndex(0)
                
                
            case .success(let image):
                image
                    .resizable()
                    .frame(width: 64,height: 64)
                
            case .failure(let error):
                
                RoundedRectangle(cornerRadius: 5)
                    .foregroundStyle(Color(hex: "#E6E6E6"))
                    .frame(width: 64,height: 64)
                    .overlay(content: {
                        Image(systemName: "exclamationmark.triangle")
                    })
                    .zIndex(0)
                    .onAppear{
                        print("Image Failure -> \(error.localizedDescription)")
                    }
                
            @unknown default:
                RoundedRectangle(cornerRadius: 5)
                    .foregroundStyle(Color(hex: "#E6E6E6"))
                    .frame(width: 64,height: 64)
                    .overlay(content: {
                        Image(systemName: "exclamationmark.triangle")
                    })
                    .zIndex(0)
                   
            }
         
        }
    }
    
    @ViewBuilder
    func Ratings(value : Float) -> some View {
        HStack(alignment:.center,spacing:4){
            let formattedValue = String(format: "%.1f", value)
            Text("\(formattedValue)")
                .font(.custom("Gilroy-SemiBold", size: 14))
                .foregroundStyle(Color(hex: "#4E4E4E"))
                .offset(y:1)
            
            ForEach(0..<5){index in
                HStack(spacing:0){
                    Image(systemName: imageName(for: index, value: value))
                        .resizable()
                        .foregroundColor(.orange)
                        .frame(width: 12,height: 12)
                }
            }
        }
    }
}

#Preview {
    MyLearningCard(
        titleOfCourse: "Basics of Computers",
        creatorName: "Anil Singh",
        ratings: "4.0",
        amount: 399,
        discountedAmount: 100,
        isCourseFree: false,
        thumbNailUrl: "https://otes-uat-public-bucket.s3.ap-south-1.amazonaws.com/bffdc672-cab0-434d-a001-fa6ebf7eebc9_sci-fi.svg",
        totalSeconds: 7709.5,
        progressHappenedInSeconds: 500.0
    ){
        
    }
}
