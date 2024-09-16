//
//  PolicyRateDetailView.swift
//  iosApp
//
//  Created by Tusmit Shah on 13/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct PolicyRateDetailView: View {
    
    let policyRateDetailViewClosed : () -> Void
    let columns = [
           GridItem(.flexible()),
           GridItem(.flexible()),
    ]
    
    var body: some View {
        VStack(spacing:0){
            HStack(spacing:0){
                Text("Policy Rates")
                    .font(.custom("Gilroy-Bold", size: 32))
                
                Spacer()
                
                Image("close-button")
                    .contentShape(Circle())
                    .onTapGesture {
                        policyRateDetailViewClosed()
                    }
            }
            .padding(.horizontal,16)
            
            LazyVGrid(columns: columns,alignment: .leading,spacing: 16){
                VStack(alignment:.leading,spacing:8){
                    Text("PAYOUT %".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("27")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Insurer".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("CARE")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Insurance Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("Comprehensive")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Vehicle Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("HE")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Renewal Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("Brand New")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("Fuel Type".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("PETROL")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("State".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("HR")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("City Category".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("CAT A")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("City".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                    Text("Gurugram")
                        .font(.custom("Gilroy-Bold", size: 16))
                }
                
                VStack(alignment:.leading,spacing:8){
                    Text("NCB".uppercased())
                        .font(.custom("Gilroy-Medium", size: 12))
                    
                      
                    Text("Yes")
                        .font(.custom("Gilroy-Bold", size: 16))
                        .foregroundStyle(Color.green)

                    
                        
                    
                   
                      
                }

            }
            .padding(.horizontal,16)
            .padding(.top,16)
            
        }
        .padding(.vertical,12)
        .overlay(content: {
            RoundedRectangle(cornerRadius: 5)
                .stroke(.black, lineWidth: 1)
        })
        .padding(.all,8)
        .background(Color.white)
    }
}

#Preview {
    PolicyRateDetailView(){
        
    }
}
