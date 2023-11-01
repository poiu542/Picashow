import webuiapi
import openai
import os
import uvicorn
from dotenv import load_dotenv
import json
from fastapi import FastAPI
from fastapi.responses import StreamingResponse
from pydantic import BaseModel
from io import BytesIO
import logging
import boto3
from botocore.exceptions import NoCredentialsError
from PIL import Image

class RequestBody(BaseModel):
    input_text: str


url = "http://127.0.0.1:7860/"

app = FastAPI()

# create API client with custom host, port
api = webuiapi.WebUIApi(host='127.0.0.1', port=7860)

# create API client with custom host, port and https
#api = webuiapi.WebUIApi(host='webui.example.com', port=443, use_https=True)

def s3_connection():
    try:
        s3 = boto3.client(service_name="s3"
                          , aws_access_key_id=aws_access_key
                          , aws_secret_access_key=aws_secret_key
                          , region_name=aws_region)
    except Exception as e:
        logger.info(e)
    else:
        return s3

def s3_upload(image_bytes):

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

# def s3_get_image_url():

    # filename = {db에서 가져온 이미지 파일 이름 리스트}
    # location = s3.get_bucket_location(Bucket={자신이 설정한 버킷 이름})["LocationConstraint"]
    # return f"https://{{자신이 설정한 버킷 이름}}.s3.{location}.amazonaws.com/{filename}"


@app.post("/image")
def sendAPI(requestBody: RequestBody):
    input = "'" + requestBody.input_text + "'을(를) 바탕으로 이미지 프롬프트를 영어로 만들어줘."

    logger.info("test")

    response = openai.ChatCompletion.create(
        model="gpt-4",  # 사용할 모델 (현재 최신 모델)
        messages=[
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": input}
        ],
    )
    logger.info(response.choices[0].message['content'])
    ai_image = api.txt2img(prompt=response.choices[0].message['content'],
                           negative_prompt="(worst quality, low quality, normal quality, lowres, low details, oversaturated, undersaturated, overexposed, underexposed, grayscale, bw, bad photo, bad photography, bad art:1.4), (watermark, signature, text font, username, error, logo, words, letters, digits, autograph, trademark, name:1.2), (blur, blurry, grainy), morbid, ugly, asymmetrical, mutated malformed, mutilated, poorly lit, bad shadow, draft, cropped, out of frame, cut off, censored, jpeg artifacts, out of focus, glitch, duplicate, (airbrushed, cartoon, anime, semi-realistic, cgi, render, blender, digital art, manga, amateur:1.3), (3D ,3D Game, 3D Game Scene, 3D Character:1.1), (bad hands, bad anatomy, bad body, bad face, bad teeth, bad arms, bad legs, deformities:1.3)",
                           seed=-1,
                           cfg_scale=7,
                           override_settings={
                               # "sd_model_checkpoint": "majicmixRealistic_v7.safetensors [7c819b6d13]"
                               "sd_model_checkpoint": "sd_xl_base_1.0.safetensors [31e35c80fc]"
                           },
                           sampler_index="DDIM",
                           # sampler_index="DPM++ SDM",
                           width=512,
                           height=1024
                           )
    image = ai_image.images[0]
    # with Image.open("./00014-99100504.png") as image:
    image_bytes = BytesIO()
    image.save(image_bytes, format='png')


    image_url = s3_upload(image_bytes)

    # return StreamingResponse(image_bytes, media_type="image/png")
    return image_url



if __name__ == "__main__":
    load_dotenv()
    openai.organization = "org-cdrFOsSb24KAY7vi5zmNpYRv"
    openai.api_key = os.getenv("OPENAI_API_KEY")

    aws_access_key = os.getenv("LOCAL_AWS_S3_ACCESS_KEY")
    aws_secret_key = os.getenv("LOCAL_AWS_S3_SECRET_KEY")
    aws_bucket_name = os.getenv("LOCAL_AWS_S3_BUCKET")
    aws_region = os.getenv("LOCAL_AWS_S3_REGION")

    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    s3 = s3_connection()

    uvicorn.run(app, host="192.168.31.138", port=8000)