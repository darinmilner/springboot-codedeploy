resource "aws_iam_policy" "ec2-policy" {
  name        = "EC2Policy"
  path        = "/"
  description = "Policy to grant permission to EC2 instances"
  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Effect" : "Allow",
        "Action" : [
          "ec2:*",
          "autoscaling:Describe*",
          "cloudwatch:*",
          "logs:*",
          "sns:*",
          "iam:GetPolicy",
          "iam:GetPolicyVersion",
          "iam:GetRole"
        ],
        "Resource" : "*"
      },
      {
        "Effect" : "Allow",
        "Action" : "iam:CreateServiceLinkedRole",
        "Resource" : "arn:aws:iam::*:role/aws-service-role/events.amazonaws.com/AWSServiceRoleForCloudWatchEvents*",
        "Condition" : {
          "StringLike" : {
            "iam:AWSServiceName" : "events.amazonaws.com"
          }
        }
      }
    ]
  })
}

# Role for EC2
resource "aws_iam_role" "ec2-role" {
  name = "EC2Role"
  assume_role_policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
          "Service" : "ec2.amazonaws.com"
        },
        "Effect" : "Allow"
      }
    ]
  })
}

# Attach policy to role
resource "aws_iam_role_policy_attachment" "ec2-role-policy-attachment" {
  role       = aws_iam_role.ec2-role.name
  policy_arn = aws_iam_policy.ec2-policy.arn
}

# Attach role to instance
resource "aws_iam_instance_profile" "ec2-instance-profile" {
  name = "EC2InstanceProfile"
  role = aws_iam_role.ec2-role.name
}
