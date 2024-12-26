//
//  ApplyFiltersView.swift
//  iosApp
//
//  Created by Tusmit Shah on 13/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ApplyFiltersView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    let applyFiltersViewClosed : () -> Void

    
    @State private var dropDownViewSelected : [String : Bool] = [
       "Vehicle Type" : false,
       "Fuel Type" : false,
       "NCB" : false,
       "State" : false,
       "City Category" : false,
       "City" : false,
       "Insurance Type" : false,
       "Renewal Type" : false,
       "Insurer" : false,
       "Vehicle Brand" : false,
       "Vehicle Model" : false
    ]
    
    
    @State private var selectedValue : [String : String] = [
        "Vehicle Type" : "",
        "Fuel Type" : "",
        "NCB" : "",
        "State" : "",
        "City Category" : "",
        "City" : "",
        "Insurance Type" : "",
        "Renewal Type" : "",
        "Insurer" : "",
        "Vehicle Brand" : "",
        "Vehicle Model": ""
    ]
    
    @State private var submittingValue : [String : String] = [
        "Vehicle Type" : "",
        "Fuel Type" : "",
        "NCB" : "",
        "State" : "",
        "City Category" : "",
        "City" : "",
        "Insurance Type" : "",
        "Renewal Type" : "",
        "Insurer" : "",
        "Vehicle Brand" : "",
        "Vehicle Model": ""
    ]
    
    @State private var searchText: String = "" // State for search text
    
    @State private var isViewAllSheetActive = false
    @State private var selectedViewAllTitle = ""
    
    // All the filters options List
    @State private var vehicleType : [VehicleData] = []
    @State private var fuelType : [FuelTypeData] = []
    @State private var statesData : [StatesData] = []
    @State private var cityCategories : [CityCategoryData] = []
    @State private var cityData : [CityData] = []
    @State private var insuranceTypes : [InsuranceTypeData] = []
    @State private var renewalTypes : [RenewalTypeData] = []
    @State private var insurerType : [InsurerData] = []
    @State private var vehicleBrands : [BrandData] = []
    @State private var vehicleModels : [ModelData] = []
    
    let NCBTypes : [String] = ["Yes", "No"]
    
    var body: some View {
        VStack(alignment:.leading,spacing:0){
            HStack(spacing:0){
                Text("Filters")
                    .font(.custom("Poppins-SemiBold", size: 24))
            
                Spacer()
                
                Image("close-button")
                    .contentShape(Circle())
                    .onTapGesture {
                        applyFiltersViewClosed()
                    }
            }
            .padding(.vertical,20)
            .padding(.horizontal,16)
            .background(
                Color(hex: "#E3FFF6")
                .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            
            VStack(spacing:0){
                ScrollView(.vertical,showsIndicators: false){
                    
                    VStack(alignment:.leading,spacing:16){
                        
                        
                        SelectionViewWithImage(
                            accessModel: accessModel,
                            selectionTitle: "Vehicle Type",
                            image: "",
                            filtersList: getFilterList(for: "Vehicle Type")
                        ){ value in
                            submittingValue["Vehicle Type"] = value
                        }
                        

                        ScrollableSelectionView(
                            accessModel: accessModel,
                            selectionTitle: "Vehicle Brand",
                            image: "",
                            onTapOfCard: {value,title  in
                                submittingValue["Vehicle Brand"] = value
                                self.selectedViewAllTitle = title
                            }
                        )
                        
                        selectionView(selectionTitle: "Vehicle Model", staticValue: "Select Vehicle Model")
                        
                        selectionView(selectionTitle: "Fuel Type", staticValue: "Select Fuel Type")
                        
                        selectionView(selectionTitle: "NCB", staticValue: "Select Status")
                        
                        
                        selectionView(selectionTitle: "State", staticValue: "Select State")
                        
                        selectionView(selectionTitle: "City Category", staticValue: "Select City Category")
                        
                        
                        ScrollableSelectionView(
                            accessModel: accessModel,
                            selectionTitle: "City",
                            image: "",
                            onTapOfCard: {value,title  in
                                submittingValue["City"] = value
                            }
                        )
                        
                        selectionView(selectionTitle: "Insurance Type", staticValue: "Select Insurance Type")
                        
                        selectionView(selectionTitle: "Renewal Type", staticValue: "Select Renewal Type")
                        
                        selectionView(selectionTitle: "Insurer", staticValue: "Select Insurer")
                    }
                    .padding(.horizontal,2)
          
                }
            }
            .padding(.horizontal,16)
            .padding(.vertical,16)
            .background(
                LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
            )
            .cornerRadius(12, corners: [.allCorners])
            
            Spacer()
            
            
            VStack(alignment: .leading,spacing: 0){
                LMSCustomButton(
                    isButtonActive: true,
                    buttonTitle: "Apply Filters"
                ){
                    let payload = SearchPolicyRatePayload(
                        state_id: submittingValue["State"] ?? "",
                        city_id: submittingValue["City"] ?? "",
                        city_category_id: submittingValue["City Category"] ?? "",
                        vehicle_type_id: submittingValue["Vehicle Type"] ?? "",
                        vehicle_model_id: "",
                        renewal_type_id: submittingValue["Renewal Type"] ?? "",
                        insurance_type_id: submittingValue["Insurance Type"] ?? "",
                        insurer_id: submittingValue["Insurer"] ?? "",
                        fuel_type_id: submittingValue["Fuel Type"] ?? "",
                        status: submittingValue["NCB"] ?? "0", page: 1, size: 50
                    )
                    print("Apply Filter Payload -> \(payload)")
                    let token = retrieveToken() ?? ""
                    
                    Task.init{
                        do
                        {
                            let (result,response) = try await accessModel.searchPolicyRates(token: token, searchPayload: payload)
                            if result {
                                applyFiltersViewClosed()
                            }
                            else {
                                applyFiltersViewClosed()
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                                    snackBar.show(message: "No Data Found, for the filters applied.", title: "No Data", type: .warning)
                                })
                                
                            }
                        }
                        catch ApiError.networkFailure {
                            // Handle network failure, e.g., show error Snackbar
                            snackBar.show(message: "Network Failure. Please check your connection.", title: "Error", type: .error)
                        } catch ApiError.lowInternetConnection {
                            // Handle low internet connection, e.g., show error Snackbar
                            snackBar.show(message: "Connection Timed Out. Please try again.", title: "Error", type: .error)
                        } catch ApiError.serverError(let status) {
                            // Handle server errors, e.g., show error Snackbar
                            snackBar.show(message: "Server Error: \(status)", title: "Error", type: .error)
                        } catch ApiError.unknownError(let description){
                            // Handle unknown errors
                            print("Data Fetching Failed -> \(description)")
                            snackBar.show(message: description, title: "Error", type: .error)
                        }
                        
                    }
                    
                }
            }
            .padding(.horizontal,16)
            .padding(.vertical,16)
            
           
         
        }
        .background(Color(hex: "#F5F8FF"))
        .onAppear{
            let _ = retrieveToken() ?? ""

        }
        .onReceive(accessModel.$vehicleTypes, perform: {values in
            if !values.isEmpty{
                vehicleType = values
            }
        })
        .onReceive(accessModel.$fuelTypes, perform: {values in
            if !values.isEmpty{
                fuelType = values
            }
        })
        .onReceive(accessModel.$getAllStatesData, perform: {values in
            if !values.isEmpty{
                statesData = values
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
        .onReceive(accessModel.$insuranceTypes, perform: {values in
            if !values.isEmpty{
                insuranceTypes = values
            }
        })
        .onReceive(accessModel.$renewalTypes, perform: {values in
            if !values.isEmpty{
                renewalTypes = values
            }
        })
        .onReceive(accessModel.$insurerTypes, perform: {values in
            if !values.isEmpty{
                insurerType = values
            }
        })
        .onReceive(accessModel.$vehicleBrands, perform: {values in
            if !values.isEmpty{
                self.vehicleBrands = values
            }
        })
        .onReceive(accessModel.$vehicleModels, perform: {values in
            if !values.isEmpty{
                self.vehicleModels = values
            }
        })
        
        
    }
    

    
    
    @ViewBuilder
    func selectionView(selectionTitle : String, staticValue : String) -> some View {
        
        let filters = getFilterList(for: selectionTitle)
        
        @State var searchText : String = ""
        
        VStack(alignment:.leading,spacing:8){
            Text(selectionTitle)
                .font(.custom("Poppins-SemiBold", size: 16))
                .padding(.leading,5)
            
            VStack(alignment:.leading,spacing:0){
                HStack(spacing:0){
                    
                    if let value = selectedValue[selectionTitle]  {
                        if selectedValue[selectionTitle] == "" {
                            Text(staticValue)
                                .font(.custom("Poppins-Medium", size: 14))
                                .foregroundStyle(Color(hex: "#C4C4C4"))
                                .onAppear{
                                    print("\(selectionTitle) -> \(value)")
                                }
                        }
                        else {
                            Text(value)
                                .font(.custom("Poppins-Medium", size: 14))
                                .foregroundStyle(Color(hex: "#000000"))
                                .onAppear{
                                    print("\(selectionTitle) -> \(value)")
                                }
                        }
                        
                    }
                    else {
                        Text(staticValue)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex: "#C4C4C4"))
                            .lineLimit(1)
                            .minimumScaleFactor(0.7)
                    }
                    
                    Spacer()
                    
                    Image("dropdown")
                        .resizable()
                        .frame(width: 20,height: 20)
                        .foregroundStyle(Color(hex: "#000000"))
                        .padding(.bottom,5)
                }
                .padding(.horizontal,16)
                
                
                if dropDownViewSelected[selectionTitle] ?? false {
                    VStack(alignment:.leading,spacing:0){
                        
                        HStack{
                            Text(staticValue)
                                .font(.custom("Poppins-Medium", size: 14))
                                .foregroundStyle(Color(hex: "#C4C4C4"))
                           
                            Spacer()
                            
                        }
                        .padding(.horizontal,16)
                        .padding(.vertical,16)
                        .background(
                            selectedValue[selectionTitle] == "" ?
                            Color(hex: "#E3FFF6") : Color.clear
                        )
                        .contentShape(Rectangle())
                        .onTapGesture {
                            withAnimation{
                                selectedValue[selectionTitle] = ""
                                submittingValue[selectionTitle] = ""
                                dropDownViewSelected[selectionTitle] = false
                                print("\(String(describing: selectedValue[selectionTitle]))")
                            }
                        }
                        
                      
                        ForEach(filters.indices,id: \.self){i in
                            let singleList = filters[i]
                            
                            switch selectionTitle {
                                case "Vehicle Type":
                                if let vehicle = singleList as? VehicleData {
                                    singleFilterValue(title: selectionTitle, value: vehicle.name, valueId: vehicle.id)
                                }
                                
                                case "Fuel Type":
                                if let fuel = singleList as? FuelTypeData {
                                    singleFilterValue(title: selectionTitle, value: fuel.name, valueId: fuel.id)
                                }
                                
                                case "NCB":
                                if let ncb = singleList as? String {
                                    if ncb == "Yes"{
                                        singleFilterValue(title: selectionTitle, value: ncb, valueId: "1")
                                    }
                                    else {
                                        singleFilterValue(title: selectionTitle, value: ncb, valueId: "0")
                                    }
                                    
                                }
                                
                                case "State":
                                if let state = singleList as? StatesData {
                                    singleFilterValue(title: selectionTitle, value: state.name, valueId: state.id)
                                }
                                
                                case "City Category":
                                if let cityCategory = singleList as? CityCategoryData {
                                    singleFilterValue(title: selectionTitle, value: cityCategory.name, valueId: cityCategory.id)
                                }
                                
                                case "City":
                                if let city = singleList as? CityData {
                                    singleFilterValue(title: selectionTitle, value: city.name, valueId: city.id)
                                }
                                
                                case "Insurance Type":
                                if let insurance = singleList as? InsuranceTypeData {
                                    singleFilterValue(title: selectionTitle, value: insurance.name, valueId: insurance.id)
                                }
                                
                                case "Renewal Type":
                                if let renewal = singleList as? RenewalTypeData {
                                    singleFilterValue(title: selectionTitle, value: renewal.name, valueId: renewal.id)
                                }
                                
                                case "Insurer" :
                                if let insurer = singleList as? InsurerData {
                                    singleFilterValue(title: selectionTitle, value: insurer.name, valueId: insurer.id)
                                }
                                
                                case "Vehicle Brand":
                                if let brand = singleList as? BrandData {
                                    singleFilterValue(title: selectionTitle, value: brand.name, valueId: brand.id)
                                }
                                
                                case "Vehicle Model":
                               
                                if let model = singleList as? ModelData {
                                    if model.vehicle_brand_id == submittingValue["Vehicle Brand"] {
                                        singleFilterValue(title: selectionTitle, value: model.name, valueId: model.id)
                                    }
                                }
                                
                                default :
                                    EmptyView()
                            }
                            
                        }
                        
                    }
                    .padding(.top,8)
                    
                }
                
            }
            .padding(.vertical,12)
            .overlay{
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color(hex: "#544C4C"),lineWidth: 1)
            }
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation{
                    dropDownViewSelected[selectionTitle]?.toggle()
                }
            }
        }
    }
    
    @ViewBuilder
    func singleFilterValue(title: String,value: String, valueId : String) -> some View{
        HStack{
            Text(value)
                .font(.custom("Poppins-Medium", size: 14))
                .foregroundStyle(Color(hex: "#000000"))
            
            Spacer()
             
        }
        .padding(.horizontal,16)
        .padding(.vertical,16)
        .background(
            selectedValue[title] == value ?
            Color(hex: "#E3FFF6") : Color.clear
        )
        .contentShape(Rectangle())
        .onTapGesture {
            withAnimation{
                selectedValue[title] = value
                submittingValue[title] = valueId
                dropDownViewSelected[title] = false
                print("\(String(describing: selectedValue[title])) :\(String(describing: submittingValue[title]))")
            }
        }
    }
    
    func getFilterList(for selectionTitle: String) -> [Any] {
        switch selectionTitle {
        case "Vehicle Type":
            return vehicleType  // Returning the full array of `VehicleData` objects
        case "Fuel Type":
            return fuelType  // Returning the full array of `FuelTypeData` objects
        case "NCB":
            return NCBTypes  // Still strings, as these are not custom objects
        case "State":
            return statesData  // Returning the full array of `StatesData` objects
        case "City Category":
            return cityCategories  // Returning the full array of `CityCategoryData` objects
        case "City":
            return cityData  // Returning the full array of `CityData` objects
        case "Insurance Type":
            return insuranceTypes  // Returning the full array of `InsuranceTypeData` objects
        case "Renewal Type":
            return renewalTypes  // Returning the full array of `RenewalTypeData` objects
        case "Insurer":
            return insurerType  // Returning the full array of `InsurerData` objects
        case "Vehicle Brand":
            return vehicleBrands
        case "Vehicle Model":
            return vehicleModels
        default:
            return []
        }
    }
    
    
    // Search Field Option
    @ViewBuilder
    func searchSelectionView(selectionTitle: String, staticValue: String) -> some View {
        let filters = getFilterList(for: selectionTitle)
        
        
        VStack(alignment: .leading, spacing: 8) {
            Text(selectionTitle)
                .font(.custom("Poppins-SemiBold", size: 16))
                .padding(.leading, 5)
            
            VStack(alignment: .leading, spacing: 0) {
                HStack(spacing: 0) {
                    if let value = selectedValue[selectionTitle] {
                        if value.isEmpty {
                            Text(staticValue)
                                .font(.custom("Poppins-Medium", size: 14))
                                .foregroundStyle(Color(hex: "#C4C4C4"))
                        } else {
                            Text(value)
                                .font(.custom("Poppins-Medium", size: 14))
                                .foregroundStyle(Color(hex: "#000000"))
                        }
                    } else {
                        Text(staticValue)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(Color(hex: "#C4C4C4"))
                            .lineLimit(1)
                            .minimumScaleFactor(0.7)
                    }
                    Spacer()
                    Image("dropdown")
                        .resizable()
                        .frame(width: 20, height: 20)
                        .foregroundStyle(Color(hex: "#000000"))
                        .padding(.bottom, 5)
                }
                .padding(.horizontal, 16)
                
                // Dropdown results based on `searchText`
                if dropDownViewSelected[selectionTitle] ?? false {
                    LazyVStack(alignment: .leading, spacing: 0) {
                        // Search Field
                        TextField("Search...", text: $searchText)
                            .font(.custom("Poppins-Medium", size: 14))
                            .foregroundStyle(searchText.isEmpty ? Color(hex: "#C4C4C4") : Color(hex: "#000000"))
                            .padding(.horizontal, 16)
                            .padding(.vertical, 16)
                        
                        // Static Value Selection
                        HStack {
                            Text(staticValue)
                                .font(.custom("Poppins-Medium", size: 14))
                                .foregroundStyle(Color(hex: "#C4C4C4"))
                            Spacer()
                        }
                        .padding(.horizontal, 16)
                        .padding(.vertical, 16)
                        .background(
                            selectedValue[selectionTitle]?.isEmpty ?? true
                                ? Color(hex: "#E3FFF6")
                                : Color.clear
                        )
                        .contentShape(Rectangle())
                        .onTapGesture {
                            withAnimation {
                                selectedValue[selectionTitle] = ""
                                submittingValue[selectionTitle] = ""
                                dropDownViewSelected[selectionTitle] = false
                            }
                        }
                        
                        ForEach(filters.filter {
                            searchText.isEmpty || matchesSearchCriteria(item: $0, searchText: searchText, selectionTitle: selectionTitle)
                        }.indices, id: \.self) { i in
                            let singleList = filters[i]
                            
                            switch selectionTitle {
                            case "Vehicle Type":
                                if let vehicle = singleList as? VehicleData {
                                    singleFilterValue(title: selectionTitle, value: vehicle.name, valueId: vehicle.id)
                                }
                            case "Fuel Type":
                                if let fuel = singleList as? FuelTypeData {
                                    singleFilterValue(title: selectionTitle, value: fuel.name, valueId: fuel.id)
                                }
                            case "NCB":
                                if let ncb = singleList as? String {
                                    let id = ncb == "Yes" ? "1" : "0"
                                    singleFilterValue(title: selectionTitle, value: ncb, valueId: id)
                                }
                            case "State":
                                if let state = singleList as? StatesData {
                                    singleFilterValue(title: selectionTitle, value: state.name, valueId: state.id)
                                }
                            case "City Category":
                                if let cityCategory = singleList as? CityCategoryData {
                                    singleFilterValue(title: selectionTitle, value: cityCategory.name, valueId: cityCategory.id)
                                }
                            case "City":
                                if let city = singleList as? CityData {
//                                    singleFilterValue(title: selectionTitle, value: city.name, valueId: city.id)
                                    searchSingleFilterValue(title: selectionTitle, value: city.name, valueId: city.id, searchText: searchText)
                                }
                            case "Insurance Type":
                                if let insurance = singleList as? InsuranceTypeData {
                                    singleFilterValue(title: selectionTitle, value: insurance.name, valueId: insurance.id)
                                }
                            case "Renewal Type":
                                if let renewal = singleList as? RenewalTypeData {
                                    singleFilterValue(title: selectionTitle, value: renewal.name, valueId: renewal.id)
                                }
                            case "Insurer":
                                if let insurer = singleList as? InsurerData {
                                    singleFilterValue(title: selectionTitle, value: insurer.name, valueId: insurer.id)
                                }
                            default:
                                EmptyView()
                            }
                        }
                    }
                    .padding(.top, 8)
                }
            }
            .padding(.vertical, 12)
            .overlay {
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color(hex: "#544C4C"), lineWidth: 1)
            }
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation{
                    dropDownViewSelected[selectionTitle]?.toggle()
                }
            }
            .gesture(DragGesture().onChanged { _ in })

        }
        
    }
    
    @ViewBuilder
    func searchSingleFilterValue(title: String, value: String, valueId: String, searchText: String) -> some View {
        // Check if the value matches the searchText or if searchText is empty
        if searchText.isEmpty || value.lowercased().contains(searchText.lowercased()) {
            HStack {
                Text(value)
                    .font(.custom("Poppins-Medium", size: 14))
                    .foregroundStyle(Color(hex: "#000000"))
                
                Spacer()
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)
            .background(
                selectedValue[title] == value
                    ? Color(hex: "#E3FFF6")
                    : Color.clear
            )
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation {
                    selectedValue[title] = value
                    submittingValue[title] = valueId
                    dropDownViewSelected[title] = false
                    print("\(String(describing: selectedValue[title])) : \(String(describing: submittingValue[title]))")
                }
            }
        }
    }

    
    
    private func matchesSearchCriteria(item: Any, searchText: String, selectionTitle: String) -> Bool {
        switch selectionTitle {
        case "Vehicle Type":
            return (item as? VehicleData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "Fuel Type":
            return (item as? FuelTypeData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "State":
            return (item as? StatesData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "City Category":
            return (item as? CityCategoryData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "City":
            return (item as? CityData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "Insurance Type":
            return (item as? InsuranceTypeData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "Renewal Type":
            return (item as? RenewalTypeData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "Insurer":
            return (item as? InsurerData)?.name.lowercased().contains(searchText.lowercased()) ?? false
        case "NCB":
            return (item as? String)?.lowercased().contains(searchText.lowercased()) ?? false
        default:
            return false
        }
    }
    
  
}

#Preview {
    ApplyFiltersView(accessModel: AccessServiceViewModel(), snackBar: SnackbarModel()){
        
    }
}
