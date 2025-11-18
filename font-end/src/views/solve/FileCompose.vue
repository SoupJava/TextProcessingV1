<template>
  <div class="fileCompose">
    <div class="filebox">
      <el-upload
          drag
          ref="upload1"
          class="upload-demo"
          action="no"
          :before-upload="beforeUpload"
          name="DocxFile"
          multiple
          :limit="1"
          :file-list="fileList1"
          :auto-upload="false"
          :http-request="uploadFile2Back1">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传world(.docx)文件</div>
      </el-upload>
      <el-upload
          drag
          ref="upload2"
          class="upload-demo"
          action="no"
          :before-upload="beforeUpload"
          name="ExcelFile"
          multiple
          :limit="1"
          :file-list="fileList2"
          :auto-upload="false"
          :http-request="uploadFile2Back2">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传excel(.xlsx)文件</div>
      </el-upload>
    </div>
    <div class="inputAndBtn">
      <el-input
          class="input"
          placeholder="请输入内容"
          style="width: 10vw"
          v-model="input"
          clearable>
      </el-input>
      <el-button type="primary" class="btn"@click="startUpload" v-loading.fullscreen.lock="fullscreenLoading" element-loading-text="拼命加载中">提交</el-button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "FileCompose",
  data(){
    return{
      fullscreenLoading: false,
      input:'number',
      fileList1: [],
      fileList2: [],
      file : {
        file1 : '',
        file2 : '',
        num : 0
      },
      dataf:new FormData()
    }
  },
  methods:{
    openFullScreen() {
      this.fullscreenLoading = true;
    },
    /**
     * 上传按钮点击事件
     */
    startUpload() {
      this.$refs.upload1.submit();
      this.$refs.upload2.submit();
    },
    /**
     * 上传按钮点击之后要发生的事件
     * @param op
     */
    uploadFile2Back1(op) {
      console.log('===============>>>>>>>>>>>1_')
      console.log(op)
      this.file.file1 = op.file;
      this.dataf.append("DocxFile",this.file.file1)
    },
    uploadFile2Back2(op) {
      setTimeout(() => {
        console.log('===============>>>>>>>>>>>2_')
        console.log(op)
        this.openFullScreen()
        this.file.file2 = op.file;
        this.dataf.append("ExcelFile",this.file.file2)
        this.dataf.append("control",this.num)
        this.uploadFileFun()
      },120)
    },
    uploadFileFun(){
      axios.post('http://localhost:8080/fileBat', this.dataf, {
        headers: {
          'Content-Type': 'application/form-data; charset=utf-8'
        },
        responseType: 'blob'
      }).then(
          res => {
            this.fileList = []
            const content = res.data
            const content1 = res.request.getResponseHeader('Content-Disposition');
            const fileName = content1 && content1.split(';')[1].split('filename=')[1];
            console.log(fileName)
            this.downloadFile(content, content.type, fileName)
          }
      );
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
      downloadElement.download = fileName;
      document.body.appendChild(downloadElement);
      // 点击下载
      downloadElement.click();
      // 下载完成移除元素
      document.body.removeChild(downloadElement);
      // 释放掉blob对象
      window.URL.revokeObjectURL(href);
    },
    beforeUpload(file) {
      if (file.name.split(".")[1] !== 'docx' && file.name.split(".")[1] !== 'ppt' && file.name.split(".")[1] !== 'xlsx') {
        this.$message.error('只能上传word(.docx),ppt(.ppt),excel(.xlsx)文件!');
        return false;
      }
      return true
    },
  },
}
</script>

<style scoped lang="less">
.fileCompose{
  margin: 15vh auto 0 auto;
  background-color: #7f8fa67D;
  width: 70vw;
  padding-top: 2vh;
  padding-left: 3vw;
  padding-right: 3vw;
  border-radius: 25px;
  .filebox{
    display: flex;
    margin: 8vh 2vw 0 2vw;
    text-align: center;
    .file{
      flex: 1;
    }
    .upload-demo{
      margin: 0 5vw;
    }
  }
  .inputAndBtn{
    display: flex;
    margin: 8vh 2vw 0 2vw;
    padding-bottom: 5vh;
    text-align: center;
    .input {
      flex: 2;
      margin: 0 5vw;
    }
    .btn{
      flex: 1;
      width: 3vw;
      margin: 0 8vw;
    }
  }
}
</style>