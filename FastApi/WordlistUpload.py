from datetime import datetime
from wsgiref.handlers import format_date_time
from time import mktime
import hashlib
import base64
from urllib.parse import urlparse
import requests
import json


class Upload:
    def __init__(self,word,appid,res_id,uid):
        self.requrl = "https://evo-gen.xfyun.cn/individuation/gen/upload"
        self.word = word
        self.appid = appid
        self.res_id = res_id
        self.uid = uid

    # calculate sha256 and encode to base64
    def sha256base64(self,data):
        sha256 = hashlib.sha256()
        sha256.update(data)
        digest = base64.b64encode(sha256.digest()).decode(encoding='utf-8')
        return digest


    # build  auth request url
    def assemble_auth_header(self):
        u = urlparse(self.requrl)
        host = u.hostname
        headers = {
            "host": host,
        }
        return headers

    def get_body(self):
        body = {
            "common": {
                "app_id": self.appid,
                "uid": self.uid
            },
            "business": {
                "res_id": self.res_id
            },
            "data": base64.b64encode(json.dumps(self.word).encode()).decode("utf-8")
        }
        bds = json.dumps(body)
        return bds

    def get_result(self):
        body = self.get_body()
        headers = self.assemble_auth_header()
        resp = requests.post(self.requrl, headers=headers, data=body)
        return  resp

if __name__ == '__main__':
    appid = "XXXXX"
    uid = "XXXXX"  # 自定义，由字母、数字、下划线组成
    res_id = "XXXXX"  # 自定义，由字母、数字、下划线组成
    # 黑白名单需要为json格式，其中黑名单替换词之间需要用空格隔开
    word = {
        "white_list": "衣据，鱿鱼圈",
        "black_list": "战士 展示,支撑 职称,规饭 鬼范,轮到 论道"
    }

    upload = Upload(word,appid,res_id,uid)
    resp = upload.get_result()
    print(resp.status_code, resp.text)