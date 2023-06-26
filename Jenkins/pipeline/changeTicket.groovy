String submitChangeTicket() {
    def changeTicket = input(
            message: "Would you like to deploy the app to AWS?",
            parameters: [
                    string(name: "CHANGE_TICKET", defaultValue: null, description: "Please enter a change request for deploying to Prod.")
            ])
    if (changeTicket == null) {
        throw new Exception("Please enter a valid changeTicket")
    }

    boolean isValid = validateChangeTicket(changeTicket)
    if (isValid) {
        echo "Change ticket is valid"
    } else {
        echo "Change ticket is not valid."
        throw new Exception("Change ticket is not valid.  Please request a new Change Ticket!")
    }

    return changeTicket
}

boolean validateChangeTicket(String changeTicket) {
    boolean isValid = changeTicket.startsWith("CH") && changeTicket.contains("2023")
    return isValid
}

return this