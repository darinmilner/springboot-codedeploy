def getAndUploadLatestEnvFileToS3(String awsRegion, String bucketName) {
    withCredentials([usernamePassword(credentialsId: "amazon", usernameVariable: "ACCESSKEY", passwordVariable: "SECRETKEY")]) {
        String fileName = sh(script: """
            #!/bin/bash
            cd Jenkins/Scripts/
            mkdir -p envfiles/
            python3 get_file.py $ACCESSKEY $SECRETKEY $awsRegion $bucketName
            cd envfiles/
            ls -la
        """, returnStdout: true
        )
        return fileName
    }
}

String configureAWSRegion(String awsRegion) {
    withCredentials([usernamePassword(credentialsId: "amazon", usernameVariable: "ACCESSKEY", passwordVariable: "SECRETKEY")]) {
        sh(script: """
          aws configure set region ${awsRegion}
          aws configure set aws_access_key_id $ACCESSKEY
          aws configure set aws_secret_access_key $SECRETKEY
       """, returnStdout: true
        )
    }
}

def getAPIEnvFile(String bucketName, String filePath) {
    try {
        echo "Getting application file $filePath from s3 bucket $bucketName"
        sh """
            aws s3 cp s3://${bucketName}/${filePath} src/resources/application-prod.yaml --profile Default
            cat src/resources/application-prod.yaml
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting $err!"
        errorLib.throwError(err, "Error getting file from s3 bucket $err")
    }
}

def copyEnvFileToRegionalS3Bucket(String bucketName, String awsRegion, String fileCommand) {
    try {
        echo "Pushing API code to $bucketName"
        withCredentials([usernamePassword(credentialsId: "amazon", usernameVariable: "ACCESSKEY", passwordVariable: "SECRETKEY")]) {
            sh """
                aws configure set region ${awsRegion} 
                aws configure set aws_access_key_id $ACCESSKEY 
                aws configure set aws_secret_access_key $SECRETKEY  
                aws s3 cp src/resources/application-prod.yaml s3://${bucketName}/${fileCommand}/  --profile Default
            """
        }
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Error pushing code to S3 bucket $err")
    }
}

def zipAndPushAPIToS3(String bucketName) {
    String versionNumber = getReleaseVersion()
    String zipFileName = "user-api-${versionNumber}"
    try {
        echo "Pushing API code to $bucketName"
        sh """
            zip -r ${zipFileName}.zip src/
            aws s3 cp ${env.WORKSPACE}/${zipFileName}.zip s3://${bucketName}/api/${versionNumber}/  --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Error pushing code to S3 bucket $err")
    }
}

String getReleaseVersion() {
    def gitCommit = sh(returnStdout: true, script: "git rev-parse HEAD").trim()
    String versionNumber
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER
    } else {
        versionNumber = gitCommit.take(8)
    }

    println "Version number is $versionNumber"
    return versionNumber
}

return this