def runTests() {
    echo "This is where tests should run but tests NEED to be written first!!!"
}

def buildAPI() {
    sh "./mvnw spring-boot:run"
}

return this