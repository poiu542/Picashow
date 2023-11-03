import os
from dotenv import load_dotenv
import uvicorn
from fastapi import FastAPI
import routers.webuiapi_endpoint as webuiapi_endpoint
import routers.mongodb_endpoint as mongodb_endpoint

load_dotenv()
aws_access_key = os.getenv("LOCAL_AWS_S3_ACCESS_KEY")
aws_secret_key = os.getenv("LOCAL_AWS_S3_SECRET_KEY")
aws_bucket_name = os.getenv("LOCAL_AWS_S3_BUCKET")
aws_region = os.getenv("LOCAL_AWS_S3_REGION")

app = FastAPI()

if __name__ == "__main__":
    app.include_router(mongodb_endpoint.router)
    app.include_router(webuiapi_endpoint.router)
    uvicorn.run(app, host="0.0.0.0", port=8000)
