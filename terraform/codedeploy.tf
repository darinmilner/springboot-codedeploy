resource "aws_codedeploy_app" "user-service" {
  name = "User-Service-API"
}

resource "aws_codedeploy_deployment_group" "user-service-deployment-group" {
  app_name              = aws_codedeploy_app.user-service.name
  deployment_group_name = "User-Service-API-DeploymentGroup-${local.region}-${var.system-environment}"
  service_role_arn      = aws_iam_role.codedeploy-service.arn

  deployment_config_name = "CodeDeployDefault.OneAtATime"

  ec2_tag_filter {
    key   = "Name"
    type  = "KEY_AND_VALUE"
    value = "${local.name-prefix}-API-Server"
  }

  # triggers a rollback on deployment failure
  auto_rollback_configuration {
    enabled = true
    events  = [
      "DEPLOYMENT_FAILURE"
    ]
  }
}