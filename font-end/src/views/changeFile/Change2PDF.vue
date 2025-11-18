<template>
  <div class="Change2PDF">
    <div class="now">
      <div class="class">
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
          <div slot="tip" class="el-upload__tip">只能上传word(.docx),ppt(.ppt),excel(.xlsx),压缩(.zip)文件</div>
        </el-upload>
        <el-button type="primary" class="uploadBtn"  @click="startUpload" v-loading.fullscreen.lock="fullscreenLoading" element-loading-text="拼命加载中">上传</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Change2PDF",
  data() {
    return {
      fullscreenLoading: false,
      textarea: '',
      fileList: [],
      text: "",
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
      this.$refs.upload.submit();
    },
    /**
     * 上传按钮点击之后要发生的事件
     * @param op
     */
    uploadFile2Back(op) {
      this.openFullScreen()
      let file = op.file;
      let fileName = op.file.name;
      let dataf = new FormData();
      dataf.append('file', file, fileName)
      axios.post("http://localhost:8080/toPdf", dataf, {
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
      console.log(file)
      console.log('========>>>>>>>>>>>>>>>')
      console.log(file.name.split(".")[1])
      if (file.name.split(".")[1] !== 'docx' && file.name.split(".")[1] !== 'ppt' && file.name.split(".")[1] !== 'xlsx' && file.name.split(".")[1] !== 'zip') {
        this.$message.error('只能上传word(.docx),ppt(.ppt),excel(.xlsx),压缩(.zip)文件!');
        return false;
      }
      return true
    },
  }
}
</script>

<style scoped lang="less">
.Change2PDF {
  .now {
    margin: 1vw auto;
    text-align: center;
    background-color:  #7f8fa67D;
    width: 30vw;
    padding-top: 3vh;
    border-radius: 25px;
    .upload-demo{
      margin-top: 10vh;
    }
    .uploadBtn{
      margin-top: 10vh;
      width: 7vw;
      margin-bottom: 2vh;
    }
  }
}

</style>