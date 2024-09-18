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
    @State private var isImagePickerPresented: Bool = false
    @State private var isCameraPresented: Bool = false
    @State private var selectedImage: Image? = nil
    @State private var uiImage: UIImage? = nil
    @State private var imageData: Data? = nil
    @State private var imageName : String = ""
    @State private var vehicleNumber : String = ""

    var body: some View {
        VStack(spacing:0){
            HStack{
                Text("Vehicle Number and Image")
                    .font(.custom("Gilroy-Bold", size: 18))
            }
            .padding(.bottom,12)
            
            Divider()
            
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
                    Text(imageName)
                        .font(.custom("Gilroy-SemiBold", size: 16))
                }
               

                
                Button(action: {
                    if let tempFileURL = selectedImage?.asTemporaryFile() {
                        // Use this tempFileURL in your API request
                        print("Temporary file created at: \(tempFileURL)")
                        // Call your API upload function here with tempFileURL
                    }
                },
                label: {
                    ZStack{
                        RoundedRectangle(cornerRadius: 5)
                            .stroke(Color(hex: "#D0D0D0"),lineWidth: 1)
                            .frame(maxWidth:.infinity, maxHeight: 51 )
                            .overlay(content: {
                                RoundedRectangle(cornerRadius: 5)
                                    .foregroundStyle(Color("button_background_2"))
                  
                            })
                        
                        Text("Upload Image")

                            .font(.custom("Gilroy-SemiBold", size: 16))
                            .foregroundStyle(Color(hex: "#FFFFFF"))
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
                    .padding(.horizontal,16)
               
            }
            .padding(.vertical,12)
            
            Spacer()
        }
        .sheet(isPresented: $isImagePickerPresented) {
            PhotoPicker(imageData: $imageData, isPresented: $isImagePickerPresented, selectedImage: $selectedImage, imageName: $imageName, uiImage: $uiImage )
        }
        .fullScreenCover(isPresented: $isCameraPresented) {
            CameraView(selectedImage: $selectedImage, isPresented: $isCameraPresented, uiImage: $uiImage)
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
                    }
                }
            }
        }
    }
}

struct CameraView: UIViewControllerRepresentable {
    @Binding var selectedImage: Image?
    @Binding var isPresented: Bool
    @Binding var uiImage: UIImage?

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
            }
            parent.isPresented = false // Dismiss the camera view
        }

        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            parent.isPresented = false // Dismiss the camera view on cancel
        }
    }
}

#Preview {
    UploadVehicleImageAndNumber()
}
