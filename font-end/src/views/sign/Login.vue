<template>
  <div class="homepage-hero-module">
    <div class="video-container">
      <div :style="fixStyle" class="filter">
        <div class="content">
          <br>
          <p class="title">欢迎登录</p>
          <el-input
              placeholder="请输入账号"
              maxlength="13"
              v-model="info.account"
              clearable>
          </el-input>
          <br><br><el-input
            placeholder="请输入密码"
            show-password
            v-model="info.password"
            clearable>
        </el-input>
          <br><br>
          <div style="display: flex;">
            <el-input
                placeholder="请输入验证码"
                maxlength="4"
                show-word-limit
                v-model="info.verification"
                style="width: 50%;"
                clearable>
            </el-input>
            <ValidCode @input="createValidCode" ref="refreshClick" style="background-color: #333333;margin-left: 2vw" />
          </div>
          <br><br>
          <div class="btn">
            <el-button type="primary" @click="loginBtn">登录</el-button>
            <el-button type="primary" @click="registerBtn">注册</el-button>
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
import ValidCode from "@/components/ValidCode";
import {setUser,getUser} from "@/utils/testDataUtils"
import {emailReg,passwordReg} from "@/utils/regUtils"
import axios from "axios";

export default {
  name: "Login",
  components:{
    ValidCode
  },
  data(){
    return{
      vedioCanPlay: false,
      fixStyle: '',
      validCode: '',
      info : {
        account: '',
        password : '',
        verification : ''
      },
      loginInfo:{
        userId : '1234567',
        password : '1234567',
        // username : '',
        // userTel : '',
        // userMail : '',
        // userAvatarUrl : ''
      },
    }
  },
  mounted() {
    sessionStorage.removeItem("user")
    this.intoAlert()
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
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    canplay() {
      this.vedioCanPlay = true
    },
    /**
     * 登录按钮
     */
    async loginBtn(){
      if(this.info.account !== '' && this.info.password !== '' && this.info.verification !== ''){
        // if(this.info.verification.toUpperCase() !== this.validCode.toUpperCase()){
        //   this.$message.error("验证码错误")
        //   this.info.verification = ''
        // }else {
        //   if(this.regUnit(this.info.account,this.info.password)){
        //     setUser(this.info.account,this.info.password)
        //
        //     // axios.post('http://localhost:8080/login',this.loginInfo).then((res) => {
        //     //   console.log(res)
        //     // })
        //     this.$router.push({path:'/article'})
        //
        //   }else {
        //     this.info.account = ''
        //     this.info.password = ''
        //   }
        // }
        if(this.info.account.length < 13){
          this.$message.error("账号长度为13位")
          this.info.account = ""
          return
        }else {
          if(this.info.verification.toUpperCase() !== this.validCode.toUpperCase()){
            this.$message.error("验证码错误")
            this.info.verification = ''
            return
          }
        }

        let msg = await setUser(this.info.account,this.info.password)
        if(msg === "密码错误"){
          this.$message.error("账号或密码错误")
          this.info.account = ""
          this.info.password = ""
          this.$refs.refreshClick.refreshCode()
          this.info.verification = ''
        }else {
          location.reload();
        }
        console.log('>>>||')
        await this.$router.push({path:'/article'})
      }else {
        this.$message({
          message: '请勿提交空内容',
          type: 'warning'
        });
      }
    },
    /**
     * 注册按钮
     */
    registerBtn(){
      this.$router.push('register')
    },
    /**
     * 测试账号密码的格式
     * @param account 账号
     * @param password 密码
     */
    regUnit(account,password){
      let accountTest = emailReg(account)
      let passwordTest = passwordReg(password)
      if(accountTest){
        if(passwordTest){
          return true
        }else {
          this.$message({
            message: '密码格式不正确'+"\n"+'密码由8~16位：数字，字符，_ , . 组成,不能以 _ 开始',
            type: 'warning'
          });
          return false
        }
      }else {
        this.$message({
          message: '账号格式不正确'+'/邮箱格式不正确',
          type: 'warning'
        });
        return false
      }
    },
    /**
     * 进入页面时的提醒
     */
    intoAlert(){

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
  padding: 0 2vw;
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