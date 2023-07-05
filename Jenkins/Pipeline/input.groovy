boolean getInput(String message, String description) {
    boolean inputMessage = input(
            message: message,
            parameters: [
                    booleanParam(defaultValue: true, description: description)
            ])
    return inputMessage
}

return this