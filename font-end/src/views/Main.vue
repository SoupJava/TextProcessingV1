<template>
  <div id="main" main>
    <el-container>
      <el-header>
        <div class="title">论文修改辅助平台</div>
        <div class="user" @click="logOut">{{log[logNum]}}</div>
        <div class="logout" @click="aboutMe">
<!--          <el-image-->
<!--          style="width: 50px; height: 50px;border-radius: 25px;"-->
<!--          v-model:src="url"-->
<!--          fit="fill"></el-image>-->
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
            <el-menu
                default-active="replace"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                :router=true>
              <el-menu-item index="replace">
                <i class="el-icon-refresh"></i>
                <span slot="title">谦敬词替换</span>
              </el-menu-item>
              <el-menu-item index="correction">
                <i class="el-icon-document-delete"></i>
                <span slot="title">文本纠错</span>
              </el-menu-item>
              <el-menu-item index="compare">
                <i class="el-icon-c-scale-to-original"></i>
                <span slot="title">文本比对</span>
              </el-menu-item>
              <el-submenu index="change2pdf">
                <template slot="title">
                  <i class="el-icon-guide"></i>文件转换
                </template>
                <el-menu-item @click="change('change2pdf')">
                  <template slot="title">
                    <i class="el-icon-s-marketing"></i>转PDF文件
                  </template>
                </el-menu-item>
                <el-menu-item  @click="change('change2docx')">
                  <template slot="title">
                    <i class="el-icon-s-claim"></i>转DOCX文件
                  </template>
                </el-menu-item>
              </el-submenu>
              <el-submenu index="fileCreat">
                <template slot="title">
                  <i class="el-icon-printer"></i>文件生成
                </template>
                <el-menu-item @click="change('filecompose')">
                  <template slot="title">
                    <i class="el-icon-files"></i>模板生成
                  </template>
                </el-menu-item>
                <el-menu-item  @click="change('filecreat')">
                  <template slot="title">
                    <i class="el-icon-receiving"></i>文件生成
                  </template>
                </el-menu-item>
              </el-submenu>
              <el-submenu index="myreplace">
                <template slot="title">
                  <i class="el-icon-user-solid"></i>我的
                </template>
                <el-menu-item @click="change('info')">
                  <template slot="title">
                    <i class="el-icon-tickets"></i>我的信息
                  </template>
                </el-menu-item>
                <el-menu-item  @click="change('articlelist')">
                  <template slot="title">
                    <i class="el-icon-info"></i>我的文本
                  </template>
                </el-menu-item>
              </el-submenu>
            </el-menu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {delUser} from "@/utils/testDataUtils"
import {user} from "@/utils/user";
import axios from "axios";
export default {
  name: "Main",
  data(){
    return{
      url: user.userAvatar,
      log : ["登录",'退出系统'],
      logNum : 0
    }
  },
  mounted() {
    axios.post("http://localhost:8080/getMyInfo").then((res) => {
      console.log()
      if(res.data.data.code === '-1'){
        this.logNum = 0
        return
      }else {
        this.logNum = 1
      }
      let userData = res.data.data
      //res.data.data.userId
      user.name = userData.username
      user.userAvatar = userData.userAvatarUrl === null || userData.userAvatarUrl === "" ? "/img/DefaultAvatar.png" : "https://thirdwx.qlogo.cn/mmopen/vi_32/rZEIzYRP1Qt6iacLJpuMdAoYfkf9aNJIM8hJsvviaZjdCvGrm6OIXraYniauOxic8miaLnEoZyJ65f7mDibzIcECMc2w/132"
      user.phone = userData.userTel === null ? ' ' : userData.userTel
      user.email = userData.userMail === null ? ' ' : userData.userMail
      user.password = userData.password

    })
    this.$router.push({path:'replace'})
    },
  methods:{
    logOut(){
      if(this.logNum === 0){
        this.$router.push({name:'Login'})
        return
      }
      let withdraw = confirm("确认退出")
      if(withdraw){
        if(delUser()){
          this.$message("退出成功")
          location.reload();
          this.$router.push({name:'Correction'})
        }else {
          this.$message.error("退出失败")
        }
      }
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    change(page){
      if(page === 'info'){
        this.$router.push('info')
      }else if(page === 'article'){
        this.$router.push('article')
      }else if(page === 'change2docx'){
        this.$router.push('change2docx')
      }else if(page === 'change2pdf'){
        this.$router.push('change2pdf')
      }else if(page === 'filecreat'){
        this.$router.push('filecreat')
      }else if(page === 'filecompose'){
        this.$router.push('filecompose')
      }else if(page === 'articlelist'){
        this.$router.push('articlelist')
      }
    },
    /**
     * 头像点击按钮
     */
    aboutMe(){
      this.$router.push({name:"Info"})
    },
  },

}
</script>

<style lang="less">
body{
  margin: 0;
  padding: 0;
  border: 0;
}
[main]{
  .el-header {
    background-color: #718093;
    text-align: center;
    .title{
      color: white;
      font-weight: bold;
      font-size: 26px;
      line-height: 60px;
      min-width: 10vw;
      display: inline;
      float: left;
      cursor: pointer;
    }
    .logout{//头像
      color: #cccccc;
      font-size: 1.2em;
      font-weight: bold;
      display: inline;
      float: right;
      line-height: 60px;
      cursor: pointer;
      padding: 5px;
    }
    .user{//退出按钮
      color: #cccccc;
      font-size: 1.2em;
      font-weight: bold;
      display: inline;
      float: right;
      line-height: 60px;
      cursor: pointer;
      margin-left: 3vw;
    }
  }

  .el-aside {
    //background-color: #333744;
    //color: #333;
    //text-align: center;
    height: calc(100vh - 60px);
  }

  .el-main {
    color: #333;
    padding: 0;
    //text-align: center;
    overflow: auto;
    height: calc(100vh - 60px);

    background-image: url("./../../public/img/bg.png");
    background-repeat: no-repeat;
    background-size: cover;
  }
  .el-main::-webkit-scrollbar{
    display: none;
  }




}
</style>

