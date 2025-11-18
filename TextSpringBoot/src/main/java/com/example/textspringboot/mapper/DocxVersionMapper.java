package com.example.textspringboot.mapper;

import com.example.textspringboot.Entity.DocxVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DocxVersionMapper {
    public int AddFile(DocxVersion Dv);
    public int AddFileTable(String TableName);
    public int DelFile(String DocxName);
    public int DelFileTable(String TableName);
    public List<DocxVersion> ListFile(String UserId);
    public DocxVersion SelectFile(String FileName);
    public int UpdateParameter(@Param("neglect") Double neglect,@Param("Iteration") Double Iteration,@Param("version") Double version,@Param("remind") Double remind,@Param("DocxName") String DocxName);
    public int SetVersion(@Param("BV") Integer BV,@Param("SV") Integer SV,@Param("DocxName") String DocxName);
}
