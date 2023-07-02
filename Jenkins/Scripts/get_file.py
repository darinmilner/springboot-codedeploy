import boto3
import logging
import sys
from botocore.exceptions import ClientError


def get_latest_envfile(access_key, secret_key):
    session = boto3.Session(
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
    )
    s3_resource = session.resource("s3", region_name="us-east-1")

    try:
        files = list(s3_resource.Bucket("taskapi-storage-bucket-useast1").objects.filter(Prefix="envfiles/"))
        files.sort(key=lambda f: f.last_modified)

        print(files[-1].key)
    except ClientError as e:
        logging.error(e)


access_key = sys.argv[1]
secret_key = sys.argv[2]

get_latest_envfile(access_key, secret_key)
