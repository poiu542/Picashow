import logging
import boto3
from botocore.exceptions import NoCredentialsError
import os
from ..main import logger
from ..main import aws_access_key
from ..main import aws_secret_key
from ..main import aws_bucket_name
from ..main import aws_region


from io import BytesIO

def connection():

    try:
        s3 = boto3.client(service_name="s3"
                          , aws_access_key_id=aws_access_key
                          , aws_secret_access_key=aws_secret_key
                          , region_name=aws_region)
    except Exception as e:
        logger.info(e)
    else:
        return s3

def upload(image_bytes, s3):
    image_buffer = BytesIO(image_bytes.getvalue())
    image_buffer.seek(0)

    image_hash = hash(image_bytes.getvalue())
    s3_object_key = f'images/{image_hash}.png'

    logger.info(image_buffer)
    logger.info(s3_object_key)

    try:
        s3.upload_fileobj(image_buffer, aws_bucket_name, s3_object_key, ExtraArgs={'ContentType': 'image/png'})

    except NoCredentialsError:
        logger.info("AWS 계정 정보 없음")
    except Exception as e:
        logger.info("오류발생", e)

    return f"https://{aws_bucket_name}.s3.{aws_region}.amazonaws.com/{s3_object_key}"