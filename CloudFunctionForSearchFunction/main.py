from pymongo import MongoClient
from flask import make_response
import json

connection_string = "mongodb+srv://mongotobq:M0ngoToBqPoc@mongo-bq.kn30v.mongodb.net/"


def connectToMongo(request):
    request_json = request.get_json()
    print()
    dbasename = request_json["databasename"]
    cname = request_json["collectionname"]
    del request_json["databasename"]
    del request_json["collectionname"]
    print(request_json)
    if request.args and 'message' in request.args:
        return request.args.get('message')
    elif request_json and 'message' in request_json:
        return request_json['message']
    else:
        client = MongoClient(connection_string)
        dbname = client[dbasename]
        collection_name = dbname[cname]
        item_details = collection_name.find(request_json)
        return json.dumps(list(item_details),default=str)
