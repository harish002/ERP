//
//  MotorCashless.swift
//  iosApp
//
//  Created by Tusmit Shah on 11/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct Motor : Hashable {
    let name : String
    let link : String
}

struct MotorCashless: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    
    let cashlessList = [
        Motor(name: "ICICI Lombard", link: "https://www.icicilombard.com/cashless-garages"),
        Motor(name: "Bajaj Allianz", link: "https://www.bajajallianz.com/general-insurance-features/car-insurance/cashless-claim-settlement.html"),
        Motor(name: "TATA AIG", link: "https://www.tataaig.com/buy-online/motor-insurance/car-insurance?utm_source=google&utm_medium=cpc&utm_campaign={4W_TAGIC_Brand_Category_Car_Desktop_EM}-tata%20aig%20motor-52147524-87714574775-kwd-21249036639&utm_content=553121219655&gad_source=1&gclid=CjwKCAiA6t-6BhA3EiwAltRFGKLmVD8IUpWTpjRYaDkJFqxq7z-ZN8Vacr7_5DhgHh_U33kQBpUycRoCKmoQAvD_BwE"),
        Motor(name: "Magma HDI", link: "https://www.magmahdi.com/de/more/contact-us?f=g"),
        Motor(name: "Royal Sundaram", link: "https://www.royalsundaram.in/cashless-garage")
        
    ]
    
    var body: some View {
        VStack(spacing:0){
            
            VStack(spacing:0){
                    
                    HStack(spacing:8){
                        
                        Image("back")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                withAnimation{
                                    router.navigateBack()
                                }
                            }
                        
                        Spacer()
                        
                        Image("motor")
                        
                        Text("Motor Cashless")
                            .font(.custom("Poppins-SemiBold", size: 24))
                            .foregroundStyle(Color.black)
                        
                        Spacer()
                        
                        Rectangle()
                            .frame(width: 20, height: 0)
                        
                    }
                    .padding(.horizontal,16)
                    .padding(.vertical,20)
            
            }
            .background(
                Color(hex: "#E3FFF6")
                .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            
            ScrollView(.vertical,showsIndicators: false){
                ForEach(cashlessList,id: \.self){cashless in
                    VStack(spacing:16){
                        HStack{
                            Text(cashless.name)
                                .font(.custom("Poppins-SemiBold", size: 16))
                                .foregroundStyle(Color("title", bundle: nil))
                                .lineLimit(1)
                            
                            Spacer()
                            
                            
                            Image("link")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                                .contentShape(Rectangle())
                                .onTapGesture {
                                    withAnimation{
                                        openLink(cashless.link)
                                    }
                                }
                            
                        }
                        .frame(maxWidth:.infinity,alignment:.leading)
                        .padding(.vertical,20)
                        .padding(.horizontal,20)
                        .background(
                            LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                        )
                        .cornerRadius(12, corners: [.allCorners])
                        .padding(.horizontal,16)
                        .contentShape(Rectangle())
                        .onTapGesture {
                            withAnimation{
                                openLink(cashless.link)
                            }
                        }
                    }
                    .padding(.top,16)
                }
            }
            
            
        }
        .frame(maxWidth:.infinity,maxHeight: .infinity,alignment: .top)
        .background(
            Color(hex: "#F5F8FF")
        )
        .navigationBarBackButtonHidden()
    }
}

#Preview {
    MotorCashless(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState()
    )
}
