variable "aws-region" {
  type = string
}

variable "system-environment" {
  type = string
}

variable "project" {
  type    = string
  default = "Task-API"
}

variable "access-key" {
  type = string
}

variable "secret-key" {
  type = string
}

variable "cidr-block" {
  type    = string
  default = "10.0.0.0/24"
}

variable "private-cidrs" {
  type    = list(string)
  default = ["10.0.10.0/24", "10.0.11.0/24"]
}

variable "vpc-cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "amis" {
  type    = map(string)
  default = {
    us-east-1 = "ami-0947d2ba12ee1ff75"
    us-east-2 = "ami-03657b56516ab7912"
  }
}
