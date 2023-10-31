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


class RequestBody(BaseModel):
    input_text: str


url = "http://127.0.0.1:7860/"

app = FastAPI()

# create API client with custom host, port
api = webuiapi.WebUIApi(host='127.0.0.1', port=7860)

# create API client with custom host, port and https
#api = webuiapi.WebUIApi(host='webui.example.com', port=443, use_https=True)

@app.post("/image")
def sendAPI(requestBody: RequestBody):
    input = "'" + requestBody.input_text + "'을(를) 바탕으로 이미지 프롬프트를 영어로 만들어줘."



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
                               "sd_model_checkpoint": "stableDiffusionXL_v30.safetensors [4e03869138]"
                           },
                           sampler_index="DDIM",
                           # sampler_index="DPM++ SDM",
                           width=512,
                           height=1024
                           )

    image = ai_image.images[0]

    image_bytes = BytesIO()
    image.save(image_bytes, format="png")
    image_bytes.seek(0)

    return StreamingResponse(image_bytes, media_type="image/png")


if __name__ == "__main__":
    load_dotenv()
    openai.organization = "org-cdrFOsSb24KAY7vi5zmNpYRv"
    openai.api_key = os.getenv("OPENAI_API_KEY")
    uvicorn.run(app, host="0.0.0.0", port=8000)
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)