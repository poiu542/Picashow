import os
from dotenv import load_dotenv
import logging
from fastapi import FastAPI
from api.webuiapi_endpoint import app as webuiapi_endpoint
app = FastAPI()
app.include_router(webuiapi_endpoint)

if __name__ == "__main__":
    load_dotenv()
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)
    aws_access_key = os.getenv("LOCAL_AWS_S3_ACCESS_KEY")
    aws_secret_key = os.getenv("LOCAL_AWS_S3_SECRET_KEY")
    aws_bucket_name = os.getenv("LOCAL_AWS_S3_BUCKET")
    aws_region = os.getenv("LOCAL_AWS_S3_REGION")


