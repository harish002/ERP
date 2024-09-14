import SwiftUI
import Foundation



func isNameValid(text : String) -> Bool {
    guard text.count > 2, text.count < 18 else {
        return false
    }
    
    let validate = NSPredicate(format: "SELF MATCHES %@", "^(([^ ]?)(^[a-zA-Z].*[a-zA-Z]$)([^ ]?))$")
    
    return validate.evaluate(with: text)
}

func isNumberValid(number : String) -> Bool {
    guard number.count > 9, number.count < 11 else {
        return false
    }
    let validate = NSPredicate(format: "SELF MATCHES %@", "^[0-9]+$")
    return validate.evaluate(with: number)
}

func isEmailValid(_ email: String) -> Bool {
    let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"

    let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
    return emailPred.evaluate(with: email)
}


func isValidUsername(input : String) -> Bool {
    
    guard input.count > 3 else {
        return false
    }
    
    let RegEx = "\\w{3,25}"
    let test = NSPredicate(format:"SELF MATCHES %@", RegEx)
    return test.evaluate(with: input)
}


func isPasswordValid(_ password : String) -> Bool {
    let RegEx = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$"
    let test = NSPredicate(format: "SELF MATCHES %@", RegEx)
    return test.evaluate(with: password)
}

