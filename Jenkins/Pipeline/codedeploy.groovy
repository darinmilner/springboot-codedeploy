def startCodeDeploy(String bucket, String awsRegion, String cloudEnvironment) {
    def storageLib = evaluate readTrusted("Jenkins/Pipeline/storage.groovy")
    String groupName = getCodeDeployGroup(awsRegion, cloudEnvironment)
    String versionNumber = storageLib.getReleaseVersion()
    echo "Starting api deployment to $groupName"
    sh """
        aws deploy create-deployment --application-name User-Service-API \\
            --s3-location bucket=${bucket},key=api/${versionNumber}/user-api-${versionNumber},bundleType=zip \\
            --deployment-group-name ${groupName} --profile Default
    """
}

String deployToAllEnvironments(String awsRegion) {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    List environments = ["dev", "test", "prod"]
    for (env in environments) {
        String groupName = commonLib.getRegionShortName(awsRegion) + "-$env"
        println groupName
        echo "Going to deploy to $env"
    }
}

String getCodeDeployGroup(String awsRegion, String cloudEnvironment) {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    String codeDeployGroupName = "User-Service-API-DeploymentGroup-"
    switch (awsRegion) {
        case "us-east-1":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion) + "-$cloudEnvironment"
            return codeDeployGroupName
        case "us-east-2":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion) + "-$cloudEnvironment"
            return codeDeployGroupName
        case "us-west-1":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion) + "-$cloudEnvironment"
            return codeDeployGroupName
        case "ap-southeast-1":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion) + "-$cloudEnvironment"
            return codeDeployGroupName
        default:
            return new Exception("Invalid or unsupported region $awsRegion")
    }
}

List<String> loopThroughCodeDeployGroups(String awsRegion) {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    List environments = ["dev", "test", "prod"]
    List<String> groups = []
    String codeDeployGroupName = "User-Service-API-DeploymentGroup-"
    for (env in environments) {
        String groupName = codeDeployGroupName + commonLib.getRegionShortName(awsRegion) + "-$env"
        groups.add(groupName)
        // TODO: add codedeploy deploy cli commads
        echo "Starting api deployment to $groupName"
    }
    return groups
}

List<String> getCodeDeployGroupsNames(String awsRegion) {
    List<String> groups
    switch (awsRegion) {
        case "us-east-1":
            groups = loopThroughCodeDeployGroups(awsRegion)
            break
        case "us-east-2":
            groups = loopThroughCodeDeployGroups(awsRegion)
            break
        case "us-west-1":
            groups = loopThroughCodeDeployGroups(awsRegion)
            break
        default:
            throw new Exception("Invalid Region")
    }
    return groups
}

return this