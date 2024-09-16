//
//  FullLengthCard.swift
//  iosApp
//
//  Created by Tusmit Shah on 08/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct FullLengthCard: View {
    
    let titleOfCourse : String
    let creatorName : String
    let ratings : String
    let count : String
    let amount : Int32
    let discountedAmount : Int32
    let isCourseFree : Bool
    let thumbNailUrl : String
    let onTapOfCard : () -> Void
    
    var body: some View {
        VStack(alignment:.leading,spacing:0){
            ColumnImage(imageURl: thumbNailUrl)
                .padding(.bottom,8)
            
            Text(titleOfCourse)
                .foregroundStyle(Color(hex: "#262626"))
                .font(.custom("Gilroy-SemiBold", size: 20))
                .padding(.bottom,3)
            
            Text(creatorName)
                .lineLimit(1)
                .foregroundStyle(Color(hex: "#262626"))
                .font(.custom("Gilroy-SemiBold", size: 12))
                .padding(.bottom,8)
            
            HStack(alignment:.center,spacing:4){
                if ratings == "0.00" {
                    Text("Not Rated Yet")
                        .foregroundStyle(Color(hex: "#4E4E4E"))
                        .font(.custom("Gilroy-SemiBold", size: 13))
                }
                else {
                    Image("Star2")
                    
                    Text(ratings)
                        .foregroundStyle(Color(hex: "#4E4E4E"))
                        .font(.custom("Gilroy-SemiBold", size: 13))
                        .offset(y:1)
                    
                    Text("(\(count))")
                        .foregroundStyle(Color(hex: "#4E4E4E"))
                        .font(.custom("Gilroy-SemiBold", size: 13))
                        .offset(y:1)
                }
            }
            .padding(.bottom,8)
            
            HStack(alignment:.center,spacing:4){
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
            .padding(.bottom,12)
            .padding(.leading,2)
            
        }
        .contentShape(Rectangle())
        .onTapGesture {
            onTapOfCard()
        }
    }
    
    @ViewBuilder
    func ColumnImage(imageURl : String) -> some View {
        ZStack{
            let imageUrl = URL(string: imageURl)
            AsyncImage(url: imageUrl){ phase in
                switch phase {
                case .empty:
                    RoundedRectangle(cornerRadius: 5)
                        .foregroundStyle(Color(hex: "#E6E6E6"))
                        .frame(height: 160)
                        .overlay(content: {
                            ProgressView()
                                .padding(.vertical,12)
                        })
                        .zIndex(0)
                    
                    
                case .success(let image):
                    image
                        .resizable()
                        .frame(height: 160)
                    
                case .failure(let error):
                    
                    RoundedRectangle(cornerRadius: 5)
                        .foregroundStyle(Color(hex: "#E6E6E6"))
                        .frame(height: 160)
                        .overlay(content: {
                            Image(systemName: "exclamationmark.triangle")
                        })
                        .zIndex(0)
                        .onAppear{
                            print("Image Failure -> \(error.localizedDescription)")
                        }
                    
                }
             
            }
            
            
            VStack(spacing:0){
                HStack(spacing:0){
                    HStack(alignment:.center,spacing:4){
                        Image("certificate-02")
                            
                      
                        Text("Certified Course")
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                            .font(.custom("Gilroy-SemiBold", size: 12))
                            .offset(y:1)
                    }
                    .padding(6)
                    .background(Color(hex: "#F8F8F8"))
                    .cornerRadius(6, corners: [.allCorners])
                    .overlay(content: {
                        RoundedRectangle(cornerRadius: 6)
                            .stroke(Color(hex: "#D0D0D0"), lineWidth: 1)
                    })
                    
                    Spacer()
                    
                        
                }
                
                Spacer()
                
                HStack(spacing:0){
                    HStack(alignment:.center,spacing:4){
                        
                        Text("Course Instructor: \(creatorName)")
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                            .font(.custom("Gilroy-SemiBold", size: 12))
                        
                    }
                    .padding(.horizontal,6)
                    .padding(.vertical,10)
                    .background(Color(hex: "#F8F8F8"))
                    .cornerRadius(6, corners: [.allCorners])
                    .overlay(content: {
                        RoundedRectangle(cornerRadius: 6)
                            .stroke(Color(hex: "#D0D0D0"), lineWidth: 1)
                    })
                    
                    Spacer()
                }
               
            }
            .padding(.all,10)
            .frame(maxHeight: 160)
            .zIndex(1)
        }
    }
    
    @ViewBuilder
    func EnrolledImage(image1 : String, image2 : String, image3 : String ) -> some View {
        HStack(spacing:0){
            ZStack{
                
               
                Image(image1)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 26,height: 26)
                    .clipShape(
                        Circle()
                    )
                    .overlay(content: {
                        Circle()
                         .stroke(Color(hex: "#F8F8F8"),lineWidth: 3)
                    })

                    
            }
            .zIndex(0)
            ZStack{
                
                 Image(image2)
                     .resizable()
                     .aspectRatio(contentMode: .fit)
                     .frame(width: 26,height: 26)
                     .clipShape(
                         Circle()
                     )
                     .overlay(content: {
                         Circle()
                          .stroke(Color(hex: "#F8F8F8"),lineWidth: 3)
                     })
                 
            }
            .padding(.leading,-6)
            .zIndex(1)
            
            ZStack{
                
                 Image(image3)
                     .resizable()
                     .aspectRatio(contentMode: .fit)
                     .frame(width: 26,height: 26)
                     .clipShape(
                         Circle()
                     )
                     .overlay(content: {
                         Circle()
                          .stroke(Color(hex: "#F8F8F8"),lineWidth: 3)
                     })
            }
            .padding(.leading,-6)
            .zIndex(2)
        }
    }
}

#Preview {
    FullLengthCard(
        titleOfCourse: "Title for the course and",
        creatorName: "Name of the Instructor / Creator",
        ratings: "5.0",
        count: "100",
        amount: 399,
        discountedAmount: 100,
        isCourseFree: false,
        thumbNailUrl: "https://otes-uat-public-bucket.s3.ap-south-1.amazonaws.com/bffdc672-cab0-434d-a001-fa6ebf7eebc9_sci-fi.svg"
    ){
        
    }
}

//
//struct SVGImageView: View {
//    let svgURL: URL
//
//    var body: some View {
//        if let svgImage = SVGKImage(contentsOf: svgURL) {
//            Image(uiImage: svgImage.uiImage)
//                .resizable()
//                .scaledToFit()
//        } else {
//            Image(systemName: "xmark.circle")
//                .resizable()
//                .scaledToFit()
//        }
//    }
//}
