<template>
  <div class="fileCompose">
    <div class="filebox">
      <el-upload
          drag
          ref="upload1"
          class="upload-demo"
          action="no"
          :before-upload="beforeUpload"
          name="File"
          multiple
          :limit="1"
          :file-list="fileList1"
          :auto-upload="false"
          :http-request="uploadFile2Back1">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传excel(.xlsx)文件</div>
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
        <div slot="tip" class="el-upload__tip">任何文件</div>
      </el-upload>
    </div>
    <div class="inputAndBtn">
      <el-input-number class="btn" v-model="num" :min="0" :max="100" label="描述文字"></el-input-number>
      <el-button type="primary" class="btn"  @click="startUpload" v-loading.fullscreen.lock="fullscreenLoading" element-loading-text="拼命加载中" style="">提交</el-button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "FileCreat",
  data() {
    return {
      num : 0,
      fullscreenLoading: false,
      textarea: '',
      fileList1: [],
      fileList2: [],
      text: "",
      file : {
        file1 : '',
        file2 : '',
        num : 0
      },
      dataf:new FormData()
    }
  },
  methods: {
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
      this.dataf.append("File",this.file.file1)

      // axios.post('http://localhost:8080/toPdf', dataf, {
      //   headers: {
      //     'Content-Type': 'application/form-data; charset=utf-8'
      //   },
      //   responseType: 'blob'
      // }).then(
      //     res => {
      //       this.fileList = []
      //       const content = res.data
      //       const content1 = res.request.getResponseHeader('Content-Disposition');
      //       const fileName = content1 && content1.split(';')[1].split('filename=')[1];
      //       console.log(fileName)
      //       this.downloadFile(content, content.type, fileName)
      //     }
      // );
    },
    uploadFile2Back2(op) {
      setTimeout(() => {
        console.log('===============>>>>>>>>>>>2_')
        console.log(op)
        this.openFullScreen()
        this.file.file2 = op.file;
        this.dataf.append("ExcelFile",this.file.file2)
        this.dataf.append("copyNum",this.num)
        this.uploadFileFun()
      },120)
    },
    uploadFileFun(){
      axios.post('http://localhost:8080/fileCopy', this.dataf, {
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
  }
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
  text-align: center;
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
    margin: 8vh 2vw 0 2vw;
    padding-bottom: 5vh;
    text-align: center;
    .input {
      margin: 0 10vw;
      width: 200vw;
    }
    .btn{
      margin: 0 10vw;
      width: 10vw;
    }
  }
}
</style>