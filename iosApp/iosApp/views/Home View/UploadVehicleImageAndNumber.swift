//
//  UploadVehicleImageAndNumber.swift
//  iosApp
//
//  Created by Tusmit Shah on 18/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import PhotosUI
import AVFoundation
import shared

extension Image {
    @MainActor func asTemporaryFile() -> URL? {
        guard let uiImage = convertToUIImage() else { return nil }
        
        // Create a temporary file URL
        let tempDirectory = URL(fileURLWithPath: NSTemporaryDirectory(), isDirectory: true)
        let fileURL = tempDirectory.appendingPathComponent(UUID().uuidString + ".jpg")
        
        do {
            // Convert UIImage to Data (JPEG format)
            guard let imageData = uiImage.jpegData(compressionQuality: 0.8) else { return nil }
            
            // Write the data to the file
            try imageData.write(to: fileURL)
            return fileURL // Return the file URL for use in your API request
        } catch {
            print("Error writing image data to file: \(error)")
            return nil
        }
    }
    
    @MainActor private func convertToUIImage() -> UIImage? {
        let renderer = ImageRenderer(content: self)
        return renderer.uiImage
    }
}


struct UploadVehicleImageAndNumber: View {
    
    @ObservedObject var accessModel : AccessServiceViewModel
    @ObservedObject var snackBar : SnackbarModel
    
    @State private var isImagePickerPresented: Bool = false
    @State private var isCameraPresented: Bool = false
    @State private var selectedImage: Image? = nil
    @State private var uiImage: UIImage? = nil
    @State private var imageData: Data? = nil
    @State private var imageURL : URL? = nil
    @State private var imageName : String = ""
    @State private var vehicleNumber : String = ""
    @State private var filePath : String? = nil
    
    @State private var loader = false
    @State private var vehicleNumberLoader = false
    @State private var policyRateLoader = false
    
    @State private var getVehicleDetails : GetVehicleDetails?
    
    @State private var vehicleType : VehicleData?
    @State private var fuelType : FuelTypeData?
    @State private var statesData : StatesData?
    @State private var cityCategories : CityCategoryData?
    @State private var cityData : CityData?
    @State private var insuranceTypes : InsuranceTypeData?
    @State private var renewalTypes : RenewalTypeData?
    @State private var insurerType : InsurerData?
    
    @State private var showAllPolicyRates : [PolicyRateData] = []
    @State private var policyRateId : String = ""
    @State private var isPolicyRateSelected = false
    

    var body: some View {
        ZStack{
            VStack(spacing:0){
                HStack{
                    Text("Vehicle Number and Image")
                        .font(.custom("Gilroy-Bold", size: 18))
                }
                .padding(.bottom,12)
                
                Divider()
                
                ScrollView{
                    VStack(spacing:12){
                        
                        Text("Upload Vehicle Image")
                            .font(.custom("Gilroy-SemiBold", size: 22))
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                        
                        if let selectedImage = selectedImage {
                            selectedImage
                                .resizable()
                                .scaledToFit()
                                .frame(width: 200, height: 200)
                        } else {
                            Rectangle()
                                .frame(width: 200, height: 200)
                                .foregroundStyle(Color.gray.opacity(0.3))
                                .overlay(content: {
                                    Image(systemName: "camera.aperture")
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(width: 24,height: 24)
                                })
                        }
                        
                        Button("Select Image") {
                            requestPhotoLibraryAccess()
                        }
                        
                        Text("OR")
                        
                        Button("Capture Image Using Camera") {
                            requestCameraAccess()
                        }
                        
                        if imageName.isEmpty {
                            Text("No image selected")
                        } else {
                            Text("File Selected: \(imageName)")
                                .font(.custom("Gilroy-SemiBold", size: 14))
                                .lineLimit(1)
                        }
                        
                        
                        
                        Button(action: {
                            withAnimation{
                                self.loader = true
                            }
                            if let filePath = filePath {
                                let token = retrieveToken() ?? ""
                                
                                // Use this filePath in your API request
                                print("File path for upload: \(filePath)")
                                
                                // Call your API upload function here with filePath
                                Task.init {
                                    accessModel.uploadImage(token: token, filePath: filePath) { result in
                                        if let message = result {
                                            if !message.isEmpty{
                                                withAnimation{
                                                    self.loader = false
                                                    vehicleNumber = message
                                                }
                                                snackBar.show(message: "Data Fetched Successfully.", title: "Success", type: .success)
                                            }
                                        } else {
                                            snackBar.show(message: "Failed to upload image.", title: "Error", type: .error)
                                        }
                                    }
                                }
                            } else {
                                print("No file path available.")
                                snackBar.show(message: "No file selected.", title: "Error", type: .error)
                            }
                            
                        },
                               label: {
                            ZStack{
                                RoundedRectangle(cornerRadius: 5)
                                    .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                                    .frame(maxWidth: .infinity, minHeight: 45 )
                                    .overlay(content: {
                                        RoundedRectangle(cornerRadius: 5)
                                            .foregroundStyle(Color("button_background_2"))
                                        
                                    })
                                if loader {
                                    ProgressView()
                                        .tint(.white)
                                }
                                else {
                                    Text("Upload Image")
                                        .font(.custom("Gilroy-SemiBold", size: 16))
                                        .foregroundStyle(Color(hex: "#FFFFFF"))
                                }
                            }
                            .padding(.horizontal,16)
                            
                        })
                        
                    }
                    .padding(.vertical,12)
                    
                    VStack{
                        Text("Enter Vehicle Number")
                            .font(.custom("Gilroy-SemiBold", size: 22))
                            .foregroundStyle(Color(hex: "#4E4E4E"))
                        
                        TextField("Vehicle Number", text: $vehicleNumber)
                            .padding()
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 5)
                                    .stroke(.black, lineWidth: 1)
                                
                            })
                            .overlay(alignment: .trailing, content: {
                                Button(action: {
                                    withAnimation{
                                        self.vehicleNumberLoader = true
                                    }
                                    let token = retrieveToken() ?? ""
                                    Task.init{
                                        let result = try await accessModel.getVehicleDetails(token: token, number: vehicleNumber)
                                        
                                        if result.status == "success"{
                                            self.vehicleNumberLoader = false
                                            snackBar.show(message: "Vehicle number submitted successfully.", title: "Success", type: .success)
                                            
                                            self.policyRateLoader = true
                                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                                let payload = SearchPolicyRatePayload(
                                                    state_id: statesData?.id ?? "",
                                                    city_id: cityData?.id ?? "",
                                                    city_category_id: cityCategories?.id ?? "",
                                                    vehicle_type_id: vehicleType?.id ?? "",
                                                    vehicle_model_id: "",
                                                    renewal_type_id: renewalTypes?.id ?? "",
                                                    insurance_type_id: "",
                                                    insurer_id: insurerType?.id ?? "",
                                                    fuel_type_id:fuelType?.id  ?? "",
                                                    status: "0", page: 1, size: 50
                                                )
                                                print("Vehicle Number Filter Payload -> \(payload)")
                                                getPolicyRatesList(token: token, payload: payload)
                                            }
                                        }
                                        else {
                                            self.vehicleNumberLoader = true
                                            snackBar.show(message: result.message, title: "Error", type: .error)
                                        }
                                    }
                                },
                                       label: {
                                    ZStack{
                                        RoundedRectangle(cornerRadius: 5)
                                            .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                                            .frame(width: 100, height: 45 )
                                            .overlay(content: {
                                                RoundedRectangle(cornerRadius: 5)
                                                    .foregroundStyle(Color("button_background_2"))
                                                
                                            })
                                        if vehicleNumberLoader {
                                            ProgressView()
                                                .tint(.white)
                                        }
                                        else {
                                            Text("Submit")
                                                .font(.custom("Gilroy-SemiBold", size: 16))
                                                .foregroundStyle(Color(hex: "#FFFFFF"))
                                        }
                                    }
                                    .padding(.trailing,5)
                                    
                                })
                            })
                            .padding(.horizontal,16)
                        
                        if !vehicleNumber.isEmpty {
                            Text("Please verify the vehicle number before continuing")
                                .font(.custom("Gilroy-SemiBold", size: 16))
                                .foregroundStyle(Color.indigo)
                                .padding(.top,8)
                        }
                        
                        Divider()
                            .padding(.vertical,12)
                        
                        
                        VStack{
                            
                            
                            if policyRateLoader {
                                ProgressView()
                                    .padding(.top,20)
                            }
                            else {
                                if !showAllPolicyRates.isEmpty {
                                    ForEach(showAllPolicyRates, id: \.self){policyRate in
                                        
                                        HStack(alignment:.top,spacing:12){
                                            Circle()
                                                .foregroundStyle(Color(hex: "#D9D9D9"))
                                                .frame(width: 42,height: 42)
                                                .overlay(content: {
                                                    Image(systemName: "doc")
                                                        .resizable()
                                                        .aspectRatio(contentMode: .fit)
                                                        .frame(width: 16, height: 16)
                                                    
                                                })
                                            
                                            HStack(alignment:.top,spacing:16){
                                                VStack(alignment:.leading,spacing:4){
                                                    Text("PAYOUT %")
                                                        .font(.custom("Gilroy-Medium", size: 12))
                                                    
                                                    Text(policyRate.payouts)
                                                        .font(.custom("Gilroy-Bold", size: 14))
                                                }
                                                
                                                
                                                VStack(alignment:.leading,spacing:4){
                                                    Text("INSURER")
                                                        .font(.custom("Gilroy-Medium", size: 12))
                                                    
                                                    Text(policyRate.insurer.name)
                                                        .font(.custom("Gilroy-Bold", size: 14))
                                                }
                                                
                                                VStack(alignment:.leading,spacing:4){
                                                    Text("INSURANCE TYPE")
                                                        .font(.custom("Gilroy-Medium", size: 12))
                                                    
                                                    Text(policyRate.insurance_type.name)
                                                        .font(.custom("Gilroy-Bold", size: 14))
                                                }
                                                
                                                
                                            }
                                            .offset(y:2)
                                            
                                            Spacer()
                                            
                                            Image(systemName: "arrowshape.right.fill")
                                        }
                                        .padding(.vertical,12)
                                        .padding(.horizontal,16)
                                        .contentShape(Rectangle())
                                        .onTapGesture {
                                            print("Policy Rate Selected")
                                            withAnimation{
                                                isPolicyRateSelected = true
                                                self.policyRateId = policyRate.id
                                            }
                                        }
                                        
                                        
                                        Divider()
                                    }
                                }
                                else {
                                    Text("No Data Available..")
                                        .font(.custom("Gilroy-SemiBold", size: 20))
                                        .padding(.top,12)
                                }
                            }
                        }
                    }
                    
                }
            }
            
            if isPolicyRateSelected {
                PolicyRateDetailView(accessModel: accessModel,snackBar: snackBar, policyRateId: policyRateId){
                    withAnimation{
                        isPolicyRateSelected = false
                    }
                }
                .zIndex(1)
            }
        }
        .sheet(isPresented: $isImagePickerPresented) {
            PhotoPicker(imageData: $imageData, isPresented: $isImagePickerPresented, selectedImage: $selectedImage, imageName: $imageName, uiImage: $uiImage, imageURL: $imageURL, filePath: $filePath )
        }
        .fullScreenCover(isPresented: $isCameraPresented) {
            CameraView(selectedImage: $selectedImage, isPresented: $isCameraPresented, uiImage: $uiImage, imageName: $imageName, imageURL: $imageURL, filePath: $filePath)
        }
        .onReceive(accessModel.$getVehicleDetails, perform: {details in
            if let detail = details {
                self.getVehicleDetails = detail
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 1){
                statesData(accessModel : accessModel)
                fuelType(accessModel: accessModel)
                insurerType(accessModel: accessModel)
                vehicleType(accessModel: accessModel)
                cities(accessModel: accessModel)
                cityCategories(accessModel: accessModel)
                renewalType(accessModel: accessModel)
            }
            
        })
       
    }
    
    func statesData(accessModel : AccessServiceViewModel){
        let state = accessModel.getAllStatesData.filter{state in
            state.name.lowercased() == getVehicleDetails?.result?.state_code
        }
        self.statesData = state.first
    }
    
    func fuelType(accessModel : AccessServiceViewModel){
        let fuel = accessModel.fuelTypes.filter{fuel in
            fuelType?.name.lowercased() == getVehicleDetails?.result?.fuel_descr.lowercased()
        }
        self.fuelType = fuel.first
    }
    
    func insurerType(accessModel : AccessServiceViewModel){
        let insurer = accessModel.insurerTypes.filter{data in
            data.name.lowercased() == getVehicleDetails?.result?.vehicle_insurance_details.insurance_company_name.lowercased()
        }
        self.insurerType = insurer.first
    }
    
    func vehicleType(accessModel : AccessServiceViewModel){
        let vehicle = accessModel.vehicleTypes.filter{data in
            data.name.lowercased() == getVehicleDetails?.result?.vehicle_type.lowercased()
        }
        self.vehicleType = vehicle.first
    }
    
    func cities(accessModel : AccessServiceViewModel){
        let city = accessModel.getAllCites.filter{data in
            data.name.lowercased() == getVehicleDetails?.result?.permanent_district_name.lowercased()
        }
        self.cityData = city.first
    }
    
    func cityCategories(accessModel : AccessServiceViewModel){
        let cityCategory = accessModel.getAllCityCategories.filter{category in
            category.id.lowercased() == cityData?.city_category_id.lowercased()
        }
        self.cityCategories = cityCategory.first
    }
    
    func renewalType(accessModel : AccessServiceViewModel){
        let insuranceDetails = getVehicleDetails?.result?.vehicle_insurance_details
        var renewalTypeName : String = ""
        if let details = insuranceDetails {
            guard let insuranceStarteDateString = insuranceDetails?.insurance_from else {
                return
            }
            
            // Create a date formatter to parse the string into a Date object
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            
            // Convert the insurance start date string to a Date object
            if let insuranceStartDate = dateFormatter.date(from: insuranceStarteDateString) {
                let currentDate = Date()
                
                // Calculate the difference in days
                let timeDiff = currentDate.timeIntervalSince(insuranceStartDate)
                let daysDiff = timeDiff / (60 * 60 * 24)
                
                if daysDiff < 30 {
                    renewalTypeName = "Rollover"
                }
                else if daysDiff >= 30 && daysDiff < 365 {
                    renewalTypeName = "New"
                }
                else {
                    renewalTypeName = "Brand New"
                }
                
                let matchedRenewalType = accessModel.renewalTypes.filter{renew in
                    renew.name.lowercased() == renewalTypeName.lowercased()
                }
                self.renewalTypes = matchedRenewalType.first
            }
            
        }
    }
    
    // Get Policy Rates
    func getPolicyRatesList(token : String, payload : SearchPolicyRatePayload){
        
        Task.init{
            do
            {
                let (result,response) = try await accessModel.searchPolicyRates(token: token, searchPayload: payload)
                if !result {
                    self.policyRateLoader = false
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                        snackBar.show(message: "No Data Found, for the filters applied.", title: "No Data", type: .warning)
                    })
                }
                else {
                    self.policyRateLoader = false
                    self.showAllPolicyRates = response
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                        snackBar.show(message: "Match Found.", title: "Success", type: .success)
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
    
 

    private func requestPhotoLibraryAccess() {
        let status = PHPhotoLibrary.authorizationStatus()
        if status == .authorized {
            isImagePickerPresented.toggle()
        } else if status == .denied || status == .restricted {
            // Handle the case where access is denied or restricted
            print("Access to photo library denied.")
        } else {
            PHPhotoLibrary.requestAuthorization { newStatus in
                if newStatus == .authorized {
                    DispatchQueue.main.async {
                        self.isImagePickerPresented.toggle()
                    }
                }
            }
        }
    }
    
    private func requestCameraAccess() {
            let status = AVCaptureDevice.authorizationStatus(for: .video)
            if status == .authorized {
                isCameraPresented.toggle()
            } else if status == .denied || status == .restricted {
                print("Access to camera denied.")
            } else {
                AVCaptureDevice.requestAccess(for: .video) { granted in
                    if granted {
                        DispatchQueue.main.async {
                            self.isCameraPresented.toggle()
                        }
                    }
                }
            }
    }
}

struct PhotoPicker: UIViewControllerRepresentable {
    @Binding var imageData: Data?
    @Binding var isPresented: Bool
    @Binding var selectedImage : Image?
    @Binding var imageName : String
    @Binding var uiImage: UIImage?
    @Binding var imageURL: URL? // To store the file URL
    @Binding var filePath: String? // To store the file path

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIViewController(context: Context) -> PHPickerViewController {
        var configuration = PHPickerConfiguration(photoLibrary: .shared())
        configuration.selectionLimit = 1
        configuration.filter = .images

        let picker = PHPickerViewController(configuration: configuration)
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: PHPickerViewController, context: Context) {}

    class Coordinator: NSObject, PHPickerViewControllerDelegate {
        let parent: PhotoPicker

        init(_ parent: PhotoPicker) {
            self.parent = parent
        }

        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
            picker.dismiss(animated: true)
            
            guard let result = results.first else { return }
            
            result.itemProvider.loadObject(ofClass: UIImage.self) { object, error in
                if let image = object as? UIImage {
                    DispatchQueue.main.async {
                        self.parent.uiImage = image
                        self.parent.selectedImage = Image(uiImage: image)
                        self.parent.imageData = image.pngData() // or use jpegData(compressionQuality:) for JPEG
                        self.parent.imageName = result.itemProvider.suggestedName ?? "Unknown Image"
                        // Save the image to the app's sandbox directory
                        self.saveImageToSandbox(image: image)
                        
                    }
                }
            }
            
            // Load the file URL if available
            if let assetIdentifier = result.assetIdentifier {
                let options = PHContentEditingInputRequestOptions()
                options.canHandleAdjustmentData = { _ in return false }
                
                let fetchResult = PHAsset.fetchAssets(withLocalIdentifiers: [assetIdentifier], options: nil)
                if let asset = fetchResult.firstObject {
                    asset.requestContentEditingInput(with: options) { (contentEditingInput, info) in
                        if let url = contentEditingInput?.fullSizeImageURL {
                            DispatchQueue.main.async {
                                self.parent.imageURL = url // Store the file URL
                                print("Captured image URL: \(url)")
                            }
                        } else {
                            print("No image URL found.")
                        }
                    }
                }
            }
            
        }
        
        
                // Function to save image to the app's sandbox directory
                func saveImageToSandbox(image: UIImage) {
                    guard let data = image.pngData() else { return }
                    
                    // Get the app's temporary directory
                    let tempDirectory = FileManager.default.temporaryDirectory
                    let fileName = UUID().uuidString + ".png" // You can generate a unique name for the image
                    let fileURL = tempDirectory.appendingPathComponent(fileName)

                    do {
                        // Write the image data to the file
                        try data.write(to: fileURL)
                        
                        DispatchQueue.main.async {
                            self.parent.filePath = fileURL.path // Store the file path for API usage
                            print("Saved image to sandbox at: \(fileURL.path)")
                        }
                    } catch {
                        print("Error saving image to sandbox: \(error.localizedDescription)")
                    }
                }
    }
}

struct CameraView: UIViewControllerRepresentable {
    @Binding var selectedImage: Image?
    @Binding var isPresented: Bool
    @Binding var uiImage: UIImage?
    @Binding var imageName : String
    @Binding var imageURL: URL? // To store the file URL
    @Binding var filePath: String? // To store the file path

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        picker.sourceType = .camera // Set the source type to camera
        return picker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}

    class Coordinator: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
        let parent: CameraView

        init(_ parent: CameraView) {
            self.parent = parent
        }

        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            if let uiImage = info[.originalImage] as? UIImage {
                self.parent.uiImage = uiImage
                parent.selectedImage = Image(uiImage: uiImage) // Convert UIImage to SwiftUI Image
                
                // Save the image and get its file name
                let fileName = self.saveImageToDocuments(image: uiImage)
                self.parent.imageName = fileName // Store the file name
              
                
            }
            parent.isPresented = false // Dismiss the camera view
        }

        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            parent.isPresented = false // Dismiss the camera view on cancel
        }
        
        private func saveImageToDocuments(image: UIImage) -> String {
                    // Get the documents directory URL
                    let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
                    
                    // Create a unique file name
                    let fileName = UUID().uuidString + ".png" // You can change the extension if needed
                    let fileURL = documentsDirectory.appendingPathComponent(fileName)

                    // Convert UIImage to JPEG data and write it to disk
                    if let data = image.jpegData(compressionQuality: 1.0) {
                        do {
                            try data.write(to: fileURL)
                            DispatchQueue.main.async {
                                self.parent.filePath = fileURL.path // Store the file path for API usage
                                print("Saved image to sandbox at: \(fileURL.path)")
                            }
                            return fileName // Return just the filename
                        } catch {
                            print("Error saving image to disk: \(error)")
                        }
                    }
                    return "unknown.jpg" // Default filename in case of failure
            }
    }
}

#Preview {
    UploadVehicleImageAndNumber(accessModel: AccessServiceViewModel(), snackBar: SnackbarModel())
}
