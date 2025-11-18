import FileBat
import FileConversion
import SampleWord
import comparsion
import do_docx
import docx_test
from fastapi import APIRouter, Path, Query, FastAPI, File, UploadFile, Form
from enum import Enum
from typing import Optional,List
from pydantic import  BaseModel,Field
import time
import uuid

from starlette.responses import FileResponse

app03=APIRouter()

@app03.post("/TextContrast/")
async def text_contrast(path1:str=Form(...),
                        path2:str=Form(...)):
    try:
        json_fruit=comparsion.do_com(path1,path2)
        return json_fruit
    except Exception as e:
        return {"message":str(e)}
@app03.post("/GetDocxText/")
async def get_docx_text(path1:str=Form(...)):
    try:
        text=comparsion.gettext(path1)
        return text
    except Exception as e:
        return {"message":str(e)}
@app03.post("/correctdocx/")
async def correct_docx(file: UploadFile = File(...)):
    start = time.time()
    try:
        res = await file.read()
        path="C:\\Project\\docx\\"+str(uuid.uuid1())+".docx"
        with open(path, "wb") as f:
            f.write(res)
        correct_code=docx_test.text_process(path)
        if correct_code==100:
            return FileResponse(
                path,
                filename="Correct.docx",
            )
        else:
            return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': file.filename}

@app03.post("/correctfile/")
async def coorrect_file(file: UploadFile = File(...)):
    start = time.time()
    try:
        res = await file.read()
        path="C:\\Project\\docx\\"+str(uuid.uuid1())+".docx"
        with open(path, "wb") as f:
            f.write(res)
        correct_code=do_docx.do_docx(path)
        if correct_code==100:
            return FileResponse(
                path,
                filename="Result.docx",
            )
        else:
            return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': file.filename}

@app03.post("/conversion/topdf")
async def conversion_topdf(file: UploadFile = File(...)):
    start = time.time()
    try:
        res = await file.read()
        filetype=file.filename.split('.')[-1]
        if filetype=='docx':
            docxName=str(uuid.uuid1())
            path="C:\\Project\\docx\\"+docxName+".docx"
            path2="C:\\Project\\pdf\\"+docxName+".pdf"
            with open(path, "wb") as f:
                f.write(res)
            correct_code=FileConversion.docxTopdf(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.pdf",
                )
            else:
                return "error"
        if filetype=='ppt':
            pptName=str(uuid.uuid1())
            path="C:\\Project\\ppt\\"+pptName+".ppt"
            path2="C:\\Project\\pdf\\"+pptName+".pdf"
            with open(path, "wb") as f:
                f.write(res)
            correct_code=FileConversion.pptTopdf(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.pdf",
                )
            else:
                return "error"
        if filetype=='xlsx':
            xlsxName=str(uuid.uuid1())
            path="C:\\Project\\excel\\"+xlsxName+".xlsx"
            path2="C:\\Project\\pdf\\"+xlsxName+".pdf"
            with open(path, "wb") as f:
                f.write(res)
            correct_code=FileConversion.excelTopdf(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.pdf",
                )
            else:
                return "error"
        if filetype=='jpg':
            jpgName=str(uuid.uuid1())
            path="C:\\Project\\jpg\\"+jpgName+".jpg"
            path2="C:\\Project\\pdf\\"+jpgName+".pdf"
            with open(path, "wb") as f:
                f.write(res)
            correct_code=FileConversion.jpgTopdf(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.pdf",
                )
            else:
                return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': file.filename}
@app03.post("/conversion/topdfzip")
async def conversion_topdf_zip(file: UploadFile=File(...)):
    start=time.time()
    try:
        res= await file.read()
        filetype=file.filename.split('.')[-1]
        if filetype=='zip':
            zipName=str(uuid.uuid1())
            zipName2=str(uuid.uuid1())
            path="C:\\Project\\zip\\"+zipName+".zip"
            path2="C:\\Project\\zip\\"+zipName2+".zip"
            with open(path,"wb") as f:
                f.write(res)
            correct_code=FileConversion.zipTopdf(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.zip",
                )
            else:
                return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': file.filename}
@app03.post("/conversion/todocxzip")
async def conversion_todocx_zip(file: UploadFile=File(...)):
    start=time.time()
    try:
        res= await file.read()
        filetype=file.filename.split('.')[-1]
        if filetype=='zip':
            zipName=str(uuid.uuid1())
            zipName2=str(uuid.uuid1())
            path="C:\\Project\\zip\\"+zipName+".zip"
            path2="C:\\Project\\zip\\"+zipName2+".zip"
            with open(path,"wb") as f:
                f.write(res)
            correct_code=FileConversion.zipTodocx(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.zip",
                )
            else:
                return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': file.filename}

@app03.post("/conversion/todocx")
async def conversion_todocx(file: UploadFile = File(...)):
    start = time.time()
    try:
        res = await file.read()
        filetype=file.filename.split('.')[-1]
        if filetype=='pdf':
            pdfName=str(uuid.uuid1())
            path="C:\\Project\\pdf\\"+pdfName+".pdf"
            path2="C:\\Project\\docx\\"+pdfName+".docx"
            with open(path, "wb") as f:
                f.write(res)
            correct_code=FileConversion.pdfTodocx(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.docx",
                )
            else:
                return "error"
        if filetype=='doc':
            docName=str(uuid.uuid1())
            path="C:\\Project\\doc\\"+docName+".doc"
            path2="C:\\Project\\docx\\"+docName+".docx"
            with open(path, "wb") as f:
                f.write(res)
            correct_code=FileConversion.docTodocx(path,path2)
            if correct_code==100:
                return FileResponse(
                    path2,
                    filename="Result.docx",
                )
            else:
                return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': file.filename}
@app03.post("/filebat")
async def file_bat(docx_file: UploadFile = File(...),
                   excel_file: UploadFile=File(...),
                   control:str=Form(...)):
    start = time.time()
    try:
        res = await docx_file.read()
        path1="C:\\Project\\docx\\"+str(uuid.uuid1())+".docx"
        res2 = await excel_file.read()
        path2="C:\\Project\\excel\\"+str(uuid.uuid1())+".xlsx"
        fruitZipName=str(uuid.uuid1())
        path3="C:/Project/TEMP/"+fruitZipName+"/"
        with open(path1, "wb") as f:
            f.write(res)
        with open(path2, "wb") as f:
            f.write(res2)
        correct_code=FileBat.do_bat(path1,path2,path3,fruitZipName,control)
        if correct_code==100:
            return FileResponse(
                "C:\\Project\\zip\\"+fruitZipName+".zip",
                filename="fruit.zip",
            )
        else:
            return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': docx_file.filename+" "+excel_file.filename}
@app03.post("/filecopy")
async def file_copy(docx_file: UploadFile = File(...),
                   excel_file: UploadFile=File(...),
                   copy_num:str=Form(...)):
    start = time.time()
    try:
        res = await docx_file.read()
        file_type=docx_file.filename.split('.')[-1]
        data_type=excel_file.filename.split('.')[-1]
        path1="C:\\Project\\docx\\"+str(uuid.uuid1())+"."+file_type
        res2 = await excel_file.read()
        path2="C:\\Project\\excel\\"+str(uuid.uuid1())+"."+data_type
        fruitZipName=str(uuid.uuid1())
        path3="C:/Project/TEMP/"+fruitZipName+"/"
        datatype=excel_file.filename.split('.')[-1]
        with open(path1, "wb") as f:
            f.write(res)
        with open(path2, "wb") as f:
            f.write(res2)
        correct_code=FileBat.file_name_bat(path1,path2,path3,fruitZipName,datatype,copy_num)
        if correct_code==100:
            return FileResponse(
                "C:\\Project\\zip\\"+fruitZipName+".zip",
                filename="fruit.zip",
                )
        else:
            return "error"
    except Exception as e:
        return {"message": str(e), 'time': time.time() - start, 'filename': docx_file.filename+" "+excel_file.filename}
@app03.post("/HumbleSubstitution")
async def HumbleSubstitution(word:str=Form(...),
                             sex:bool=Form(...),
                             identity:int=Form(...),
                             Generation:int=Form(...)):
    try:
        word=SampleWord.replaceWords(word,sex,identity,Generation)
        return word
    except Exception as e:
        return {"message": str(e)}
@app03.post("/ComReports")
async def ComReports(file:UploadFile=Form(...),
                     Address:str=Form(...)):
    try:
        fruitExcelName=str(uuid.uuid1())
        res = await file.read()
        path="C:\\Project\\docx\\"+str(uuid.uuid1())+".docx"
        with open(path, "wb") as f:
            f.write(res)
        correct_code=FileBat.get_Similarity(path,Address,fruitExcelName)
        if correct_code==100:
            return FileResponse(
                "C:\\Project\\excel\\"+fruitExcelName+".xlsx",
                filename="fruit.xlsx",
                )
        else:
            return "error"
    except Exception as e:
        return {"message":str(e)}
