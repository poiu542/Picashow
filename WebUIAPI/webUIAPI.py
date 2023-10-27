import webuiapi
from fastapi import FastAPI
from fastapi.responses import StreamingResponse
from pydantic import BaseModel
from io import BytesIO


class RequestBody(BaseModel):
    prompt: str


url = "http://127.0.0.1:7860/"

app = FastAPI()

# create API client with custom host, port
api = webuiapi.WebUIApi(host='127.0.0.1', port=7860)

# create API client with custom host, port and https
#api = webuiapi.WebUIApi(host='webui.example.com', port=443, use_https=True)

@app.post("/image")
def sendAPI(requestBody: RequestBody):
    ai_image = api.txt2img(prompt=requestBody.prompt,
                           negative_prompt="(worst quality:2),(low quality:2),(normal quality:2),lowers,watermark,",
                           seed=-1,
                           cfg_scale=7,
                           override_settings={
                               "sd_model_checkpoint": "majicmixRealistic_v7.safetensors [7c819b6d13]"
                           },
                           sampler_index="DPM++ 2M Karras",
                           width=512,
                           height=1024,
                           )

    image = ai_image.images[0]

    image_bytes = BytesIO()
    image.save(image_bytes, format="png")
    image_bytes.seek(0)

    return StreamingResponse(image_bytes, media_type="image/png")


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)