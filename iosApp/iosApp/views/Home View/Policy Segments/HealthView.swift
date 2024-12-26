//
//  HealthView.swift
//  iosApp
//
//  Created by Tusmit Shah on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HealthView: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    @ObservedObject var router : Router
    
    // Filter Lists
    @State private var insuranceTypesList : [InsuranceTypeUsingSegmentIDData] = []
    @State private var renewalTypesList : [RenewalTypesBySegmentIdData] = []
    @State private var slabTypesList : [SlabData] = []
    @State private var insurerGroupsList : [InsurerGroupData] = []
    @State private var policySegmentsList : [PolicySegmentResponse] = []
    @State private var productTypeList : [ProductData] = []
    
    // Filter Objects
    @State private var insuranceType : InsuranceTypeUsingSegmentIDData?
    @State private var renewalTypes : RenewalTypeData?
    @State private var slabTypes : SlabData?
    @State private var insurerGroups : InsurerGroupData?
    @State private var policySegments : PolicySegmentResponse?
    @State private var productType : ProductData?
    
    // Values for Drop Down Menu
    @State private var dropDownViewSelected : [String : Bool] = [
        "Insurance Type" : false,
        "Renewal Type" : false,
        "Insurer Group" : false,
        "Slab" : false,
        "Insurer" : false,
        "Product" : false
    ]
    
    // Value Selected from the Filter
    @State private var selectedValue : [String : String] = [
        "Insurance Type" : "",
        "Renewal Type" : "",
        "Insurer Group" : "",
        "Slab" : "",
        "Insurer" : "",
        "Product" : ""
    ]
    
    @State private var submittingValue : [String : String] = [
        "Insurance Type" : "",
        "Renewal Type" : "",
        "Insurer Group" : "",
        "Slab" : "",
        "Insurer" : "",
        "Product" : ""
    ]
    
    @State private var isDataFiltered = false


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
                    
                    Image("health")
                    
                    Text("Health")
                        .font(.custom("Poppins-SemiBold", size: 24))
                        .foregroundStyle(Color.black)
                    
                    Spacer()
                    
                    Rectangle()
                        .frame(width: 20, height: 0)
                    
                }
                .padding(.horizontal,16)
                .padding(.vertical,16)
                
            }
            .background(
                Color(hex: "#E3FFF6")
                    .ignoresSafeArea(edges: .top) // Extend the gradient to ignore the safe area at the top
            )
            .onAppear{
                    allSlabTypes()
                    getAllInsurerGroups()
                    getAllPolicySegments()
                    getAllProductTypes()
                    getInsuranceTypeByPolicySegments(segmentId : "3b554917-a340-4682-9eac-3ffe13ec660f")
                    getRenewalTypeByPolicySegments(segmentId: "3b554917-a340-4682-9eac-3ffe13ec660f")

            }
            
            VStack(spacing:0){
                ScrollView(.vertical,showsIndicators: false){
                    VStack(spacing:16){
                        
                        selectionView(selectionTitle: "Insurance Type", staticValue: "Insurance Type")
                        
                        selectionView(selectionTitle: "Renewal Type", staticValue: "Renewal Type")
                        
                        selectionView(selectionTitle: "Slab", staticValue: "Slab Type")
                        
                        selectionView(selectionTitle: "Insurer Group", staticValue: "Insurer Group")
                        
                        
                        selectionView(selectionTitle: "Insurer", staticValue: "Insurer")
                        
                        selectionView(selectionTitle: "Product", staticValue: "Product")
                        
                    }
                    .padding([.horizontal,.vertical],16)
                    .background(
                        LinearGradient(gradient: Gradient(colors: [Color(hex: "#FFFFFF"),Color(hex: "#EBF1FF")]), startPoint: .leading, endPoint: .trailing)
                    )
                    .cornerRadius(12, corners: [.allCorners])
                    .padding([.horizontal,.vertical],16)
                }

                Button {
                    let payload = GeneralPolicyRatePayload(
                        insurer_id: submittingValue["Insurer"] ?? "", renewal_type_id: submittingValue["Renewal Type"] ?? "",
                        insurance_type_id: submittingValue["Insurance Type"] ?? "", policy_segment_id: "3b554917-a340-4682-9eac-3ffe13ec660f",
                        slab_id: submittingValue["Slab"] ?? "", product_id: submittingValue["Product"] ?? "",
                        ppt_id: "", insurer_group_id: submittingValue["Slab"] ?? "",
                        payouts: "", payins: "",
                        remarks: "", description: ""
                    )
                    let token = retrieveToken() ?? ""
                    searchGeneralPolicyRates(token: token, payload: payload)
                    if isDataFiltered {
                        withAnimation{
                            router.navigateTo(to: .heathDataView)
                        }
                    }
                }
                label : {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color(hex: "#1F2ADC"))
                        .frame(height: 44)
                        .overlay(content: {
                            Text("Submit")
                                .font(.custom("Poppins-SemiBold", size: 16))
                                .foregroundStyle(Color.white)
                            
                        })
                        .padding(.horizontal,16)
                }
            }
        }
        .frame(maxWidth:.infinity,maxHeight: .infinity,alignment: .top)
        .background(
            Color(hex: "#F5F8FF")
        )
        .navigationBarBackButtonHidden()
        
        .onReceive(accessModel.$slabTypes, perform: {slab in
            if !slab.isEmpty {
                self.slabTypesList = slab
            }
        })
        .onReceive(accessModel.$insurerGroupsData, perform: {value in
            if !value.isEmpty {
                self.insurerGroupsList = value
            }
        })
        .onReceive(accessModel.$policySegments, perform: {value in
            if !value.isEmpty {
                self.policySegmentsList = value
            }
        })
        .onReceive(accessModel.$getRenewalTypesByID, perform: {value in
            if !value.isEmpty {
                self.renewalTypesList = value
            }
        })
        .onReceive(accessModel.$productTypes, perform: {value in
            if !value.isEmpty {
                self.productTypeList = value
            }
        })
        .onReceive(accessModel.$getInsuranceTypes, perform: {value in
            if !value.isEmpty {
                self.insuranceTypesList = value
            }
        })
    }
    
    
        func getFilterList(for selectionTitle: String) -> [Any] {
            switch selectionTitle {
            case "Insurance Type" :
                return insuranceTypesList
                
            case "Renewal Type" :
                return renewalTypesList
                
            case "Insurer Group" :
                return insurerGroupsList
                
            case "Slab" :
                return slabTypesList
                
            case "Insurer" :
                return []
                
            case "Product" :
                return productTypeList
                
            default:
                return []
            }
        }
    
    
    @ViewBuilder
    func selectionView(selectionTitle : String, staticValue : String) -> some View {
        
        let filters = getFilterList(for: selectionTitle)
        
        VStack(alignment:.leading,spacing:8){
            
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
                                
                                
                                case "Insurance Type":
                                if let insurance = singleList as? InsuranceTypeUsingSegmentIDData {
                                    singleFilterValue(title: selectionTitle, value: insurance.name, valueId: insurance.id)
                                }
                                
                                case "Renewal Type":
                                if let renewal = singleList as? RenewalTypesBySegmentIdData {
                                    singleFilterValue(title: selectionTitle, value: renewal.name, valueId: renewal.id)
                                }
                                
                                case "Insurer" :
                                if let insurer = singleList as? InsurerData {
                                    singleFilterValue(title: selectionTitle, value: insurer.name, valueId: insurer.id)
                                }
                                
                                case "Slab" :
                                if let slab = singleList as? SlabData {
                                    singleFilterValue(title: selectionTitle, value: slab.name, valueId: slab.id)
                                }
                                
                                case "Insurer Group":
                                if let insurer = singleList as? InsurerGroupData {
                                    singleFilterValue(title: selectionTitle, value: insurer.name, valueId: insurer.id)
                                }
                                
                                case "Product":
                                if let product = singleList as? ProductData {
                                    singleFilterValue(title: selectionTitle, value: product.name, valueId: product.id)
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
        
        // Apis
        // Slab Types
        func allSlabTypes(){
            let token = retrieveToken() ?? ""
            Task.init {
                do
                {
                    try await accessModel.getAllSlabTypes(token: token)
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
        
        // Insurer Groups Data
        func getAllInsurerGroups(){
            let token = retrieveToken() ?? ""
            Task.init {
                do
                {
                    try await accessModel.getAllInsurerGroups(token: token)
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
        
        // Policy Segments
        func getAllPolicySegments(){
            let token = retrieveToken() ?? ""
            Task.init {
                do
                {
                    try await accessModel.getAllPolicySegments(token: token)
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
        
        // Product Types
        func getAllProductTypes(){
            let token = retrieveToken() ?? ""
            Task.init {
                do
                {
                    try await accessModel.getAllProductTypes(token: token)
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
        
        // Insurance Type Using Policy Segment ID
        func getInsuranceTypeByPolicySegments(segmentId : String){
            let token = retrieveToken() ?? ""
            Task.init {
                do
                {
                    try await accessModel.getInsuranceTypeByPolicySegments(segmentId: segmentId, token: token )
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
    
        // Renewal Type Using Policy Segment ID
        func getRenewalTypeByPolicySegments(segmentId : String){
            let token = retrieveToken() ?? ""
            Task.init {
                do
                {
                    try await accessModel.getRenewalTypesByID(segmentId: segmentId, token: token)
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

    
    
    func searchGeneralPolicyRates(token : String, payload : GeneralPolicyRatePayload){
        Task.init{
            do
            {
                let result = try await accessModel.searchGeneralPolicyRates(token: token, searchPayload: payload)

                if result {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                        snackBar.show(message: "Match Found.", title: "Success", type: .success)
                    })
                    self.isDataFiltered = result
                }
                else {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                        snackBar.show(message: "No Data Found, for the filters applied.", title: "No Data", type: .warning)
                    })
                    self.isDataFiltered = result
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

#Preview {
    HealthView(
        accessModel: AccessServiceViewModel(),
        snackBar: SnackbarModel(),
        router: Router()
    )
}
