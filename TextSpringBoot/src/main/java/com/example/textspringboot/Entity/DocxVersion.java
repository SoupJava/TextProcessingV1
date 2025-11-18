package com.example.textspringboot.Entity;

public class DocxVersion {
    private String UserId;//用户ID
    private String DocxName;//论文项目名称
    private String CreateTime;//创建时间
    private String TableName;//（数据库）论文存储表名
    private String Address;//（本机）存储地址
    private Double Neglect;//忽略率，默认98%
    private Double Iteration;//小版本率，默认80%
    private Double Version;//大版本率，默认50%
    private Integer Remind;//没用的东西，忘删了，留着以后用
    private Integer bigversion;//当前大版本号
    private Integer smallversion;//当前小版本号

    public DocxVersion(String userId, String docxName, String createTime, String tableName, String address, Double neglect, Double iteration, Double version, Integer remind, Integer BV, Integer SV) {
        UserId = userId;
        DocxName = docxName;
        CreateTime = createTime;
        TableName = tableName;
        Address = address;
        Neglect = neglect;
        Iteration = iteration;
        Version = version;
        Remind = remind;
        bigversion = BV;
        smallversion = SV;
    }

    public DocxVersion() {
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

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Double getNeglect() {
        return Neglect;
    }

    public void setNeglect(Double neglect) {
        Neglect = neglect;
    }

    public Double getIteration() {
        return Iteration;
    }

    public void setIteration(Double iteration) {
        Iteration = iteration;
    }

    public Double getVersion() {
        return Version;
    }

    public void setVersion(Double version) {
        Version = version;
    }

    public Integer getRemind() {
        return Remind;
    }

    public void setRemind(Integer remind) {
        Remind = remind;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public Integer getBV() {
        return bigversion;
    }

    public void setBV(Integer BV) {
        this.bigversion = BV;
    }

    public Integer getSV() {
        return smallversion;
    }

    public void setSV(Integer SV) {
        this.smallversion = SV;
    }

    @Override
    public String toString() {
        return "DocxVersion{" +
                "UserId='" + UserId + '\'' +
                ", DocxName='" + DocxName + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", TableName='" + TableName + '\'' +
                ", Address='" + Address + '\'' +
                ", Neglect=" + Neglect +
                ", Iteration=" + Iteration +
                ", Version=" + Version +
                ", Remind=" + Remind +
                ", BV=" + bigversion +
                ", SV=" + smallversion +
                '}';
    }
}
