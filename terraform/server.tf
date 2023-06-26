resource "aws_vpc" "main" {
  cidr_block           = var.vpc-cidr
  enable_dns_hostnames = true
  enable_dns_support   = true
  instance_tenancy     = "default"

  tags = {
    Name = "${var.system-environment}MainVPC"
  }
}

resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id

  cidr_block              = var.cidr-block
  availability_zone       = local.zone1
  map_public_ip_on_launch = true
}

resource "aws_subnet" "private" {
  vpc_id = aws_vpc.main.id

  cidr_block              = var.cidr-block
  availability_zone       = local.zone2
  map_public_ip_on_launch = false
}

resource "aws_vpc_endpoint" "s3-endpoint" {
  service_name    = "com.amazonaws.${var.aws-region}.s3"
  route_table_ids = [aws_route_table.private-route.id]
  vpc_id          = aws_vpc.main.id
}

resource "aws_vpc_endpoint" "dynamodb-endpoint" {
  service_name       = "com.amazonaws.${var.aws-region}.dynamodb"
  route_table_ids    = [aws_route_table.private-route.id]
  security_group_ids = []
  vpc_id             = aws_vpc.main.id
}


resource "aws_instance" "server" {
  ami                    = var.amis[var.aws-region]
  instance_type          = "t2.micro"
  key_name               = "terraform"
  vpc_security_group_ids = [aws_security_group.server-sg.id]
  availability_zone      = local.zone1
  subnet_id              = aws_subnet.public.id
  iam_instance_profile   = aws_iam_instance_profile.ec2-instance-profile.arn

  user_data = file("${path.module}/install-codedeploy-agent.sh")

  tags = {
    Name    = "${local.name-prefix}-API-Server"
    Project = var.project
  }
}

#creates cloudwatch loggroup and subscription stream
resource "aws_cloudwatch_log_group" "webserverlogs" {
  name              = format("%s-webserverlogs", local.name-prefix)
  retention_in_days = 7
}

resource "aws_cloudwatch_log_stream" "webserverstream" {
  name           = "webserver"
  log_group_name = aws_cloudwatch_log_group.webserverlogs.name
}