<template>
  <div id="ArticliItem" ArticliItem  @click="openArticleItems">
    <div class="name">{{info.name}}</div>
    <hr />
    <div class="num" style=";margin-left: 5px;margin-top: 5px;font-size: 1.1em;" >共 {{info.num}} 个版本</div>
  </div>
</template>

<script>
export default {
  name: "ArticliItem",
  props:{
    pInfo :{
      name:null,
      num : null,
      bate1 : null,
      bate2 : null,
      bate3 : null,
      isSelected : null,  //判断是否是被选中的项目
    }
  },
  data(){
    return{
      info:{},
      test : 1,
    }
  },
  created() {
    this.init()
    },
  methods:{
    init(){
      this.changeName()
    },
    changeName(){
      console.log('000000000000111111111')
      this.info.name = this.pInfo.name
      this.info.name = this.info.name.length > 10 ? this.info.name.slice(0,7)+"..." : this.info.name
      this.info.num = this.pInfo.num ? this.pInfo.num : 0
    },
    openArticleItems(){
      console.log('===<<<<<')
      console.log(this.pInfo)
      this.$router.push({path: `/article?article=${this.pInfo.name}&bate1=${this.pInfo.bate1}&bate2=${this.pInfo.bate2}&bate3=${this.pInfo.bate3}`});
    },
  },

}
</script>

<style scoped lang="less">

[ArticliItem]{
  width: 17vw;
  height: 130px;
  color: #f7f1e3;
  background-color: rgb(113, 128, 147);
  border-radius: 5px;
  margin: 3vh 2vw 0vh 1vw;
  float: left;
  padding: 5px 4px;
  .name{
    margin-left: 5px;
    margin-top: 5px;
    font-weight: bold;
    font-size: 1.8em;
  }
  .num{
    margin-left: 5px;
    margin-top: 5px;
    font-size: 1.5em;
  }
}
[ArticliItem]:hover{
  background-color: rgb(220, 223, 230);
  background-blend-mode: multiply,multiply;
  animation-name: example;
  color: #5b5151;
  animation-duration: 0.4s;
  cursor: pointer;
  box-shadow: 0 12px 12px 0 rgba(0, 0, 0, 0.3);
}
@keyframes example {
  from {box-shadow: 0 12px 12px 0 rgba(0, 0, 0, 0.5);}
  to {background-color: rgb(220, 223, 230);box-shadow: 0 12px 12px 0 rgba(0, 0, 0, 0.3);}
}
</style>