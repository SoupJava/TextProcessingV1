import Vue from 'vue'
import VueRouter from 'vue-router'

import Main from '@/views/Main.vue'

import Login from "@/views/sign/Login";
import Register from "@/views/sign/Register";

import Correction from "@/views/Correction";
import Replace from "@/views/Replace";
import Compare from "@/views/Compare";

import Info from "@/views/user/Info";
import Article from "@/views/user/Article";
import ArticleList from "@/views/user/ArticleList";

import Change2DOCX from "@/views/changeFile/Change2DOCX";
import Change2PDF from "@/views/changeFile/Change2PDF";

import FileCompose from "@/views/solve/FileCompose";
import FileCreat from "@/views/solve/FileCreat";

import {getUser} from "@/utils/testDataUtils";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Main',
    component: Main,
    redirect : 'compare',
    children : [
      {
        path: 'replace',
        name : 'Replace',
        component: Replace

      },{
        path: 'correction',
        name : 'Correction',
        component: Correction

      },{
        path: 'info',
        name : 'Info',
        component: Info

      },{
        path: 'article',
        name : 'Article',
        component: Article

      },{
        path: 'login',
        name : 'Login',
        component: Login

      },{
        path: 'register',
        name : 'Register',
        component: Register
      },{
        path: 'change2docx',
        name : 'Change2DOCX',
        component: Change2DOCX
      },{
        path: 'change2pdf',
        name : 'Change2PDF',
        component: Change2PDF
      },{
        path: 'filecreat',
        name : 'FileCreat',
        component: FileCreat
      },{
        path: 'filecompose',
        name : 'FileCompose',
        component: FileCompose
      },{
        path: 'compare',
        name : 'Compare',
        component: Compare
      },{
        path: 'articlelist',
        name : 'ArticleList',
        component: ArticleList
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach(async (to,from,next)=>{
  console.log(`跳转至${to.name}页面`)
  // to: 即将要进入的路由
  // from：从哪个路由来的即当前导航正要离开的路由
  // next: 放行，进入管道中的下一个路由
  if(to.name === "Info" || to.name === "Article" || to.name === "ArticleList"){
    if( await getUser() === 1){
      next()
    }else {
      next()  //先放行之前的路由，再修改路由  //不然会报错,无法解决该问题，所以整了一个这个方法
      next("/login")
      // this.$router.push({path:"login"})  //没有this
    }
  }else {
    next()
  }

})

export default router
