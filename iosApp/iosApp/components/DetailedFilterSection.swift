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
    var filtersList : [Any]
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
    
    @State private var matchingIndices: [Int] = []
    
    
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
                    ForEach(matchingIndices,id: \.self){i in
                            let singleList = filtersList[i]
                            switch selectionTitle {
                            case "Vehicle Brand":
                                if let brand = singleList as? BrandData {
                                    CardComponent(title: selectionTitle,
                                                  value: brand.name,
                                                  valueId: brand.id,
                                                  isSelected: brand.id == selectedValue,
                                                  imageURl: brand.media_url ?? ""
                                    )
                                }
                            case "City":
                                if let city = singleList as? CityData {
                                    CardComponent(title: selectionTitle,
                                                  value: city.name,
                                                  valueId: city.id,
                                                  isSelected: city.id == selectedValue,
                                                  imageURl: city.media_url ?? ""
                                    )
                                }
                                
                            default :
                                EmptyView()
                            }
                    }
                }
                .padding(.horizontal,16)
                .padding(.vertical,2)
            }
            .onChange(of: searchText) { newValue in
                // Trigger view updates when `searchText` changes
                matchingIndices = getMatchingIndices(searchValue: newValue, selectionTitle: selectionTitle)

            }
        }
        .padding(.vertical,16)
        .background(
            Color(hex: "#FFFFFF")
        )
        .frame(maxWidth: .infinity,maxHeight: .infinity,alignment: .top)
        .onAppear{
            self.matchingIndices = Array(filtersList.indices) // Display all items initially
        }
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
    
    // Function to return matching indices
        private func getMatchingIndices(searchValue: String, selectionTitle: String) -> [Int] {
            if searchValue.isEmpty {
                return Array(filtersList.indices) // Return all indices if search text is empty
            }
            return filtersList.enumerated().compactMap { index, item in
                matchesSearchCriteria(item: item, searchValue: searchValue, selectionTitle: selectionTitle) ? index : nil
            }
        }
    
    private func logSearchTerm(_ term: String) {
        print("User searched for: \(term)")
    }
    
    @ViewBuilder
    func CardComponent(title : String, value:String, valueId : String, isSelected : Bool, imageURl : String) -> some View {
        VStack(alignment:.center,spacing: 15){
            Rectangle()
                .stroke(isSelected ? Color(hex: "#1F2ADC") : Color(hex: "#544C4C").opacity(0.2), style: .init(lineWidth: 2))
                .background(isSelected ? Color(hex: "#E3FFF6") : Color(hex: "#FFFFFF"))
                .frame(width: 79,height: 74)
                .overlay(alignment:.center,content: {
                    let imageURL = URL(string: imageURl)
                    AsyncImage(url: imageURL) { phase in
                        if let image = phase.image {
                            image
                                .resizable()
//                                .aspectRatio(contentMode: .fit)
                                .frame(width: 79,height: 74)
                                .padding(2)
                        }
                        else if phase.error != nil {
                            Image("dummy-image1")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 79,height: 74)
                                
                        }
                        else {
                            Image("dummy-image1")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 79,height: 74)
                                
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
    
    private func matchesSearchCriteria(item: Any,searchValue : String, selectionTitle: String) -> Bool {
        switch selectionTitle {
        case "Vehicle Type":
            return (item as? VehicleData)?.name.lowercased().contains(searchValue.lowercased()) ?? false

        case "City Category":
            return (item as? CityCategoryData)?.name.lowercased().contains(searchValue.lowercased()) ?? false
            
        case "City":
            return (item as? CityData)?.name.lowercased().contains(searchValue.lowercased()) ?? false
        
        case "Vehicle Brand":
            return (item as? BrandData)?.name.lowercased().contains(searchValue.lowercased()) ?? false

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
