//
//  NotificationService.swift
//  NotificationServiceExtension
//
//  Created by Tusmit Shah on 26/09/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import UserNotifications

class NotificationService: UNNotificationServiceExtension {

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
        
        guard let bestAttemptContent = bestAttemptContent else {
            contentHandler(request.content)
            return
        }

        // Check if there's an image URL in the payload
        if let imageURLString = request.content.userInfo["image"] as? String, let imageURL = URL(string: imageURLString) {
            downloadImage(from: imageURL) { attachment in
                if let attachment = attachment {
                    bestAttemptContent.attachments = [attachment]
                }
                // Call the content handler after processing the notification
                contentHandler(bestAttemptContent)
            }
        } else {
            // No image in the payload, proceed as usual
            contentHandler(bestAttemptContent)
        }
    }

    override func serviceExtensionTimeWillExpire() {
        if let contentHandler = contentHandler, let bestAttemptContent = bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }

    private func downloadImage(from url: URL, completion: @escaping (UNNotificationAttachment?) -> Void) {
        URLSession.shared.downloadTask(with: url) { (location, response, error) in
            guard let location = location else {
                completion(nil)
                return
            }
            
            let tempDirectory = FileManager.default.temporaryDirectory
            let tempFilePath = tempDirectory.appendingPathComponent(url.lastPathComponent)
            
            do {
                try FileManager.default.moveItem(at: location, to: tempFilePath)
                let attachment = try UNNotificationAttachment(identifier: "", url: tempFilePath, options: nil)
                completion(attachment)
            } catch {
                print("Error creating attachment: \(error.localizedDescription)")
                completion(nil)
            }
        }.resume()
    }
}
