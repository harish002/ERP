//
//  ColumnCards.swift
//  iosApp
//
//  Created by Tusmit Shah on 07/08/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ColumnCards: View {
    
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
                .padding(.bottom,7)
            
            Text(titleOfCourse)
                .lineLimit(4)
                .fixedSize(horizontal: false, vertical: true)
                .foregroundStyle(Color(hex: "#262626"))
                .font(.custom("Gilroy-SemiBold", size: 18))
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
            
            Spacer(minLength: 0)
            
        }
        .frame(width: 155)
        .contentShape(Rectangle())
        .onTapGesture {
            onTapOfCard()
        }
    }
    
    @ViewBuilder
    func ColumnImage(imageURl : String) -> some View {
        let imageUrl = URL(string: imageURl)
        ZStack{
        
            AsyncImage(url: imageUrl){phase in
                switch phase {
                case .empty:
                    ProgressView()
                    
                case .success(let image):
                    image
                        .resizable()
                        .frame(height: 111)
                    
                case .failure(let error):
                    RoundedRectangle(cornerRadius: 5)
                        .foregroundStyle(Color(hex: "#E6E6E6"))
                        .frame(height: 111)
                        .overlay(content: {
                            RoundedRectangle(cornerRadius: 5)
                                .stroke(Color(hex: "#D0D0D0"), lineWidth: 1)
                        })
                        .zIndex(0)
                }
             
            }
                
            
            VStack(spacing:0){
                HStack(spacing:0){
                    Image("certificate-01")
                        .padding(4)
                        .background(Color(hex: "#F8F8F8"))
                        .cornerRadius(4, corners: [.allCorners])
                        .overlay(content: {
                            RoundedRectangle(cornerRadius: 4)
                                .stroke(Color(hex: "#D0D0D0"), lineWidth: 1)
                        })
                    
                    Spacer()
                    
                        
                }
                .padding(.horizontal,8)
                .padding(.top,8)
                
                Spacer()
               
            }
            .frame(height: 111)
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
                    .frame(width: 20,height: 20)
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
                     .frame(width: 20,height: 20)
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
                     .frame(width: 20,height: 20)
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
    ColumnCards(
        titleOfCourse: "Title for the course and",
        creatorName: "Name of the Instructor / Creator",
        ratings: "5.0",
        count: "100",
        amount: 399,
        discountedAmount: 100,
        isCourseFree: false, 
        thumbNailUrl: "https://cdn.sanity.io/images/tlr8oxjg/production/5ff5d5ece10bed416bef03604a02a5224b30cd82-1456x816.png?w=3840&q=80&fit=clip&auto=format"
    ){
        
    }
}

//
//EnrolledImage(
//    image1: "photo",
//    image2: "Google",
//    image3: "Facebook"
//)
//
//Text("+32 people enrolled")
//    .foregroundStyle(Color(hex: "#4E4E4E"))
//    .font(.custom("Gilroy-SemiBold", size: 10))
