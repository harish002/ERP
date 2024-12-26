//
//  SelectionViewWithImage.swift
//  iosApp
//
//  Created by Tusmit Shah on 19/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SelectionViewWithImage: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    let selectionTitle : String
    let image : String
    let filtersList : [Any]
    let onTapOfCard : (String) -> Void
    
    @State private var vehicleType : [VehicleData] = []
    @State private var cityCategories : [CityCategoryData] = []
    @State private var cityData : [CityData] = []
    @State private var vehicleBrands : [BrandData] = []
    @State private var selectedValue : String? = nil
    
    var body: some View {
        VStack(alignment: .center, spacing: 8){
            Text(selectionTitle)
                .font(.custom("Poppins-SemiBold", size: 16))
                .foregroundStyle(Color(hex: "#000000"))
            
            LazyHStack(alignment:.center,spacing:12){
                ForEach(filtersList.indices,id: \.self){i in
                    let singleList = filtersList[i]
                    
                    switch selectionTitle {
                        case "Vehicle Type":
                        if let vehicle = singleList as? VehicleData {
                            CardComponent(title: selectionTitle, value: vehicle.name,valueId: vehicle.id,isSelected: selectedValue == vehicle.id)
                        }
                        
                        default :
                            EmptyView()
                    }
                    
                }
            }

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
    
    @ViewBuilder
    func CardComponent(title : String, value:String, valueId : String, isSelected : Bool) -> some View {
        VStack(alignment:.center,spacing: 15){
            Rectangle()
                .stroke(isSelected ? Color(hex: "#1F2ADC") : Color(hex: "#544C4C").opacity(0.2), style: .init(lineWidth: 2))
                .background(Color(hex: "#FFFFFF"))
                .frame(width: 100,height: 100)
                .overlay(alignment:.center,content: {
                    let imageURL = URL(string: "https://picsum.photos/200/300")
                    AsyncImage(url: imageURL) { phase in
                        if let image = phase.image {
                            Image("image")
                                .resizable()
                                .scaledToFit()
                                .clipShape(Rectangle())
                                .padding(12)
                        }
                        else if phase.error != nil {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20, alignment: .center)
                                
                        }
                        else {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20, alignment: .center)
                                
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
}


// Used for list with more than 15 entries
struct ScrollableSelectionView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    let selectionTitle : String
    let image : String
    let onTapOfCard : (String,String) -> Void
    
    @State private var filtersList: [Any] = [] // Original list
    
    @State private var isViewAllScreenActive: Bool = false
    
    @State private var vehicleType : [VehicleData] = []
    @State private var cityCategories : [CityCategoryData] = []
    @State private var cityData : [CityData] = []
    @State private var vehicleBrands : [BrandData] = []
    @State private var selectedValue : String? = nil
    
    var body: some View {
        VStack(alignment: .center, spacing: 8){
            Text(selectionTitle)
                .font(.custom("Poppins-SemiBold", size: 16))
                .foregroundStyle(Color(hex: "#000000"))
            
            ScrollViewReader{scrollViewProxy in
                ScrollView(.horizontal,showsIndicators: false){
                    LazyHStack(alignment:.top,spacing:12){
                        ForEach(filtersList.indices,id: \.self){i in
                            if i < 8 {
                                let singleList = filtersList[i]
                                
                                switch selectionTitle {
                                case "Vehicle Brand":
                                    if let brand = singleList as? BrandData {
                                        CardComponent(
                                            title: selectionTitle,
                                            value: brand.name,
                                            valueId: brand.id,
                                            isSelected: brand.id == selectedValue
                                        )
                                        .id(i)
                                    }
                                    
                                case "City":
                                    if let city = singleList as? CityData {
                                        CardComponent(
                                            title: selectionTitle,
                                            value: city.name,
                                            valueId: city.id,
                                            isSelected: city.id == selectedValue
                                        )
                                        .id(i)
                                    }
                                    
                                default :
                                    EmptyView()
                                }
                            }
                        }
                        
                        Rectangle()
                            .stroke(Color(hex: "#544C4C").opacity(0.2), style: .init(lineWidth: 2))
                            .background(Color(hex: "#FFFFFF"))
                            .frame(width: 79,height: 74)
                            .overlay(alignment: .center,content: {
                                Text("View All")
                                    .font(.custom("Poppins-Medium", size: 14))
                                    .foregroundStyle(Color(hex: "#000000"))
                            })
                            .contentShape(Rectangle())
                            .onTapGesture {
                                withAnimation{
                                    self.isViewAllScreenActive = true
                                }
                            }
                    }
                    .padding(2)
                }
                .onChange(of: selectedValue) { _ in
                    switch selectionTitle {
                    case "Vehicle Brand":
                        if let index = filtersList.firstIndex(where: { ($0 as? BrandData)?.id == selectedValue }) {
                            // Scroll to the selected element
                            withAnimation {
                                scrollViewProxy.scrollTo(index, anchor: .leading)
                            }
                        }
                    case "City":
                        if let index = filtersList.firstIndex(where: { ($0 as? CityData)?.id == selectedValue }) {
                            // Scroll to the selected element
                            withAnimation {
                                scrollViewProxy.scrollTo(index, anchor: .leading)
                            }
                        }
                        
                    default:break
                    }
                }
            }
            .onAppear {
                self.filtersList = getFilterList(for: selectionTitle) // Initialize the list
            }
        }
        .sheet(isPresented: $isViewAllScreenActive, content: {
            DetailedFilterSection(
                accessModel: accessModel,
                selectionTitle: selectionTitle,
                filtersList: filtersList,
                onTapOfCard: {value in
                    handleSelection(value)
                },
                isViewAllScreenActive: $isViewAllScreenActive,
                selectedValue: $selectedValue
            )
        })
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
    
    func handleSelection(_ valueId: String) {
        
           // Reorder the list to place the selected value at the first index
        switch selectionTitle {
        case "Vehicle Brand":
            if let index = filtersList.firstIndex(where: { ($0 as? BrandData)?.id == valueId }) {
                let selectedItem = filtersList.remove(at: index)
                filtersList.insert(selectedItem, at: 0)
            }
        case "City":
            if let index = filtersList.firstIndex(where: { ($0 as? CityData)?.id == valueId }) {
                let selectedItem = filtersList.remove(at: index)
                filtersList.insert(selectedItem, at: 0)
            }
        default:
            break
        }
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
                            Image("hyundia")
                                .resizable()
                                .scaledToFit()
                                .clipShape(Rectangle())
                                .padding(12)
                        }
                        else if phase.error != nil {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20, alignment: .center)
                                
                        }
                        else {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20, alignment: .center)
                                
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
                onTapOfCard(valueId, title)
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
}

#Preview {
    SelectionViewWithImage(
        accessModel: AccessServiceViewModel(),
        selectionTitle: "Vehicle Type",
        image: "",
        filtersList: [],
        onTapOfCard: {_ in
            
        }
    )
}
