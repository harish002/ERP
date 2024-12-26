//
//  DetailedFilterSection.swift
//  iosApp
//
//  Created by Tusmit Shah on 20/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DetailedFilterSection: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    let selectionTitle : String
    let filtersList : [Any]
    let onTapOfCard : (String) -> Void
    @Binding var isViewAllScreenActive : Bool
    @Binding var selectedValue: String? // Bind to parent
    
    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible())
    ]
    
    @State private var searchText: String = ""
    
    @State private var vehicleType : [VehicleData] = []
    @State private var cityCategories : [CityCategoryData] = []
    @State private var cityData : [CityData] = []
    @State private var vehicleBrands : [BrandData] = []
    
    
    var body: some View {
        VStack(alignment: .center, spacing: 16){
            // Search Field
            
            HStack(alignment:.center,spacing:0){
                TextField("Type \(selectionTitle)", text: $searchText)
                    .font(.custom("Poppins-Medium", size: 14))
                    .padding(.horizontal, 16)
                    .padding(.vertical, 10)
                
                Spacer()
                
                Image(systemName: "magnifyingglass")
                    .resizable()
                    .frame(width: 20, height: 20)
                    .foregroundStyle(Color(hex: "#000000"))
                    .padding(.trailing, 16)
                    
            }
            .background(Color(hex: "#F5F5F5"))
            .cornerRadius(6)
            .overlay(
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color(hex: "#C4C4C4"), lineWidth: 1)
            )
            .padding(.horizontal, 16)
             
            
            ScrollView(.vertical,showsIndicators: false){
                LazyVGrid(columns: columns,spacing: 15){
                    ForEach(filtersList.filter {
                        searchText.isEmpty || matchesSearchCriteria(item: $0,selectionTitle: selectionTitle)
                    }.indices,id: \.self){i in
                            let singleList = filtersList[i]
                            
                            switch selectionTitle {
                            case "Vehicle Brand":
                                if let brand = singleList as? BrandData {
                                    CardComponent(title: selectionTitle,
                                                  value: brand.name,
                                                  valueId: brand.id,
                                                  isSelected: brand.id == selectedValue)
                                }
                            case "City":
                                if let city = singleList as? CityData {
                                    CardComponent(title: selectionTitle,
                                                  value: city.name,
                                                  valueId: city.id,
                                                  isSelected: city.id == selectedValue)
                                }
                                
                            default :
                                EmptyView()
                            }
                    }
                }
                .padding(.horizontal,16)
                .padding(.vertical,2)
            }
        }
        .padding(.vertical,16)
        .background(
            Color(hex: "#FFFFFF")
        )
        .frame(maxWidth: .infinity,maxHeight: .infinity,alignment: .top)
        .onReceive(accessModel.$vehicleTypes, perform: {values in
            if !values.isEmpty{
                vehicleType = values
            }
        })
        .onReceive(accessModel.$getAllCityCategories, perform: {values in
            if !values.isEmpty{
                cityCategories = values
            }
        })
        .onReceive(accessModel.$getAllCites, perform: {values in
            if !values.isEmpty{
                cityData = values
            }
        })
        .onReceive(accessModel.$vehicleBrands, perform: {values in
            if !values.isEmpty{
                self.vehicleBrands = values
            }
        })
    
    }
    
    @ViewBuilder
    func CardComponent(title : String, value:String, valueId : String, isSelected : Bool) -> some View {
        VStack(alignment:.center,spacing: 15){
            Rectangle()
                .stroke(isSelected ? Color(hex: "#1F2ADC") : Color(hex: "#544C4C").opacity(0.2), style: .init(lineWidth: 2))
                .background(isSelected ? Color(hex: "#E3FFF6") : Color(hex: "#FFFFFF"))
                .frame(width: 79,height: 74)
                .overlay(alignment:.center,content: {
                    let imageURL = URL(string: "https://picsum.photos/200/300")
                    AsyncImage(url: imageURL) { phase in
                        if let image = phase.image {
                            Image("delhi")
                                .resizable()
                                .scaledToFill()
                                .clipShape(Rectangle())
                                .padding(2)
                        }
                        else if phase.error != nil {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20, alignment: .center)
                                .foregroundStyle(Color(hex: "#C4C4C4"))
                                
                        }
                        else {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20, alignment: .center)
                                .foregroundStyle(Color(hex: "#C4C4C4"))
                                
                        }
                    }
                    
                })
            
            Text(value)
                .font(.custom("Poppins-Medium", size: 14))
                .foregroundStyle(Color(hex: "#000000"))
                .frame(width: 80,alignment: .center)
                .lineLimit(1)
        }
        .contentShape(Rectangle())
        .onTapGesture {
            withAnimation{
                self.selectedValue = valueId
                onTapOfCard(valueId)
                self.isViewAllScreenActive = false
            }
        }
        
    }
    
    func getFilterList(for selectionTitle: String) -> [Any] {
        switch selectionTitle {
        case "Vehicle Type":
            return vehicleType  // Returning the full array of `VehicleData` objects
 // Returning the full array of `StatesData` objects
        case "City Category":
            return cityCategories  // Returning the full array of `CityCategoryData` objects
        case "City":
            return cityData  // Returning the full array of `CityData` objects // Returning the full array of `InsurerData` objects
        case "Vehicle Brand":
            return vehicleBrands

        default:
            return []
        }
    }
    
    private func matchesSearchCriteria(item: Any, selectionTitle: String) -> Bool {
        switch selectionTitle {
        case "Vehicle Type":
            return (item as? VehicleData)?.name.lowercased().contains(searchText.lowercased()) ?? false

        case "City Category":
            return (item as? CityCategoryData)?.name.lowercased().contains(searchText.lowercased()) ?? false
            
        case "City":
            return (item as? CityData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        
        case "Vehicle Brand":
            return (item as? BrandData)?.name.lowercased().contains(searchText.lowercased()) ?? false

        default:
            return false
        }
    }
}

#Preview {
    DetailedFilterSection(
        accessModel: AccessServiceViewModel(),
        selectionTitle: "Vehicle Type",
        filtersList: [],
        onTapOfCard: {_ in
            
        },
        isViewAllScreenActive: .constant(false),
        selectedValue: .constant("")
    )
}
