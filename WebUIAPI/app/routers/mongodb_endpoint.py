import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from pymongo import MongoClient
from fastapi import APIRouter
import app.main as main
main.load_dotenv()

router = APIRouter()

mongodb_URI = os.getenv("MONGODB_URI")
client = MongoClient(mongodb_URI)

# 배경화면 다운로드 시 유저 등록 API
@router.post("/download")
def registUser():
    return True


# 배경화면 조회 API
@router.get("/list")
def getList():
    # mongodb_URI = "localhost:27017"
    # collection = client.final.wallpaper
    collection = client['final']['wallpaper']

    image_url_list = collection.find({}, {'_id': False})

    sorted_list = sorted(image_url_list, key=lambda x: len(x['phone_number']), reverse=True)

    list = []

    for i in sorted_list:
        data = {'url': i['url']}
        list.append(data)

    return list
