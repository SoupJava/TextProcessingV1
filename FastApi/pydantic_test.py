# from pydantic import BaseModel, ValidationError,constr
# from datetime import datetime, date
# from pathlib import Path
# from typing import List, Optional
# from sqlalchemy import Column,Integer,String
# from sqlalchemy.dialects.postgresql import  ARRAY
# from sqlalchemy.ext.declarative import declarative_base
# print("\033[31m1.---Pydantic的基本用法---\033[0m")
#
#
# class User(BaseModel):
#     id: int  # 必填字段
#     name: str = "John Snow"  # 有默认值，选填字段
#     signup_ts: Optional[datetime] = None
#     friends: List[int] = []  # 列表中元素是int类或者可以直接转换成int类型
#
#
# external_data = {
#     "id": "123",
#     "signup_ts": "2022-12-22 12:32",
#     "friends": [1, 2, "3"]  # "3"可以直接int（“3”）
# }
# user = User(**external_data)
# print(user.id, user.friends)
# print(repr(user.signup_ts))
# print(user.dict())
# print("\033[31m2.---校验失败处理---\033[0m")
# try:
#     User(id=1, signup_ts=datetime.today(), friends=[1, 2, "not number"])
# except ValidationError as e:
#     print(e.json())
# print("\033[31m4.---模型类的属性和方法---\033[0m")
# print(user.dict())
# print(user.json())
# print(user.copy())
# print(User.parse_obj(obj=external_data))
# print(User.parse_raw('{"id": 123, "name": "John Snow", "signup_ts": "2022-12-22T12:32:00", "friends": [1, 2, 3]}'))
# path = Path('py_tu.json')
# path.write_text('{"id": 123, "name": "John Snow", "signup_ts": "2022-12-22T12:32:00", "friends": [1, 2, 3]}')
# print(User.parse_file(path))
# print(user.schema())
# print(user.schema_json())
# user_data = {"id": "error", "name": "John Snow", "signup_ts": "2022-12-22T12:32:00", "friends": [1, 2, 3]}
# print(User.construct(**user_data))  # 不检验，直接创建模型类
# print(User.__fields__.keys())  # 定义模型类的时候，所有字段都注明类型，字段顺序就不会乱
#
# print("\033[31m4.---递归模型---\033[0m")
#
#
# class Sound(BaseModel):
#     sound: str
#
#
# class Dog(BaseModel):
#     birthday: date
#     weight: float = Optional[None]
#     sound: List[Sound]
#
#
# dogs = Dog(birthday= date.today(), weight=6.66, sound=[{"sound": "wnagwang--"}, {"sound": "yingying--"}])
# print(dogs.dict())
#
# print("\033[31m5.---ORM模型:从类实例创建符合ORM对象的模型:---\033[0m")
#
# Base=declarative_base()
#
# class CompanyOrm(Base):
#     _tablename_='companies'
#     id=Column(Integer,primary_key=True,nullable=False)
#     public_key=Column(String(20),index=True,nullable=False,unique=True)
#     name=Column(String(63),unique=True)
#     domains=Column(ARRAY(String(255)))
#
# class CompanyMode(BaseModel):
#     id:int
#     public_key:constr(max_length=20)
#     name: constr(max_length=63)
#     domains: List[constr(max_length=255)]
#     class Config:
#         orm_mode=True
#
# co_orm=CompanyOrm(
#     id=123,
#     public_key='foobar',
#     name='Testing',
#     domains=['example.com','imooc.com']
# )
# print(CompanyMode.from_orm(co_orm))
