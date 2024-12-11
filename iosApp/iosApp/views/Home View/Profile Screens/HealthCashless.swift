//
//  HealthCashless.swift
//  iosApp
//
//  Created by Tusmit Shah on 11/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct Health : Hashable {
    let name : String
    let link : String
}

struct HealthCashless: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var router : Router
    @ObservedObject var navigationState : NavigationState
    
    let cashlessList = [
        Health(name: "ICICI Lombard", link: "https://www.icicilombard.com/cashless-hospitals"),
        Health(name: "Bajaj Allianz", link: "https://www.bajajallianz.com/general-insurance-features/health-insurance/cashless-health-insurance.html"),
        Health(name: "Manipal Cigna", link: "https://www.manipalcigna.com/prime-cashless-opd-network"),
        Health(name: "Niva Bupa", link: "https://www.nivabupa.com/health-insurance-plans/get-quote.html?gclid=CjwKCAiA6t-6BhA3EiwAltRFGOksb6FwOeZ7rCGJrND06modJSnf_1U8Z8Dwb0wWLBVB0AqTcyVpmhoCdQsQAvD_BwE&cid=S_Brand_E_NB_Health_Insurance&utm_source=google&utm_medium=cpc&utm_campaign=Google_Search_Niva_Brand_Exact_New&utm_term=niva%20bupa%20health&utm_content=Niva_Bupa_Health_Insurance&ef_id=CjwKCAiA6t-6BhA3EiwAltRFGOksb6FwOeZ7rCGJrND06modJSnf_1U8Z8Dwb0wWLBVB0AqTcyVpmhoCdQsQAvD_BwE:G:s&s_kwcid=AL!7961!3!638197148300!e!!g!!niva%20bupa%20health"),
        Health(name: "Hdfc Ergo", link: "https://www.hdfcergo.com/campaigns/hdfc-ergo-health-insurance-2?&utm_source=google_search_1&utm_medium=cpc&utm_campaign=Health_Search_Brand_Neev-Phrase&utm_adgroup=Generic-Insurance&adid=632972567247&utm_term=hdfc%20ergo%20health%20insurance&utm_network=g&utm_matchtype=p&utm_device=c&utm_location=1007765&utm_sitelink={sitelink}&utm_placement=&ci=googlesearch&SEM=1&gad_source=1&gclid=CjwKCAiA6t-6BhA3EiwAltRFGOjad3aqCEwAYDEQX67CTksTdt0BoiPgg7n3zY6GGHq9t40WVn0D_xoCeSgQAvD_BwE"),
        Health(name: "Hdfc Ergo Cashless Garage", link: "https://www.hdfcergo.com/locators/cashless-garages-networks"),
        Health(name: "Care Insurance", link: "https://www.careinsurance.com/health-plan-network-hospitals.html"),
        Health(name: "Star Health", link: "https://www.starhealth.in/lookup/hospital/")
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
                        
                        Image("healthCashless")
                        
                        Text("Health Cashless")
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
    HealthCashless(
        accessModel: AccessServiceViewModel(),
        router: Router(),
        navigationState: NavigationState()
    )
}
