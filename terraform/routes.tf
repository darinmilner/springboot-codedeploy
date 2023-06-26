resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "Main Internet Gateway"
  }
}

resource "aws_route_table" "public-route" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = local.open-cidr
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "Public Route Table"
  }
}

resource "aws_route_table_association" "public-rt-assoc" {
  route_table_id = aws_route_table.public-route.id
  subnet_id      = aws_subnet.public.id
}

resource "aws_route_table" "private-route" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = local.open-cidr
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "Private Route Table"
  }
}

resource "aws_route_table_association" "private-rt-assoc" {
  route_table_id = aws_route_table.private-route.id
  subnet_id      = aws_subnet.private.id
}