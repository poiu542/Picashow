import os
from dotenv import load_dotenv
import logging
import uvicorn
from fastapi import FastAPI
from app.routers import webuiapi_endpoint
from app.routers import mongodb_endpoint

if __name__ == "__main__":
    load_dotenv()
    aws_access_key = os.getenv("LOCAL_AWS_S3_ACCESS_KEY")
    aws_secret_key = os.getenv("LOCAL_AWS_S3_SECRET_KEY")
    aws_bucket_name = os.getenv("LOCAL_AWS_S3_BUCKET")
    aws_region = os.getenv("LOCAL_AWS_S3_REGION")
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    app = FastAPI()

    app.include_router(mongodb_endpoint.router)
    app.include_router(webuiapi_endpoint.router)
    uvicorn.run(app, host="0.0.0.0", port=8000)

