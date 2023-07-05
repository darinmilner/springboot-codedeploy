FROM maven:3.9.2-eclipse-temurin-11-alpine

RUN apk upgrade && apk update && apk add --no-cache python3 py-pip git zip

RUN apk add --no-cache aws-cli

RUN echo which aws

RUN pip install boto3