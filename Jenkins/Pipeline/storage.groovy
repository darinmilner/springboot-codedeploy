def getAPIEnvFileFromUSEast1Bucket(String awsRegion) {
    try {
        echo "Getting application file from us-east-1 s3 bucket"
        sh """
            aws configure set region us-east-1 --profile Default
            aws s3 cp s3://taskapi-storage-bucket-useast1/application-prod.yaml src/resources/application-prod.yaml --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting $err!"
        errorLib.throwError(err, "Error getting file from us-east-1 s3 bucket $err")
    }

    if (awsRegion != "us-east-1") {
        String region = awsRegion.replace("-", "")
        copyEnvFileToRegionalS3Bucket("taskapi-storage-bucket-${region}", awsRegion)
    }
}

def getAPIEnvFile(String bucketName) {
    try {
        echo "Getting application file from s3 bucket $bucketName"
        sh """
            aws s3 cp s3://${bucketName}/envfiles/ src/resources/application-prod.yaml --profile Default
            cat src/resources/application-prod.yaml
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting $err!"
        errorLib.throwError(err, "Error getting file from s3 bucket $err")
    }
}

def copyEnvFileToRegionalS3Bucket(String bucketName, String awsRegion) {
    try {
        echo "Pushing API code to $bucketName"
        sh """
            aws configure set region ${awsRegion} --profile Default
            file = \$(aws s3 ls  s3://$bucketName/envfiles/ --recursive | sort | tail -n 1 | awk '{print \$4}' --profile Default) 
            echo file 
           # aws s3 cp src/resources/application-prod.yaml s3://${bucketName}/envfiles/application-prod.yaml  --profile Default
        """
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
            ls -la
            aws s3 cp ${WORKSPACE}/${zipFileName}.zip s3://${bucketName}/api/${versionNumber}/  --profile Default
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