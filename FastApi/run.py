from fastapi import FastAPI
import  uvicorn
from tutorial import app03
app=FastAPI()
app.include_router(app03,prefix='/chapter03',tags=['论文修改辅助平台'])

if __name__ =='__main__':
    uvicorn.run('run:app',host='127.0.0.1',port=8000,reload=True,debug=True,workers=8)
