<template>
<div class="compare">
  <div class="header" v-if="this.isLogin">
    <div v-if="this.haveProjectSession">
      第一篇文章 ： <el-cascader style="margin-right: 5vw" :options="options1" clearable @change="selectChange1">
      <template slot-scope="{ node, data }">
        <span>{{ data.label }}</span>
        <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
      </template>
    </el-cascader>
      第二篇文章 ： <el-cascader :options="options2" clearable @change="selectChange2">
      <template slot-scope="{ node, data }">
        <span>{{ data.label }}</span>
        <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
      </template>
    </el-cascader>
    </div>
  </div>
  <div class="input">
    <el-input
        type="textarea"
        :autosize="{ minRows: 36, maxRows: 36}"
        class="input1"
        placeholder="请输入内容"
        @change ="keepTxt"
        v-model="text1"
    >
    </el-input>
    <div class="input2" v-html="diff">
    </div>
    <el-input
        type="textarea"
        :autosize="{ minRows: 36, maxRows: 36}"
        class="input3"
        @change ="keepTxt"
        v-model="text2">
    </el-input>
  </div>
</div>
</template>

<script>
import { diff_match_patch } from 'diff-match-patch';
import {keepValue} from "@/utils/user"
import {getUser} from "@/utils/testDataUtils";
import axios from "axios";
import qs from "qs";

export default {
  name: "Compare",
  data(){
    return{
      haveProjectSession :true,
      isLogin : false,
      articleName : "",
      text1: '',
      text2: '',
      diff : '',
      keppText1 : keepValue.replace1,
      keppText2 : keepValue.replace2,
      options1: [   //第1个框框的内容
        {
          value: 'article1',
          label: '我要上天'
        },
        {
          value: 'article2',
          label: '我要下海'
        },
        {
          value: 'article3',
          label: '我既要上天还要下海'
        }
      ],
      options2: [   //第2个框框的内容
        {
          value: 'article1',
          label: '文章1'
        },
        {
          value: 'article2',
          label: '文章2'
        },
        {
          value: 'article3',
          label: '文章3'
        }
      ]
    }
  },
  mounted() {
    this.keepMyValue()
    this.isLoginFun()
    this.haveProjectSessionFun()
    },
  methods:{
    /**
     * 判断用户是否登录
     */
    async isLoginFun(){
      this.isLogin =  await getUser() === 1
      if(this.isLogin){
        // todo 获取文章信息
      }
    },
    /**
     * 判断用户是否选中了某个文章项目
     */
    haveProjectSessionFun(){
      axios.post("http://localhost:8080/getSelect").then(res => {
        // res.data.data.docxName.split("_")[1]
        if(res.data.msg !== "Session过期"){  //有项目选中
          this.haveProjectSession = true
          this.articleName = res.data.data.docxName.split("_")[1]
          this.getProjectInfo(this.articleName)
        }else {
          this.haveProjectSession = false
        }

      })
    },
    /**
     * 获取被选中项目的信息
     */
    getProjectInfo(name){
      // getSelectedProjectInfo
      // console.log('=-=-=-=-=-=-=-=-=-=')
      // console.log(name)
      this.options1 = []
      this.options2 = []
      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({"ThesisName" : name}),
        url :"http://localhost:8080/getOneProject" ,
      };
      axios(options).then((res) => {
        if(res.data.msg === "成功"){
          console.log(res.data.data)
          for(let i = 0 ; i < res.data.data.length ; i++){
            // res.data.data[i].fileName
            this.options1.push({value: res.data.data[i].fileName,label: res.data.data[i].fileName})
            this.options2.push({value: res.data.data[i].fileName,label: res.data.data[i].fileName})
          }
        }else {
          this.$message.error("信息获取失败")
        }
      })
    },
    /**
     * 下拉列表选中事件
     */
    selectChange1(e){
      // TODO
      if(e.length === 0){
        // code
        this.text1 = ''
      }else {
        this.getTextContent(e,1)
        // // code
        // this.text1 = e
        // this.text1 += "  "
      }
    },
    selectChange2(e){
      // TODO
      if(e.length === 0){
        // code
        this.text2 = ''
      }else {
        this.getTextContent(e,2)
        // alert(e)
        this.text2 = e
        this.text2 += "  "
      }
    },
    /**
     * 获取选中文章内容
     */
    getTextContent(name,num){
      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({versionName:String(name)}),
        url :"http://localhost:8080/getArticleContent" ,
      };
      axios(options).then((res) => {
        if(res.data.msg === "成功"){
          this[`text${num}`] = res.data.data
        }else {
          this.$message.error("文章信息获取失败")
        }
      })
    },
    /**
     * 存储当前值
     */
    keepMyValue(){
      this.text1 = this.keppText1 === "" ? "这是测试效果啊" : this.keppText1
      this.text2 = this.keppText2 === "" ? "这是一个测试效果" : this.keppText2
    },
    keepTxt(){
      keepValue.replace1 = this.text1
      keepValue.replace2 = this.text2
    },
    compare() {
      const dmp = new diff_match_patch();
      const diffs = dmp.diff_main(this.text1, this.text2);
      dmp.diff_cleanupSemantic(diffs);
      this.diff = dmp.diff_prettyHtml(diffs);
    },
    submit(){
      let text = this.textarea1.replace(/^\s*|\s*$/g,'')
      if(text !== ''){
        // TODO 向后端发送请求
        this.textarea2 = this.textarea3 = text
      }else {
        this.$message({
          message : " 无法提交空内容",
          type : 'warning'
        })
        this.textarea1 = ''
      }
    },
  },
  watch: {
    text1: 'compare',
    text2: 'compare',
  },
}
</script>

<style scoped lang="less">
.compare{
  .header{
    height: 5vh;
    margin-top: 2vh;
  }
  .el-input__inner{
    overflow-x: auto;
  }
  padding: 0 2vw;
  .input{
    display: flex;
    width: 100%;
    margin-top: 3vh;
    .input1{
      flex: 8;
      margin: 0 1vw;
      overflow-x: auto;
      //border: solid 2px #333333;
      border-top: solid 2px #333333;
      border-left: solid 2px #333333;
      border-right: solid 2px #333333;
    }
    .input2{
      flex: 10;
      margin: 0 1vw;
      border: solid 2px #333333;
      width: 33%;
      height: 80vh;
      background-color: white;
      overflow-y: auto; /* 显示垂直滚动条 */
      padding: 1vh 1vw;
      border-radius: 15px;
    }
    .input3{
      flex: 8;
      margin: 0 1vw;
      border-top: solid 2px #333333;
      border-left: solid 2px #333333;
      border-right: solid 2px #333333;
    }
  }
}
</style>