package com.example.textspringboot.Entity;

public class Docx {
    private String UserId;//用户ID
    private String DocxName;//论文项目名称
    private String FileName;//版本名
    private String UploadTime;//上传时间
    private String Address;//存储地址

    public Docx(String userId, String docxName, String fileName, String uploadTime, String address) {
        UserId = userId;
        DocxName = docxName;
        FileName = fileName;
        UploadTime = uploadTime;
        Address = address;
    }

    public Docx() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDocxName() {
        return DocxName;
    }

    public void setDocxName(String docxName) {
        DocxName = docxName;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    @Override
    public String toString() {
        return "Docx{" +
                "UserId='" + UserId + '\'' +
                ", DocxName='" + DocxName + '\'' +
                ", FileName='" + FileName + '\'' +
                ", UploadTime='" + UploadTime + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
