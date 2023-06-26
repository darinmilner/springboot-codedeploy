locals {
  zone1         = data.aws_availability_zones.az-zones.names[0]
  zone2         = data.aws_availability_zones.az-zones.names[1]
  zone3         = data.aws_availability_zones.az-zones.names[2]
  azs           = slice(data.aws_availability_zones.az-zones.names, 0, 3)
  server-port   = 8080
  db-port       = 5432
  all-ports     = 0
  ssh-port      = 22
  https-port    = 443
  http-port     = 80
  tcp-protocol  = "tcp"
  all-protocols = "-1"
  open-cidr     = "0.0.0.0/0"
  region        = replace(var.aws-region, "-", "")
  name-prefix   = format("%s-%s", var.project, var.system-environment)

  common-tags = {
    Env       = var.system-environment
    ManagedBy = "Terraform"
    Project   = var.project
  }
}

provider "aws" {
  region     = var.aws-region
  access_key = var.access-key
  secret_key = var.secret-key
}
