package com.example.textspringboot.mapper;

import com.example.textspringboot.Entity.Docx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DocxMapper {
    public List<Docx> SelectByUser(@Param("userId") String userid,@Param("TableName")String TableName);
    public Docx SelectByOne(@Param("FileName") String FileName,@Param("TableName")String TableName);
    public Docx SelectNewFile(@Param("FileName") String FileName);
    public int CountTable(@Param("FileName") String FileName);
    public int AddDocx(@Param("docx") Docx docx,@Param("TableName")String TableName);
    public int DelDocx(@Param("FileName") String FileName,@Param("TableName")String TableName);
    public int UpdateAddress(@Param("FileName")String FileName, @Param("Address")String Address,@Param("TableName")String TableName);//上传文件专属
}
