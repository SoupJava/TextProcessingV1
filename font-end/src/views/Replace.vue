<template>
<div class="replace">
  <el-form ref="form" :model="sizeForm" label-width="80px" size="mini" class="form">
    <el-form-item label="原文内容 :">
      <el-input v-model="sizeForm.text"
                type="textarea"
                :autosize="{ minRows: 7, maxRows: 7}"
                @change ="keepTxt"
      ></el-input>
    </el-form-item>
    <el-form-item label="对方性别 :">
      <el-radio-group v-model="sizeForm.gender" size="medium" style="float: left">
        <el-radio-button border label="男" value=1></el-radio-button>
        <el-radio-button border label="女" value=0></el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="对方辈分 :">
      <el-radio-group v-model="sizeForm.position" size="medium" style="float: left">
        <el-radio-button border label="平辈"></el-radio-button>
        <el-radio-button border label="长辈"></el-radio-button>
        <el-radio-button border label="晚辈"></el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="对方身份 :">
      <el-radio-group v-model="sizeForm.status" size="medium" style="float: left;">
        <el-radio class="radioBtn" border label="老师"></el-radio>
        <el-radio class="radioBtn" border label="师兄/姐"></el-radio>
        <el-radio class="radioBtn" border label="父母"></el-radio>
        <el-radio class="radioBtn"  border label="其他"></el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item size="large">
      <el-button type="primary" @click="onSubmit" style="width: 10vw">提交</el-button>
    </el-form-item>
  </el-form>
  <div class="result">
    <el-input v-model="text"
              type="textarea"
              readonly
              :autosize="{ minRows: 7, maxRows: 7}"
              style="color: #333333;font-weight: bold"
    ></el-input>
  </div>
</div>
</template>

<script>
import axios from "axios";
import qs from "qs";
import {keepValue} from "@/utils/user"

export default {
  name: "Replace",
  data() {
    return {
      text : "",
      humble : keepValue.humble,
      sizeForm: {
        text: '',
        gender : '',
        status : '',
        position : ''
      },
      data : {
        word : '' ,
        sex : '' ,
        identity : '' ,
        Generation : ''
      }
    }
  },
  mounted() {
    this.keepMyValue()
    },
  methods:{
    keepMyValue(){
      this.sizeForm.text = this.humble === "" ? "" : this.humble
    },
    keepTxt(){
      keepValue.humble = this.sizeForm.text
    },
    /**
     * 判断内容是否有空
     */
    toCheck(){
      if(this.sizeForm.text === ''){
        this.$message.warning('无法提交空内容')
        return false
      }else
      if(this.sizeForm.gender === ''){
        this.$message.warning('请选择对方性别')
        return false
      }else
      if(this.sizeForm.position === ''){
        this.$message.warning('请选择对方辈分')
        return false
      }else
      if(this.sizeForm.status === ''){
        this.$message.warning('请选择对方身份')
        return false
      }else {
        return true
      }
    },
    /**
     * 数据处理
     * @returns {{}}
     */
    setData(){
      this.data.word = this.sizeForm.text
      this.data.sex = this.sizeForm.gender === '男'
      this.chooseIdentity()
      this.chooseStatus()
    },
    /**
     * 映射身份和数字
     * @returns {string}
     */
    chooseStatus(){
      console.log("this.sizeForm.position : ",this.sizeForm.position )
      if(this.sizeForm.position === "平辈") this.data.Generation =  '0'
      if(this.sizeForm.position === "长辈") this.data.Generation =  '1'
      if(this.sizeForm.position === "晚辈") this.data.Generation =  '2'
    },
    /**
     * 映辈分份和数字
     * @returns {string}
     */
    chooseIdentity(){
      if(this.sizeForm.status === "老师") this.data.identity =  '0'
      if(this.sizeForm.status === "师兄/姐") this.data.identity =  '1'
      if(this.sizeForm.status === "父母") this.data.identity =  '2'
      if(this.sizeForm.status === "其他") this.data.identity =  '3'
    },
    /**
     * 提交
     */
    onSubmit() {
      if(!this.toCheck()) return
      this.setData()
      //TODO 发送数据

      const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        data: qs.stringify(this.data),
        url :"http://localhost:8080/replace" ,
      };
      axios(options).then((res) => {
        this.text = res.data.data.split(" ")[1].split('"')[0]
      })
    },
  }
}
</script>

<style scoped lang="less">
.replace{
  margin: 1vw 3vw;
  text-align: center;
  .inputNode{
    width: 70vw;
  }
  .form{
    width: 60vw;
    padding: 2vh;
    margin-top: 5vh;
    background-color: white;
    border-radius: 0 15px 15px 0;
    border: solid 1px gray;
    .radioBtn{
    margin:0 1vw 0 0;
    }
  }
  .result{
    width: 62vw;
    margin-top: 5vh;
  }
}
</style>