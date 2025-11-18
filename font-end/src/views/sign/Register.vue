<template>
  <div class="homepage-hero-module">
    <div class="video-container">
      <div :style="fixStyle" class="filter">
        <div class="content">
          <br>
          <p class="title">欢迎注册</p>
          <el-input
              placeholder="请输入昵称"
              v-model="info.nickName"
              clearable>
          </el-input>
          <br><br><el-input
            placeholder="请输入账号"
            v-model="info.account"
            maxlength="13"
            clearable>
        </el-input>
          <br><br>
          <el-input
              placeholder="请输入密码"
              show-password
              v-model="info.password"
              clearable>
          </el-input><br><br>
          <el-input
              placeholder="请再次输入密码"
              show-password
              v-model="info.rePassword"
              clearable>
          </el-input>
          <br><br>
          <div class="btn">
            <el-button type="primary" @click="registerBtn">注册</el-button>
            <el-button type="primary" @click="loginBtn">登录</el-button>
          </div>
        </div>
      </div>
      <video :style="fixStyle" autoplay loop muted class="fillWidth" v-on:canplay="canplay">
        <source src="../../../public/video/BG.mp4" type="video/mp4"/>
      </video>
    </div>
  </div>

</template>

<script>
import {nameReg,accountReg,passwordReg} from "@/utils/regUtils"
import axios from "axios";


export default {
  name: "Register",
  data(){
    return{
      vedioCanPlay: false,
      fixStyle: '',
      info : {
        nickName : '',
        account: '',
        password : '',
        rePassword : ''
      }
    }
  },
  mounted() {
    sessionStorage.removeItem("user")

    window.onresize = () => {
      const windowWidth = document.body.clientWidth
      const windowHeight = document.body.clientHeight
      const windowAspectRatio = windowHeight / windowWidth
      let videoWidth
      let videoHeight
      if (windowAspectRatio < 0.5625) {
        videoWidth = windowWidth
        videoHeight = videoWidth * 0.5625
        this.fixStyle = {
          height: windowWidth * 0.5625 + 'px',
          width: windowWidth + 'px',
          'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
          'margin-left': 'initial'
        }
      } else {
        videoHeight = windowHeight
        videoWidth = videoHeight / 0.5625
        this.fixStyle = {
          height: windowHeight + 'px',
          width: windowHeight / 0.5625 + 'px',
          'margin-left': (windowWidth - videoWidth) / 2 + 'px',
          'margin-bottom': 'initial'
        }
      }
    }
    window.onresize()
  },
  methods:{
    canplay() {
      this.vedioCanPlay = true
    },
    /**
     * 登录按钮
     */
    loginBtn(){
      this.$router.push('login')
    },
    /**
     * 注册按钮
     */
    registerBtn(){
      if(this.info.account !== '' && this.info.password !== '' && this.info.rePassword !== '' && this.info.nickName !== ''){
        if(this.info.rePassword !==this.info.password){
          this.$message({
            message: '两次密码不一致',
            type: 'warning'
          });
        }else {
          if(!this.regUnit(this.info.nickName,this.info.account,this.info.password,this.info.rePassword)){
            this.$message('信息不准确')
          }else {
            let userInfo = {
              username : this.info.nickName,
              userId : this.info.account,
              password : this.info.password,
              userAvatarUrl: null,
              userMail : null,
              userTel : null
            }
            // TODO 发送网络请求
            axios.post("http://localhost:8080/register",userInfo).then((res) => {
              console.log(res)
            })


            this.$message({
              message : "注册成功，请前往登录",
              type : "success",
              duration : 500
            })
            // setTimeout(() => {
            //   this.$router.push({name : "Correction"})
            // } , 750)
          }
        }
      }else {
        this.$message({
          message: '请勿提交空内容',
          type: 'warning'
        });
      }
    },
    /**
     * 验证数据的格式
     * @param nickName 用户名
     * @param account 账号（邮箱）
     * @param password 密码1
     * @param rePassword 密码2
     */
    regUnit(nickName,account,password,rePassword){
      let nameTest = nameReg(String(nickName))
      let accountTest = accountReg(String(account))
      let passwordTest = passwordReg(String(password))
      let rePasswordTest = passwordReg(String(rePassword))

      if(!nameTest){
        setTimeout(() => {this.$message.error("昵称格式不正确/(6~15位字符，包括数字，字母，下划线_，点. , 不能以数字开始)")},50)
        this.info.nickName = ''
        return false
      }else if(!accountTest){
        setTimeout(() => {this.$message.error("账号格式不正确/(13位纯数字，不能以0开始)")},50)
        this.info.account = ''
        return false
      }else if(!passwordTest){
          setTimeout(() => {this.$message.error("密码格式不正确/(8~16位字符，包括数字，字母，下划线_，点. , 不能以数字开始)")},50)
        this.info.password = ''
        return false
      }else if(!rePasswordTest){
            setTimeout(() => {this.$message.error("密码格式不正确/(8~16位字符，包括数字，字母，下划线_，点. , 不能以数字开始)")},50)
        this.info.password = ''
        return false
      }else {
        return true
      }

    },
  }
}

</script>

<style scoped lang="less">
.homepage-hero-module,
.video-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.video-container .poster img{
  z-index: 0;
  position: absolute;
}

.video-container .filter {
  z-index: 1;
  position: absolute;
  width: 100%;
}

.fillWidth {
  width: 100%;
}
.content{
  padding: 0 2vw ;
  width: 30vw;
  height: 50vh;
  margin:10vw 30vw;
  background-color: rgba(83,92,104,0.7);
  border-radius: 15px;
  .title{
    color: aliceblue;
    font-weight: bold;
    font-size: 2.4em;
    text-align: center;
    margin-bottom: 1vh;
    margin-top: 1vh;
  }
  .btn{
    text-align: center;
    *{
      margin: 0 2vw;
      width: 10vw;
    }
  }
}
</style>