import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from pymongo import MongoClient

import app.main as main
main.load_dotenv()
from fastapi import APIRouter, Query, Path
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
def getList(page: int = Query(default=1)):
    # mongodb_URI = "localhost:27017"
    collection = client.final.wallpaper

    page_number = page
    limit = 15

    image_url_list = (collection
                      .find({}, {'_id': False})
                      .skip((page_number-1)*limit)
                      .limit(limit))


    total_pages = math.ceil(collection.count_documents({}) / limit)

    last_page_num = math.ceil(total_pages / limit)

    sorted_list = sorted(image_url_list, key=lambda x: len(x['phone_number']), reverse=True)

    list = []

    for i in sorted_list:
        data = {'url': i['url']}
        list.append(data)

    return {'list': list,
             'limit': limit,
             'page_number': page_number,
             'last_page_num': last_page_num}

@router.get("/list/{theme}")
def getThemeList(theme: str = Path(), page: int = Query(default=1)):

    collection = client.final.wallpaper
    page_number = page
    limit = 15

    image_url_list = (collection
                      .find({'theme': theme}, {'_id': False})
                      .skip((page_number-1)*limit)
                      .limit(limit))

    total_pages = math.ceil(collection.count_documents({}) / limit)

    last_page_num = math.ceil(total_pages / limit)
    sorted_list = sorted(image_url_list, key=lambda x: len(x['phone_number']), reverse=True)

    list = []

    for i in sorted_list:
        data = {'url': i['url']}
        list.append(data)
    return {'list': list,
        'limit': limit,
        'page_number': page_number,
        'last_page_num': last_page_num}
