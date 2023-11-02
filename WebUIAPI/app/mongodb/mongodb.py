from ..main import app
from pymongo import MongoClient


# 배경화면 다운로드 시 유저 등록 API
@app.post("/download")
def registUser():
    return True


# 배경화면 조회 API
@app.get("/list")
def getList():
    mongodb_URI = "mongodb+srv://ldg03198:GZpxCobLh5To6L2k@wallpaper.vvwenhz.mongodb.net/?retryWrites=true&w=majority"
    # mongodb_URI = "localhost:27017"
    client = MongoClient(mongodb_URI)
    # collection = client.final.wallpaper

    collection = client['final']['wallpaper']

    image_url_list = collection.find({}, {'_id': False})

    sorted_list = sorted(image_url_list, key=lambda x: len(x['phone_number']), reverse=True)

    list = []

    for i in sorted_list:
        data = {'url': i['url']}
        list.append(data)

    print(list)

    return list
