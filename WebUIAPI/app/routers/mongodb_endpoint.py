import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from pymongo import MongoClient

import app.main as main
main.load_dotenv()
from fastapi import APIRouter, Query
import math


router = APIRouter()

mongodb_URI = os.getenv("MONGODB_URI")
client = MongoClient(mongodb_URI)

# 배경화면 다운로드 시 유저 등록 API
@router.post("/download")
def registUser():
    return True


# 배경화면 조회 API
@router.get("/list")
async def getList(q: int = Query(default=1)):
    # mongodb_URI = "localhost:27017"
    collection = client.final.wallpaper

    page_number = q
    page_size = 15

    image_url_list = (collection
                      .find({}, {'_id': False})
                      .skip((page_number-1)*page_size)
                      .limit(page_size))


    total_pages = math.ceil(collection.count_documents({}) / page_size)

    last_page_num = math.ceil(total_pages / page_size)

    sorted_list = sorted(image_url_list, key=lambda x: len(x['phone_number']), reverse=True)

    list = []

    for i in sorted_list:
        data = {'url': i['url']}
        list.append(data)

    return {'list': list,
             'limit': page_size,
             'page': page_number,
             'last_page_num': last_page_num}