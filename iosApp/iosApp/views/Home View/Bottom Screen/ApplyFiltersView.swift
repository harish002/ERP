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
       "Insurer" : false
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
        "Insurer" : ""
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
        "Insurer" : ""
    ]
    
    
    let vehicleTypes = ["HE", "Car", "Truck"]
    
    @State private var vehicleType : [VehicleData] = []
    @State private var fuelType : [FuelTypeData] = []
    @State private var statesData : [StatesData] = []
    @State private var cityCategories : [CityCategoryData] = []
    @State private var cityData : [CityData] = []
    @State private var insuranceTypes : [InsuranceTypeData] = []
    @State private var renewalTypes : [RenewalTypeData] = []
    @State private var insurerType : [InsurerData] = []
    
    let NCBTypes : [String] = ["Yes", "No"]
    
    var body: some View {
        VStack(alignment:.leading,spacing:0){
            HStack(spacing:0){
                Text("Filters")
                    .font(.custom("Gilroy-Bold", size: 32))
                
                Spacer()
                
                Image("close-button")
                    .contentShape(Circle())
                    .onTapGesture {
                        applyFiltersViewClosed()
                    }
            }
            .padding(.horizontal,16)
            .padding(.bottom,12)
            .padding(.top,12)
            
            ScrollView{
                VStack(alignment:.leading,spacing:0){
                    Text("Vehicle Details")
                        .font(.custom("Gilroy-Bold", size: 22))
                        .padding(.bottom,16)
                        .padding(.top,20)
                    
                    VStack(spacing:16){
                        selectionView(selectionTitle: "Vehicle Type", staticValue: "Select Vehicle Type")
                        
                        selectionView(selectionTitle: "Fuel Type", staticValue: "Select Fuel Type")
                        
                        selectionView(selectionTitle: "NCB", staticValue: "Select Status")
                    }
                    
                    Divider()
                        .padding(.vertical,20)
                    
                    Text("Location Details")
                        .font(.custom("Gilroy-Bold", size: 22))
                        .padding(.bottom,16)
                       
                    
                    VStack(spacing:16){
                        selectionView(selectionTitle: "State", staticValue: "Select State")
                        
                        selectionView(selectionTitle: "City Category", staticValue: "Select City Category")
                        
                        selectionView(selectionTitle: "City", staticValue: "Select City")
                    }
                    
                    Divider()
                        .padding(.vertical,20)
                    
                    Text("Policy Details")
                        .font(.custom("Gilroy-Bold", size: 22))
                        .padding(.bottom,16)
                    
                    VStack(spacing:16){
                        selectionView(selectionTitle: "Insurance Type", staticValue: "Select Insurance Type")
                        
                        selectionView(selectionTitle: "Renewal Type", staticValue: "Select Renewal Type")
                        
                        selectionView(selectionTitle: "Insurer", staticValue: "Select Insurer")
                    }
                    
                    
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
                            status: Int32(submittingValue["NCB"] ?? "0") ?? 0, page: 1, size: 10
                        )
                        print("Apply Filter Payload -> \(payload)")
                        let token = retrieveToken() ?? ""
                        
                        Task.init{
                            do
                            {
                                let result = try await accessModel.searchPolicyRates(token: token, searchPayload: payload)
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
                    .padding(.vertical,16)
                    
                    
                }
                .padding(.horizontal,16)
            }
        }
        .onAppear{
            let token = retrieveToken() ?? ""
            allCities(token: token)
            insuranceTypes(token : token)
            renewalTypes(token : token)
            insurerTypes(token : token)
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
        
        
        
  
    }
    
    
    @ViewBuilder
    func selectionView(selectionTitle : String, staticValue : String) -> some View {
        
        let filters = getFilterList(for: selectionTitle)
        
        VStack(alignment:.leading,spacing:8){
            Text(selectionTitle)
                .font(.custom("Gilroy-SemiBold", size: 16))
                .padding(.leading,5)
            
            VStack(alignment:.leading,spacing:0){
                HStack(spacing:0){
                    
                    if selectedValue[selectionTitle] == "" {
                        Text(staticValue)
                            .font(.custom("Gilroy-Medium", size: 14))
                    }
                    else {
                        if let value = selectedValue[selectionTitle]  {
                            Text(value)
                                .font(.custom("Gilroy-Medium", size: 14))
                        }
                       
                    }
                    
                    Spacer()
                    
                    Image("union-1")
                        .padding(.top,5)
                }
                
                
                if dropDownViewSelected[selectionTitle] ?? false {
                    VStack(alignment:.leading,spacing:16){
                        HStack{
                            Text(staticValue)
                                .font(.custom("Gilroy-Medium", size: 14))
                           
                            Spacer()
                            
                            if selectedValue[selectionTitle] == "" {
                                Circle()
                                    .fill(Color(hex: "#3960F6"))
                                    .frame(width: 9, height: 9)
                                    .overlay(content: {
                                        Circle()
                                            .stroke(Color(hex: "#3960F6"),lineWidth: 1)
                                            .frame(width: 16,height: 16)
                                    })
                                    
                            }
                        }
                        .padding(.top,8)
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
                                
                                default :
                                    EmptyView()
                            }
                            
                            
                            
                            
                        }
                        
                    }
                    .padding(.top,8)
                    
                }
                
            }
            .padding(.horizontal,16)
            .padding(.vertical,16)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color(hex: "#949494"),lineWidth: 1.2)
                    .shadow(color: Color(hex: "#000004"), radius: 0.3)
            )
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
                .font(.custom("Gilroy-Medium", size: 14))
            
            Spacer()
            
            
            if selectedValue[title] == value {
                Circle()
                    .fill(Color(hex: "#3960F6"))
                    .frame(width: 9, height: 9)
                    .overlay(content: {
                        Circle()
                            .stroke(Color(hex: "#3960F6"),lineWidth: 1)
                            .frame(width: 16,height: 16)
                    })
            }
        }
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
        default:
            return []
        }
    }
    
    func insuranceTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllInsuranceTypes(token: token)
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
                snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
            }
        }
    }
    
    func renewalTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllRenewalTypes(token: token)
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
                snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
            }
        }
    }
    
    func insurerTypes(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllInsurerTypes(token: token)
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
                snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
            }
        }
    }
    
    func allCities(token : String){
        Task.init {
            do
            {
                try await accessModel.getAllCitiesData(token: token)
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
                snackBar.show(message: "Ooops..Something went wrong, try one more time.", title: "Error", type: .error)
            }
        }
    }
}

#Preview {
    ApplyFiltersView(accessModel: AccessServiceViewModel(), snackBar: SnackbarModel()){
        
    }
}
