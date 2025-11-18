package com.example.textspringboot.Controller;

import com.example.textspringboot.Common.Result;
import com.example.textspringboot.Entity.Docx;
import com.example.textspringboot.Entity.DocxVersion;
import com.example.textspringboot.Entity.user;
import com.example.textspringboot.mapper.DocxMapper;
import com.example.textspringboot.mapper.DocxVersionMapper;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/docx")
public class DocxController {
    @Resource
    DocxMapper docxmapper;
    @Resource
    DocxVersionMapper docxVersionmapper;
    @Autowired
    HttpServletRequest request;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");

    @PostMapping("/mydocx")//获取所有文件，什么都不用传入，不是项目，跟之前的不太一样
    public Result<?> MyDocx(@RequestParam("ThesisName") String ThesisName){
        System.out.println("mydocx");
        List<Docx> docxes;
        user u= (user) request.getSession().getAttribute("user");
        if(null==u){
            return Result.error("-1","用户Session过期");
        }
        Result frult=SelectFile(ThesisName);
        if(frult.getCode().equals("-1")||frult.getCode().equals("2")){
            return Result.error("-1","Session过期了");
        }
        DocxVersion DV=(DocxVersion) frult.getData();
        docxes=docxmapper.SelectByUser(u.getUserId(),DV.getTableName());
        return Result.success(docxes);
    }
    @PostMapping("/Selectdocx")//获取文件信息，给我传入文件名就行，不是项目
    public Result<?> SelDocx(@RequestParam("name") String Dname,
                             @RequestParam("ThesisName") String ThesisName){
        System.out.println("Selectdocx");
        user u= (user) request.getSession().getAttribute("user");
        if(null==u){
            return Result.error("-1","Session过期");
        }
        Result frult=SelectFile(ThesisName);
        if(frult.getCode().equals("-1")||frult.getCode().equals("2")){
            return Result.error("-1","Session过期了");
        }
        DocxVersion DV=(DocxVersion) frult.getData();
        System.out.println(Dname);
        Docx docxes= docxmapper.SelectByOne(Dname,DV.getTableName());
        return Result.success(docxes);
    }
    @PostMapping("/DeleteDocx")//传回来个文件名就行了，问题已经修复，删除项目中的某个版本（不是项目）
    public Result<?> DelDocx(@RequestParam("name") String Dname,
                             @RequestParam("ThesisName") String ThesisName){
        System.out.println("DeleteDocx");
        user u= (user) request.getSession().getAttribute("user");
        if(null==u){
            return Result.error("-1","Session过期");
        }
        Result frult=SelectFile(ThesisName);
        if(frult.getCode().equals("-1")||frult.getCode().equals("2")){
            return Result.error("-1","Session过期了");
        }
        DocxVersion DV=(DocxVersion) frult.getData();
        System.out.println(Dname);
        Docx docxes= docxmapper.SelectByOne(Dname,DV.getTableName());
        System.out.println(docxes.getAddress());
        File file=new File(docxes.getAddress());
        System.out.println(Dname);
        if (file.delete()) {
            docxmapper.DelDocx(Dname,DV.getTableName());
            return Result.success();
        } else {
            return Result.error("-1","文件删除失败");
        }
    }
    @PostMapping("/docxdownload")//项目的某个版本下载，传给我个文件名字就行
    public void DownloadDocx(HttpServletResponse response, @RequestParam("name") String Dname,
                             @RequestParam("ThesisName") String ThesisName) throws IOException {
        System.out.println("docxdownload");
        user u= (user) request.getSession().getAttribute("user");
        if(null==u){
            return ;
        }
        String FileName=Dname;
        Result frult=SelectFile(ThesisName);
        if(frult.getCode().equals("-1")||frult.getCode().equals("2")){
            return ;
        }
        DocxVersion DV=(DocxVersion) frult.getData();
        Docx docxes= docxmapper.SelectByOne(Dname,DV.getTableName());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+
                URLEncoder.encode(FileName,"UTF-8")+".docx");

        // 从文件系统中读取文件内容
        File file = new File(docxes.getAddress());
        FileInputStream inputStream = new FileInputStream(file);

        // 将文件内容写入response的outputStream
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }
    @PostMapping("/docxupload")//上传文件，给我传个文件就行（不是项目），版本号会随着更改，后台自动判断
    public Result<?> UploadDocx(@RequestParam("file") MultipartFile file,
                                @RequestParam("ThesisName") String ThesisName) throws IOException {
        System.out.println("docxupload");
        if (null == file) {
            return Result.error("-1","上传失败，未找到文件");
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        if (!fileName.endsWith(".docx")) {
            return Result.error("-1","上传失败，请上传docx文件");
        }
        String filePath;
        String filename="";//存储到数据库的名字
        Result frult=SelectFile(ThesisName);
        if(frult.getCode().equals("-1")||frult.getCode().equals("2")){
            return Result.error("-1","Session过期了");
        }
        DocxVersion DV=(DocxVersion) frult.getData();
        int bigversion=DV.getBV();
        int smallversion=DV.getSV();
        // 得到格式化后的日期
        String daytime = sdf.format(new Date());
        String hourtime=sdf2.format(new Date());
        String uploadTime=daytime+"-"+hourtime;
        // 获取上传的文件名称
        fileName = file.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString();
        filePath="C:/Project/user/";
        try {
            user u= (user) request.getSession().getAttribute("user");
            if(null==u){
                return Result.error("-1","Session过期");
            }
            String folderName = filePath+u.getUserId()+"/"+DV.getTableName();
            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdir();
            }
            filePath=filePath+u.getUserId()+"/";
            String filePathTemp=filePath+DV.getTableName()+"/";
            filePath=filePath+DV.getTableName()+"/"+newFileName+".docx";
            System.out.println(filePath);
            File dest = new File(filePath);
            file.transferTo(dest);
            List<String> TEMP=new ArrayList<>();
            System.out.println("上传成功，当前上传的文件保存在 "+filePath + newFileName);
            if(docxmapper.CountTable(DV.getTableName())<1){
                bigversion=1;
                smallversion=1;
            }else {
                Docx d1=docxmapper.SelectNewFile(DV.getTableName());
                String filePath2=d1.getAddress();
                OkHttpClient client = new OkHttpClient.Builder()
                        .callTimeout(300, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                        .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                        .build();
                FormBody requestBody=new FormBody.Builder()
                        .add("path1",filePath)
                        .add("path2",filePath2)
                        .build();
                Request request1=new Request.Builder()
                        .url("http://127.0.0.1:8000/chapter03/TextContrast")
                        .post(requestBody)
                        .build();
                Response response1 = client.newCall(request1).execute();
                Double difNum= Double.valueOf(response1.body().string());
                System.out.println(difNum);
                TEMP.add(String.valueOf(bigversion));
                TEMP.add(String.valueOf(smallversion));
                if(difNum<=1&&difNum>DV.getNeglect()){
                    smallversion=smallversion+1;
                    TEMP.add("toosmall");
                } else if (difNum<=DV.getNeglect()&&difNum>DV.getIteration()) {
                    smallversion=smallversion+1;
                    TEMP.add("normal");
                } else if (difNum<=DV.getIteration()&&difNum>DV.getVersion()) {
                    bigversion=bigversion+1;
                    smallversion=1;
                    TEMP.add("normal");
                }else {
                    bigversion=bigversion+1;
                    smallversion=1;
                    TEMP.add("toobig");
                }
            }
            filename="第"+bigversion+"."+smallversion+"版";
            TEMP.add(filename);
            Docx d=new Docx(u.getUserId(),DV.getDocxName(),filename,uploadTime,filePath);
            docxmapper.AddDocx(d,DV.getTableName());
            docxVersionmapper.SetVersion(bigversion,smallversion,DV.getDocxName());
            // TODO: 2023/4/7 乱码重命名+修改数据库里面的名字
            filePathTemp=filePathTemp+filename+".docx";
            // 旧的文件或目录
            File oldName = new File(filePath);
            // 新的文件或目录
            File newName = new File(filePathTemp);
            if (newName.exists()) {  //  确保新的文件名不存在
                throw new java.io.IOException("file exists");
            }
            if(oldName.renameTo(newName)) {
                System.out.println("已重命名");
            } else {
                System.out.println("Error");
            }
            docxmapper.UpdateAddress(filename,filePathTemp,DV.getTableName());
            RefreshSession();
            return Result.success(TEMP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 待完成 —— 文件类型校验工作
        return Result.error("-1","失败，程序出问题了");
    }
    @PostMapping("/BackUpload")//版本回退
    public Result<?> BackUpload(@RequestParam("BV") Integer BV,
                                @RequestParam("SV") Integer SV,
                                @RequestParam("ThesisName") String ThesisName,
                                @RequestParam("name") String Dname){
        // TODO: 2023/4/8

        Result fruit1=UpdateVersion(BV,SV,ThesisName);
        Result fruit2=DelDocx(Dname,ThesisName);
        if(fruit1.getCode().equals("0")&&fruit2.getCode().equals("0"))
            return Result.success();
        else
            return Result.error("-1","Session过期或者后端出问题了");
    }
    @PostMapping("/AddFile")//添加论文项目
    public Result<?> AddFile(@RequestParam("ThesisName") String ThesisName){
        System.out.println("AddFile");
        // 得到格式化后的日期
        String daytime = sdf.format(new Date());
        String hourtime=sdf2.format(new Date());
        String CreateTime=daytime+"-"+hourtime;
        // 时间 和 日期拼接
        String TableName = UUID.randomUUID().toString();
        String Address="C:/Project/user/";
        try{
            user u= (user) request.getSession().getAttribute("user");
            if(null==u){
                return Result.error("-1","Session过期");
            }
            String UserId=u.getUserId();
            Address=Address+UserId+"/"+TableName;
            String DocxName=UserId+"_"+ThesisName;
            Double neglect=0.98;
            Double Iteration=0.80;
            Double version=0.50;
            Integer remind=0;
            Integer bigversion=0;
            Integer smallversion=0;
            DocxVersion DV=new DocxVersion(DocxName,UserId,CreateTime,TableName,
                    Address,neglect,Iteration,version,remind,bigversion,smallversion);
            docxVersionmapper.AddFile(DV);
            docxVersionmapper.AddFileTable(TableName);
            File folder = new File(Address);
            if (!folder.exists()) {
                folder.mkdir();
            }
            return Result.success("创建成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("-1","失败,程序出问题了");
    }
    @PostMapping("/DelFile")//删除论文项目
    public Result<?> DelFile(@RequestParam("ThesisName") String ThesisName){
        System.out.println("DelFile");
        try{
            user u= (user) request.getSession().getAttribute("user");
            if(null==u){
                return Result.error("-1","Session过期");
            }
            String UserId=u.getUserId();
            String DocxName=UserId+"_"+ThesisName;
            Result SelFruit=SelectFile(ThesisName);
            DocxVersion DV=(DocxVersion) SelFruit.getData();
            if(SelFruit.getCode().equals("2")||SelFruit.getCode().equals("-1")){
                return Result.error("-1","未发现要删除的信息,或程序出现错误（小概率）");
            }
            docxVersionmapper.DelFile(DocxName);
            docxVersionmapper.DelFileTable(DV.getTableName());//需要提供表名要select
            //删除本机的有关该论文的所有文章
            //这点不能忘记做
            File file=new File(DV.getAddress());
            if (deleteFile(file)) {
                System.out.println("文件删除成功！");
            }
            return Result.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("-1","程序出问题了");
    }
    @PostMapping("/SelectFile")//查找该用户的论文项目下的某一版本
    public Result<?> SelectFile(@RequestParam("ThesisName") String ThesisName){
        System.out.println("SelectFile");
        try{
            user u= (user) request.getSession().getAttribute("user");
            if(null==u){
                return Result.error("-1","Session过期");
            }
            String UserId=u.getUserId();
            String DocxName=UserId+"_"+ThesisName;
            DocxVersion DV=docxVersionmapper.SelectFile(DocxName);
            if(DV==null){
                return Result.error("2","结果为空");
            }else{
                return Result.success(DV);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("-1","程序出问题了");
    }
    @PostMapping("/SelectAllFile")//查找该用户该版本论文下的所有版本
    public Result<?> SelectAllFile(){//什么的都不用传入
        System.out.println("SelectAllFile");
        try {
            user u = (user) request.getSession().getAttribute("user");
            if (null == u) {
                return Result.error("-1", "Session过期");
            }
            String UserId=u.getUserId();
            List<DocxVersion> DVL=docxVersionmapper.ListFile(UserId);
            for(DocxVersion d:DVL){
                d.setRemind(docxmapper.CountTable(d.getTableName()));
            }
            return Result.success(DVL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("-1","程序出问题了");
    }
    @PostMapping("/UpdateParameter")//修改版本概率
    public Result<?> UpdateParameter(@RequestParam("neglect") Double neglect,
                                     @RequestParam("Iteration") Double Iteration,
                                     @RequestParam("version") Double version,
                                     @RequestParam("ThesisName") String ThesisName){
        System.out.println("UpdateParameter");
        try{
            user u = (user) request.getSession().getAttribute("user");
            if (null == u) {
                return Result.error("-1", "Session过期");
            }
            String UserId=u.getUserId();
            String DocxMame=UserId+"_"+ThesisName;
            docxVersionmapper.UpdateParameter(neglect,Iteration,version,0.0,DocxMame);
            //注意修改后重新设置当前的论文Session值
            //一会做
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("-1","程序出问题了");
    }
    @PostMapping("/UpdateVersion")//修改版本号
    public Result<?> UpdateVersion(@RequestParam("BV") Integer BV,
                                   @RequestParam("SV") Integer SV,
                                   @RequestParam("ThesisName") String ThesisName){
        System.out.println("UpdateVersion");
        try {
            user u = (user) request.getSession().getAttribute("user");
            if (null == u) {
                return Result.error("-1", "Session过期");
            }
            String UserId = u.getUserId();
            String DocxMame = UserId + "_" + ThesisName;
            docxVersionmapper.SetVersion(BV,SV,DocxMame);
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("-1","程序出现问题了");
    }
    // TODO: 2023/4/6
    @PostMapping("/ComReports")
    public void GetComReports(HttpServletResponse response,@RequestParam("file") MultipartFile file,
                                   @RequestParam("ThesisName") String ThesisName) throws IOException {
        System.out.println("ComReports");
        InputStream inputStream = null;
        Result frult=SelectFile(ThesisName);
        if(frult.getCode().equals("-1")||frult.getCode().equals("2")){
            return ;
        }
        DocxVersion DV=(DocxVersion) frult.getData();
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getOriginalFilename(),
                        RequestBody.create(MediaType.parse(file.getContentType()), file.getBytes()))
                .addFormDataPart("Address",DV.getAddress())
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/chapter03/ComReports")
                .post(requestBody)
                .build();
        Response response1 = client.newCall(request).execute();
        inputStream = response1.body().byteStream();
//        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("fruit.xlsx", "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
        outputStream.close();
    }
    @PostMapping("/SetFileSession")//选择论文，存入session
    public Result<?> SetFileSession(@RequestParam("ThesisName") String ThesisName){//传论文项目的名称
        System.out.println("SetFileSession");
        Result result=SelectFile(ThesisName);
        DocxVersion DV=(DocxVersion) result.getData();
        request.getSession().setAttribute("DV",DV);
        return Result.success();
    }
    private Result<?> GetFileSession(){//什么都不用传
        System.out.println("GetFileSession");
        DocxVersion DV=(DocxVersion)request.getSession().getAttribute("DV");
        if(DV==null){
            return Result.error("-1","Session过期");
        }else {
            return Result.success(DV);
        }
    }
    @PostMapping("/GetFileSession")//刷新当前选中的论文项目的Session值，用于当用户修改概率后获取同步更新的session信息
    public Result<?> RefreshSession(){//什么都不用传
        System.out.println("RefreshSession");
        try{
            Result fruit=GetFileSession();
            DocxVersion DV;
            if(fruit.getCode().equals("-1")){
                return Result.error("-1","Session过期");
            }else
            {
                DV=(DocxVersion) fruit.getData();
            }
            String DocxName=DV.getDocxName();
            String ThesisName=DocxName.split("_")[1];
            Result fruit1=SetFileSession(ThesisName);
            if(fruit1.getCode().equals("0")){
                return GetFileSession();
            }else {
                return Result.error("-1","刷新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("-1","刷新失败");
    }
    private static Boolean deleteFile(File file) {//删除文件夹下所有文件，工具方法
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件是否存在以及文件路径是否正确");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFile(f);
            } else {
                //文件删除
                f.delete();
                //打印文件名
                System.out.println("文件名：" + f.getName());
            }
        }
        //文件夹删除
        file.delete();
        System.out.println("目录名：" + file.getName());
        return true;
    }
}
