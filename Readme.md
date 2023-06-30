Spring Boot - CodeDeploy on AWS

This application is a Spring Boot API that will be deployed on AWS using
Jenkins, Terraform and AWS Codedeploy.

Steps to run the Terraform Application

1. Create a VPC and EC2 instance using the Terraform code.
2. terraform init (initializes the Terraform app)
3. terraform validate (validates the syntax of the Terraform code)
4. terraform plan (creates a deployment plan)
5. terraform apply (applies the code to AWS to create the resources)
6. install-codedeploy-agent.sh shell script deploys code deploy agent
   on the EC2 instance when created (userdata script when running terraform)

Steps to Run Jenkins

1. cd to Jenkins folder and run docker-compose up (builds the Jenkins instance on Docker)
2. Start Jenkins by opening localhost:8080 and adding the password
3. Save AWS credentials in Jenkins and create a job

Codedeploy in Jenkins

1. Jenkinsfile defines the steps to run the pipeline. It is at the root of the Jenkins project
2. The Pipeline folder contains several groovy files organized in seperate
   files to keep the code readable, organized and maintainable.
3. common.groovy has the common functions such as configuring the AWS CLI to run in
   the pipeline and setting the bucket variables.
4. storage.groovy configures the bucket variables based on region and has functions
   to get the API code from the correct S3 bucket.
5. The Agent folder has a Dockerfile which will build a Maven Alpine image and install the AWS CLI
6. Environment variables set constant variables to use throughout the whole pipeline.
7. There are three parameters to set the AWS Region, Pipeline action and API deployment environment.
8. The pipeline stages check the Agent installments, set the variables based on parameters
   and run the tests, get the API code based on region and run the codedeploy AWS command based on
   region and environment
9. The pipeline Groovy code is broken into function and has several try-catch to catch
   if the function is successful. An unsuccessful function will end the pipeline
10. For prod environment it is necessary to input a change ticket and the change ticket function
    has basic validation to check if the ticket is valid.
11. The post closure will run when the pipeline finishes to clean the workspace.