package com.example.textspringboot.Controller;

import com.example.textspringboot.Common.Result;
import com.example.textspringboot.Entity.Docx;
import com.example.textspringboot.Entity.DocxVersion;
import com.example.textspringboot.mapper.DocxMapper;
import okhttp3.*;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/work")
public class WorkController {
    @Autowired
    HttpServletRequest request;
    @Resource
    DocxMapper docxMapper;
    @PostMapping("/correctDocx")
    public void uploadBeginFile(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("correctDocx");
        InputStream inputStream = null;
//        user u= (user) request.getSession().getAttribute("user");
//        if(null!=u){
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                    .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                    .build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                    .build();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:8000/chapter03/correctdocx")
                    .post(requestBody)
                    .build();
            Response response1 = client.newCall(request).execute();
            inputStream = response1.body().byteStream();
//        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Correct.docx", "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
        outputStream.close();
//        return Result.success();
    }
    @PostMapping("/correctFile")
    public void uploadSemiprocessingFile(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("correctFile");
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getOriginalFilename(),
                        RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/correctfile")
                .post(requestBody)
                .build();
        Response response1 = client.newCall(request).execute();
        InputStream inputStream = response1.body().byteStream();
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Result.docx", "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
        outputStream.close();
//        return Result.success();
    }
//    @PostMapping("/correctword")
//    public Result<?> correctword(@RequestParam("word") String word) throws IOException {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .callTimeout(300, TimeUnit.SECONDS)
//                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
//                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
//                .build();
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("word",word)
//                .build();
//        Request request = new Request.Builder()
//                .url("http://127.0.0.1:8000/chapter03/correctwords")
//                .post(requestBody)
//                .build();
//        Response response = client.newCall(request).execute();
//        String fruit=response.body().string();
//        return Result.success(fruit);
//    }
    @PostMapping("/FileToPdf")
    public void FileToPdf(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("FileToPdf");
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        System.out.println(fileSuffix);
        if(fileSuffix.equals(".zip")){
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                    .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                    .build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                    .build();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:8000/chapter03/conversion/topdfzip")
                    .post(requestBody)
                    .build();
            Response response1 = client.newCall(request).execute();
            InputStream inputStream = response1.body().byteStream();
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Result.zip", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
            outputStream.close();
        }else{
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                    .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                    .build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                    .build();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:8000/chapter03/conversion/topdf")
                    .post(requestBody)
                    .build();
            Response response1 = client.newCall(request).execute();
            InputStream inputStream = response1.body().byteStream();
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Result.pdf", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
            outputStream.close();
        }

    }
    @PostMapping("/FileToPdfForZip")
    public void FileToPdfForZip(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("FileToPdfForZip");
        //已废弃
    }
    @PostMapping("/FileToDocxForZip")
    public void FileToDocxForZip(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("FileToDocxForZip");
        //已废弃
    }
    @PostMapping("/FileToDocx")
    public void FileToDocx(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("FileToDocx");
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        System.out.println(fileSuffix);
        if(fileSuffix.equals(".zip")){
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                    .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                    .build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                    .build();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:8000/chapter03/conversion/todocxzip")
                    .post(requestBody)
                    .build();
            Response response1 = client.newCall(request).execute();
            InputStream inputStream = response1.body().byteStream();
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Result.zip", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
            outputStream.close();
        }else{
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                    .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                    .build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                    .build();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:8000/chapter03/conversion/todocx")
                    .post(requestBody)
                    .build();
            Response response1 = client.newCall(request).execute();
            InputStream inputStream = response1.body().byteStream();
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Result.docx", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
            outputStream.close();
        }
    }
    @PostMapping("/FileBat")
    public void FileBat(HttpServletResponse response,
                          @RequestParam("DocxFile") MultipartFile DocxFile,
                         @RequestParam("ExcelFile") MultipartFile ExcelFile,
                        @RequestParam("control") String control) throws IOException {
        System.out.println("FileBat");
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("docx_file", DocxFile.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), DocxFile.getBytes()))
                .addFormDataPart("excel_file", ExcelFile.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), ExcelFile.getBytes()))
                .addFormDataPart("control",control)
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/filebat")
                .post(requestBody)
                .build();
        Response response1 = client.newCall(request).execute();
        InputStream inputStream = response1.body().byteStream();
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("result.zip", "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
        outputStream.close();
    }
    @PostMapping("/FileCopy")
    public void FileCopy(HttpServletResponse response,
                        @RequestParam("File") MultipartFile DocxFile,
                        @RequestParam("ExcelFile") MultipartFile ExcelFile,
                         @RequestParam("copyNum") String copyNum) throws IOException {
        System.out.println("FileCopy");
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("docx_file", DocxFile.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), DocxFile.getBytes()))
                .addFormDataPart("excel_file", ExcelFile.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), ExcelFile.getBytes()))
                .addFormDataPart("copy_num",copyNum)
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/filecopy")
                .post(requestBody)
                .build();
        Response response1 = client.newCall(request).execute();
        InputStream inputStream = response1.body().byteStream();
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("result.zip", "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
        outputStream.close();
    }
    @PostMapping("/HumbleSubstitution")
    public Result<?> HumbleSubstitution(@org.springframework.web.bind.annotation.RequestParam("word") String word,
                                        @org.springframework.web.bind.annotation.RequestParam("sex") Boolean sex,
                                        @org.springframework.web.bind.annotation.RequestParam("identity") Integer identity,
                                        @org.springframework.web.bind.annotation.RequestParam("Generation") Integer Generation) throws IOException {
        System.out.println("HumbleSubstitution");
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("word",word)
                .addFormDataPart("sex", String.valueOf(sex))
                .addFormDataPart("identity", String.valueOf(identity))
                .addFormDataPart("Generation", String.valueOf(Generation))
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/HumbleSubstitution")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String fruit=response.body().string();
        return Result.success(fruit);
    }
    @PostMapping("/TextContrast")
    public Result<?> TextContrast(@org.springframework.web.bind.annotation.RequestParam("text1") String text1,
                                  @org.springframework.web.bind.annotation.RequestParam("text2") String text2) throws IOException {
        System.out.println("TextContrast");
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("text1",text1)
                .addFormDataPart("text2",text2)
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/TextContrast")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String fruit=response.body().string();
        System.out.println(StringEscapeUtils.unescapeJson(fruit));
        return Result.success(StringEscapeUtils.unescapeJson(fruit));
    }
    @PostMapping("/GetText")
    public Result<?> GetText(@RequestParam("versionName")String versionName) throws IOException {
        System.out.println("GetText");
        DocxVersion DV=(DocxVersion)request.getSession().getAttribute("DV");
        if(DV==null){
            return Result.error("-1","Session过期");
        }
        String TableName=DV.getTableName();
        Docx docx=docxMapper.SelectByOne(versionName,TableName);
        String path1=docx.getAddress();
        OkHttpClient client=new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("path1",path1)
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/GetDocxText")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String fruit=response.body().string();
        fruit=StringEscapeUtils.unescapeJava(fruit);
        return Result.success(fruit);
    }
}
