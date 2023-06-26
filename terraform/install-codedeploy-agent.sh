#!/bin/bash
sudo yum -y update
sudo yum install -y ruby
cd /home/ec2-user
curl -O https://aws-codedeploy-us-east-1.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo yum install -y python-pip
sudo pip install awscli
sudo amazon-linux-extras install java-openjdk11

sudo service codedeploy-agent status
java -vers