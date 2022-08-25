from pymongo import MongoClient
from flask import make_response
import json

connection_string = "mongodb+srv://mongotobq:M0ngoToBqPoc@mongo-bq.kn30v.mongodb.net/"


def connectToMongo(request):
    request_json = request.get_json()
    client = MongoClient(connection_string)
    dbname = client["ecommerce"]
    collection_name = dbname["products"]
    item_details = collection_name.aggregate(request_json)
    return json.dumps(list(item_details),default=str)