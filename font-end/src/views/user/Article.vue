<template>
<div class="article">
  <el-input
      placeholder="请输入内容"
      style="width: 25vw ; margin-top: 5vh; margin-left: 3vw;"
      v-model="input"
      clearable>
    <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
  </el-input>
  <el-button icon="el-icon-refresh" @click="init"></el-button>
  <el-popover
      placement="bottom-start"
      trigger="click"
      style="margin-right: 5vw">
    <el-upload
        drag
        ref="upload"
        class="upload-demo"
        action="no"
        :before-upload="beforeUpload"
        name="file"
        multiple
        :limit="1"
        :file-list="fileList"
        :auto-upload="false"
        :http-request="uploadFile2Back">
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div slot="tip" class="el-upload__tip">只能上传word(.docx)文件</div>
    </el-upload>
    <el-button  type="primary" @click="upload">确定</el-button>
    <el-button slot="reference" type="primary"style="margin-left: 2vw; width: 8vw;">上传</el-button>
    <el-button slot="reference" type="danger"style="margin-left: 2vw; width: 8vw;" @click="delThisArticle">删除</el-button>
  </el-popover>
  <el-switch
      style="background-color: rgb(245, 247, 250);height: 3em;padding: 0 1vw;border-radius: 5px"
      v-model="selectValue"
      active-text="选中本项目"
      inactive-text="不选中本项目"
      active-color="rgb(64, 158, 255)"
      inactive-color="#ff4949"
      @change="selectThisProject">
  </el-switch>

  <div class="number" style="margin-left: 3vw;margin-top: 2vh;margin-bottom: 2vh">
    <el-input-number v-model="numbers.num.num1" @change="handleChange" :min="numbers.min" :max="numbers.num.num2 - 1"></el-input-number>&nbsp;&nbsp;&nbsp;
    <el-input-number v-model="numbers.num.num2" @change="handleChange" :min="numbers.num.num1 + 1" :max="numbers.num.num3 - 1"></el-input-number>&nbsp;&nbsp;&nbsp;
    <el-input-number v-model="numbers.num.num3" @change="handleChange" :min="numbers.num.num2 + 1" :max="numbers.max"></el-input-number>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    <el-tooltip class="item" effect="dark" content="生成大版本，小版本和禁止上传的分界线" placement="right">
      <i class="el-icon-question" style="cursor: pointer;"></i>
    </el-tooltip>
    <el-button type="primary"style="margin-left: 1vw;" @click="saveConfig">保存配置信息</el-button>

    <el-popover
        placement="bottom-start"
        trigger="click"
        style="margin-right: 5vw;">
      <el-upload
          drag
          ref="uploadReport"
          class="upload-demo"
          action="no"
          :before-upload="beforeUpload"
          name="file"
          multiple
          :limit="1"
          :file-list="fileListComReports"
          :auto-upload="false"
          :http-request="uploadFile2BackComReports">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传word(.docx)文件</div>
      </el-upload>
      <el-button  type="primary" @click="uploadReport" v-loading.fullscreen.lock="fullscreenLoading" element-loading-text="拼命加载中">确定</el-button>
      <el-button slot="reference" type="primary"style="margin-left: 2vw;margin-top: 2vh;">生成相似度对比报告</el-button>
    </el-popover>
  </div>
  <el-table
      :data="tableData"
      border
      stripe
      style="width: 80%;margin-top: 3vh; margin-left: 3vw"
      height="550">
    <el-table-column
        fixed
        prop="date"
        label="日期"
        width="250">
    </el-table-column>
    <el-table-column
        prop="name"
        label="版本号"
        width="400">
    </el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button
            size="mini"
            type="primary"
            @click="downLoad(scope.$index)">下载</el-button>
        <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index)">删除</el-button>
      </template>

    </el-table-column>
  </el-table>
</div>
</template>

<script>
import axios from "axios";
import qs from "qs";

export default {

  name: "Article",
  mounted() {
    this.init()
    },
  data(){
    return{
      fullscreenLoading : false,
      num: 1,
      input: '',
      tableData: [],
      data :[],
      fileList : [],
      fileListComReports : [],
      thisPageArticle : '', //本页面中的文章列表的名称
      numbers : {
        num : {
          num1 : 15,
          num2 : 55,
          num3 : 75
        },
        max : 98,
        min : 5
      },
      selectValue: true
    }
  },
  methods:{
    /**
     * 初始化数组
     */
    init(){
      this.fullscreenLoading = false
      this.thisPageArticle = this.$route.query.article
      this.initPercentage()
      this.isSelect()
      this.tableData = []
      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({"ThesisName" : this.thisPageArticle}),
        url :"http://localhost:8080/getOneProject" ,
      };
      axios(options).then((res) => {
        if(res.data.msg === "成功"){
            for(let i = 0 ; i < res.data.data.length ; i ++){
              let obj = {
                date : res.data.data[i].uploadTime,
                name : res.data.data[i].fileName
              }
              this.tableData.push(obj)
            }
        }else {
          this.$message.error("信息获取失败")
        }
      })
    },
    /**
     * 初始化百分比信息
     */
    initPercentage(){
      this.numbers.num.num1 = this.$route.query.bate1 * 100
      this.numbers.num.num2 = this.$route.query.bate2 * 100
      this.numbers.num.num3 = this.$route.query.bate3 * 100
    },
    /**
     * 删除本集合
     */
    delThisArticle(){
      // deleteProject
      // this.$message.error(`删除 : ${this.thisPageArticle}`)
      this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const options = {
          method: 'POST',
          headers: { 'content-type': 'application/x-www-form-urlencoded'},
          data: qs.stringify({"ThesisName" : this.thisPageArticle}),
          url :"http://localhost:8080/deleteProject" ,
        };
        axios(options).then((res) => {
          if(res.data.msg === "成功"){
              this.$message({
                message:"删除成功",
                type:"success"
              })
            this.$router.push({path:"articlelist"})
          }else {
            this.$message.error("删除失败")
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /**
     * 保存项目配置信息
     */
    saveConfig(){
      // this.numbers.num.num1
      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({
          "neglect" : this.numbers.num.num3 / 100,
          "Iteration" : this.numbers.num.num2 / 100,
          "version" : this.numbers.num.num1 / 100,
          "ThesisName" : this.thisPageArticle,
        }),
        url :"http://localhost:8080/saveConfig" ,
      };
      axios(options).then((res) => {
        if(res.data.msg === "成功"){
          this.$message({
            message : "修改成功",
            type:"success",

          })
        }else {
          this.$message.error("修改失败")
        }

      })
    },
    /**
     * 选中该项目
     */
    selectThisProject(){
      if(!this.selectValue){
        this.$message({
          message:"选中其他项目，该项目会自动取消选中",
          type : "info"
        })
      }
      this.selectValue = true
      if(this.selectValue){   //当前值变为真------------选中
        const options = {
          method: 'POST',
          headers: { 'content-type': 'application/x-www-form-urlencoded'},
          data: qs.stringify({"ThesisName" : this.thisPageArticle,}),
          url :"http://localhost:8080/setSelect" ,
        };
        axios(options).then(res => {
          if(res.data.msg === "成功"){
            this.selectValue = true
          }else {
            this.$message.error("选择失败")
          }

        })
      }
    },
    /**
     * 判断该项目是否被选中
     */
    isSelect(){
      axios.post("http://localhost:8080/getSelect").then(res => {
        if(res.data.msg === "Session过期" || res.data.data.docxName.split("_")[1] !== this.thisPageArticle){  //未选中该项目
          this.selectValue = false
        }else {
          this.selectValue = true
        }

      })
    },
    openFullScreen() {
      this.fullscreenLoading = true;
    },
    /**
     * 计数器
     * @param value
     */
    handleChange(value) {
      console.log(value);
    },
    beforeUpload(file) {
      if (file.name.split(".")[1] !== 'docx') {
        this.$message.error('上传文件只能是docx格式!');
        return false;
      }
      return true
    },
    uploadFile2Back(op) {
      // this.openFullScreen()
      let file = op.file;
      let fileName = op.file.name;
      let dataf = new FormData();
      dataf.append('ThesisName', String(this.thisPageArticle))
      dataf.append('file',file)
      // console.log('上传文件的信息')
      axios.post(`http://localhost:8080/projectAddFile`, dataf, {
        headers: {
          'Content-Type': 'application/form-data; charset=utf-8'
        },
      }).then((res) => {
        let text
        if(res.data.data[2] === "toosmall"){
          text = "高"
        }else if(res.data.data[2] === "toobig"){
          text = '低'
        }
        if(res.data.msg === "成功"){ //通过判断来显是否有弹窗
          this.$confirm(`文件相似的太${text}, 是否继续上传?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$message({
              type: 'success',
              message: '上传成功!'
            });
            this.init()
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '上传已取消'
            });
            // TODO 文件不上传需要调用的接口
            const options = {
              method: 'POST',
              headers: { 'content-type': 'application/x-www-form-urlencoded' },
              data: qs.stringify({BV:res.data.data[0],SV:res.data.data[1],ThesisName:this.thisPageArticle,name:res.data.data[3]}),
              url :"http://localhost:8080/backUpload" ,
            };
            axios(options).then((res) => {
              if(res.data.msg === "成功"){
                console.log('已删除已经上传的文件')
                this.init()
              }else {
                this.$message.error("出错!")
                this.init()
              }
            })
          })
        }else {
          this.init()
        }
      })
    },
    uploadFile2BackComReports(op) {
      this.openFullScreen()
      let file = op.file;
      let fileName = op.file.name;
      let dataf = new FormData();
      dataf.append("file",file)
      dataf.append("ThesisName", String(this.thisPageArticle))
      // console.log('上传文件的信息')
      axios.post(`http://localhost:8080/createReport`, dataf, {
        headers: {
          'Content-Type': 'application/form-data; charset=utf-8'
        },
        responseType: 'blob'
      }).then((res) => {
        this.fileListComReports = []
        const content = res.data
        const content1 = res.request.getResponseHeader('Content-Disposition');
        const fileName = content1 && content1.split(';')[1].split('filename=')[1];
        // console.log(fileName)
        this.downloadFile(content, content.type, fileName)
      })
    },
    /**
     * 搜索框
     */
    search(){
      if(this.input !== ''){
        const options = {
          method: 'POST',
          headers: { 'content-type': 'application/x-www-form-urlencoded'},
          data: qs.stringify({name : this.input,ThesisName : String(this.thisPageArticle)}),
          url :"http://localhost:8080/getFile" ,
        };
        axios(options).then((res) => {
          if(res.data.msg === "成功"){
            this.tableData = []
            this.tableData.push({
              date : res.data.data.uploadTime,
              name : res.data.data.fileName
            })
          }else {
            this.$message.error("查询失败")
          }
        })
      }else {
        this.$message({
          message:"搜索内容为空",
          type : "info"
        })
      }
    },
    /**
     * 上传文章
     */
    upload(){
      this.$message({
        message:"文件上传",
        type : "info"
      })
      this.$refs.upload.submit()
    },
    uploadReport(){
      this.$refs.uploadReport.submit()
    },
    /**
     * 删除文件
     */
    handleDelete(index) {
    //  this.tableData[index].name
      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({ThesisName:this.thisPageArticle,name : this.tableData[index].name}),
        url :"http://localhost:8080/projectdeleteFile" ,
      };
      axios(options).then((res) => {
        this.init()
      })
    },
    /**
     * 下载文件
     */
    downLoad(index){
      // console.log({ThesisName:this.thisPageArticle,name : this.tableData[index].name})
      this.$message({
        message : "下载文件为 : " + this.tableData[index].name,
        type : "info"
      })
      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded'},
        responseType: 'blob',
        data: qs.stringify({ThesisName:this.thisPageArticle,name : this.tableData[index].name}),
        url :"http://localhost:8080/projectDownload",
      };
      axios(options).then(res => {
        this.fileList = []
        const content = res.data
        const content1 = res.request.getResponseHeader('Content-Disposition');
        const fileName = content1 && content1.split(';')[1].split('filename=')[1];
        this.downloadFile(content, content.type, fileName)
      })
    },
    downloadFile(data, type, fileName) {
      this.fullscreenLoading = false;
      let blob = new Blob([data], {type: `application/${type};charset=utf-8`});
      // 获取heads中的filename文件名
      let downloadElement = document.createElement('a');
      // 创建下载的链接
      let href = window.URL.createObjectURL(blob);
      downloadElement.href = href;
      // 下载后文件名
      downloadElement.download = decodeURI(fileName);
      document.body.appendChild(downloadElement);
      // 点击下载
      downloadElement.click();
      // 下载完成移除元素
      document.body.removeChild(downloadElement);
      // 释放掉blob对象
      window.URL.revokeObjectURL(href);
    },
  },
}
</script>

<style scoped>

</style>