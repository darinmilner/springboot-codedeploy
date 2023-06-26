resource "aws_security_group" "server-sg" {
  name        = "server-sg"
  description = "server security group"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Open API port 8080"
    from_port   = local.server-port
    to_port     = local.server-port
    protocol    = local.all-protocols
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SSH from VPC"
    from_port   = local.ssh-port
    to_port     = local.ssh-port
    protocol    = local.tcp-protocol
    cidr_blocks = [aws_vpc.main.cidr_block]
  }

  egress {
    description = "Outbound traffic"
    from_port   = local.all-ports
    to_port     = local.all-ports
    protocol    = local.all-protocols
    cidr_blocks = [local.open-cidr]
  }

  tags = {
    Name = "Server-sg"
  }
}
