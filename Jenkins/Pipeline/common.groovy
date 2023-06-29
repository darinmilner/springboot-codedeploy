String getRegionShortName(String awsRegion) {
    return awsRegion.replace("-", "")
}

String getBucketName(String awsRegion) {
    String bucketName = env.BUCKET_PREFIX
    switch (awsRegion) {
        case "us-west-1":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        case "us-east-1":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        case "us-east-2":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        case "ap-southeast-3":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

String getDynamoDBStateTableName(String awsRegion) {
    String tableName = env.DYNAMO_TABLE_PREFIX
    switch (awsRegion) {
        case "us-west-1":
            tableName += getRegionShortName(awsRegion)
            return tableName
        case "us-east-1":
            tableName += getRegionShortName(awsRegion)
            return tableName
        case "us-east-2":
            tableName += getRegionShortName(awsRegion)
            return tableName
        case "ap-southeast-3":
            tableName += getRegionShortName(awsRegion)
            return tableName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

def configureAWSProfile(String awsRegion) {
    echo "Configuring AWS Profile"

    withCredentials([usernamePassword(credentialsId: "amazon", usernameVariable: "ACCESSKEY", passwordVariable: "SECRETKEY")]) {
        sh 'aws configure set aws_access_key_id $ACCESSKEY --profile Default'
        sh 'aws configure set aws_secret_access_key $SECRETKEY --profile Default'
    }

    try {
        sh """
            aws configure set region ${awsRegion} --profile Default
            aws configure set output "json" --profile Default
        """

    } catch (Exception err) {
        echo "Error configuring AWS Profile $err"
        throw err
    }
}

return this