def startCodeDeploy(String bucket, String awsRegion, String cloudEnvironment) {
    def storageLib = evaluate readTrusted("Jenkins/Pipeline/storage.groovy")
    String groupName = getCodeDeployGroup(awsRegion, cloudEnvironment)
    String versionNumber = storageLib.getReleaseVersion()
    echo "Starting api deployment to $groupName"
    sh """
        aws deploy create-deployment --application-name UserServiceAPI \\
            --s3-location bucket=${bucket},key=api/${versionNumber}/user-api-${versionNumber},bundleType=zip \\
            --deployment-group-name ${groupName} --profile Default
    """
//    --deployment-config-name CodeDeployDefault.OneAtATime \\
//            --description UserAPICopeDeploy  --Profile Default
}

String deployToAllEnvironments(String region) {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    List environments = ["dev", "test", "prod"]
    for (env in environments) {
        String groupName = commonLib.getRegionShortName(region) + "-$env"
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

List<String> loopThroughCodeDeployGroups(String region) {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    List environments = ["dev", "test", "prod"]
    List<String> groups = []
    String codeDeployGroupName = "User-Service-API-DeploymentGroup-"
    for (env in environments) {
        String groupName = codeDeployGroupName + commonLib.getRegionShortName(region) + "-$env"
        groups.add(groupName)
        // TODO: add codedeploy deploy cli commads
        echo "Starting api deployment to $groupName"
    }
    return groups
}

List<String> getCodeDeployGroupsNames(String region) {
    List<String> groups
    switch (region) {
        case "us-east-1":
            groups = loopThroughCodeDeployGroups(region)
            break
        case "us-east-2":
            groups = loopThroughCodeDeployGroups(region)
            break
        default:
            throw new Exception("Invalid Region")
    }
    return groups
}

return this