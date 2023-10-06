<template>
  <div class="base">
    <!-- 注册登录界面 -->
    <div  class="loginAndRegist">
      <!--登录表单-->
      <div  class="loginArea">
        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!-- 标语 -->
          <div v-show="isShow" class="title">
            LOGIN
          </div>
        </transition>
        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!-- 密码框和用户名框 -->
          <div v-show="isShow" :model="loginUser" class="pwdArea">
            <div style="flex: 1;justify-content: center;display: flex;align-items: center;">
              <el-input prefix-icon="el-icon-user" class="username" v-model="loginUser.name" style="width: 165px"  placeholder="用户名"></el-input>
            </div>
            <div style="flex: 1;justify-content: center;display: flex;align-items: center">
              <el-input prefix-icon="el-icon-lock" placeholder="密码"  v-model="loginUser.password" style="width: 165px" show-password></el-input>
            </div>
            <div style="flex: 1;justify-content: center;display: flex;align-items: center">
              <el-popover trigger="click" placement="top" prop="validCode">
                <div slot="reference">
                  <el-input prefix-icon="el-icon-vaildcode" placeholder="验证码"  v-model="loginUser.code" style="width: 165px;" >
                  </el-input>
                </div>
                  <valid-code v-model="code" @update:value="getCode"/>
              </el-popover>


            </div>
          </div>
        </transition>
        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!-- 登录注册按钮/ 忘记密码 -->
          <div v-show="isShow" class="btnArea">
            <div style="display: flex; flex-direction: column; align-items: center;">
              <el-button style="background-color:#041322; letter-spacing: 5px;font-size: 14px;
                         border-radius: 30px !important;
                         " @click="UserLogin">
                登录</el-button>
              <div style="margin-top: 10px;margin-right: -285px; cursor: pointer ">
              <span class="span_a">忘记密码</span>
              </div>
            </div>
          </div>

        </transition>
      </div>
      <!-- 注册表单 -->
      <div class="registArea">
        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!--  注册表头-->
          <div v-show="!isShow" class="rigestTitle">
            管理员注册
          </div>
        </transition>
        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!--            注册表单-->
          <div  v-show="!isShow" class="registForm">
            <div style="flex: 1;display: flex;justify-content: center;align-items: center">
              用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;名:
              <el-input
                  placeholder="请输入用户名"
                  v-model="regUser.regUsername"
                  style="width: 165px;margin-left: 10px"
                  clearable
              >
              </el-input>
            </div>
            <div style="flex: 1;display: flex;justify-content: center;align-items: center">
              密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
              <el-input placeholder="请输入密码" style="width: 165px;margin-left: 10px" v-model="regUser.regPwd" show-password></el-input>
            </div>
            <div style="flex: 1;display: flex;justify-content: center;align-items: center;">
              确&nbsp;认&nbsp;密&nbsp;码:
              <el-input placeholder="请再次输入密码" style="width: 165px;margin-left: 10px" v-model="regUser.regRePwd" show-password></el-input>
            </div>
            <div style="flex: 1;display: flex;align-items: center">
              管理员审核:
              <template>
                <el-select id="elselect"  v-model="regUser.selectValue" filterable style="width: 100px;margin-left: 10px"  placeholder="请选择">
                  <el-option
                      v-for="item in admins"
                      :key="item.id"
                      :label="item.nickname"
                      :value="item.id">
                  </el-option>
                </el-select>
              </template>
            </div>
          </div>
        </transition>
        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!--            注册按钮-->
          <div  v-show="!isShow" class="registBtn">
            <el-button  style="background-color: #041322; letter-spacing: 5px; border-radius: 30px !important;
                        font-size: 14px;" @click="userRegister">注册</el-button>
          </div>
        </transition>
      </div>
      <!-- 信息展示界面 -->
      <div id="aaa" class="showInfo"
           :class="{
             'login': isShow, 'register': !isShow
            }"
           :style="{
             borderTopRightRadius:styleObj.bordertoprightradius,
             borderBottomRightRadius:styleObj.borderbottomrightradius,
             borderTopLeftRadius:styleObj.bordertopleftradius,
             borderBottomLeftRadius:styleObj.borderbottomleftradius,
             right:styleObj.rightDis,
            }"
           ref="showInfoView">

        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!-- 没有用户输入用户名或者找不到用户名的时候 -->
          <div v-show="isShow" style="display: flex;flex-direction: column;align-items: center;
          justify-content: center;width: 100%;height: 100%">
            <!-- 欢迎语 -->
            <div style="flex: 2;display: flex;align-items: center;font-size: 22px;color:#8C4356;font-weight: bold">
              欢迎登入后台管理系统
            </div>
            <!-- 欢迎图片 -->
            <div style="flex: 2">
              <el-button class="chk_registBtn"
                         style=" border-radius: 30px !important; font-size: 14px"
                         @click="changeToRegiest">还没有账户？点击注册
              </el-button>
            </div>
          </div>
        </transition>

        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__fadeInUp"
            leave-active-class="animate__zoomOut"
            appear
        >
          <!-- 用户注册的时候展示信息 -->
          <div v-show="!isShow" style="display: flex;flex-direction: column;align-items: center;justify-content: center;width: 100%;height: 100%">
            <!-- 欢迎语 -->
            <div style="flex: 2;display: flex;align-items: center;font-size: 22px;color:#8C4356;font-weight: bold">
              欢迎注册
            </div>
            <!-- 欢迎图片 -->
            <div style="flex: 2">
              <el-button  class="chk_loginBtn" style="border-radius: 30px !important; font-size: 14px" @click="changeToLogin">已有账户？点击登录</el-button>
            </div>
          </div>
        </transition>
      </div>
    </div>

  </div>
</template>

<script>
import 'animate.css';
// eslint-disable-next-line no-unused-vars
import request from "@/utils/request";
import ValidCode from "@/ValidCode/ValidCode";
export default {
  name:'Login',
  components:{
    ValidCode
  },
  data() {
    return {
      code: '', //验证码组件传过来的code
      loginUser: {
        code: "", //表单上用户输入的code
        name: "",
        password: ""
      },


      admins:{},
      ////看看用不用转成用户对象
      regUser:{
        regUsername:'',
        regRePwd:'',
        regPwd:'',
        selectValue:"",
      },
      styleObj:{
        bordertoprightradius:'15px',
        borderbottomrightradius: '15px',
        bordertopleftradius:'0px',
        borderbottomleftradius:'0px',
        rightDis:'0px'
      },
      isShow:true,
      loginImage: "../assets/login.png", // 登录状态的背景图片
      registerImage: "../assets/register.png", // 注册状态的背景图片
    }
  },
  created() {
    this.loadInfoOfAdmin();
  },
  methods:{
    changeToRegiest(){
      this.styleObj.bordertoprightradius= '0px'
      this.styleObj.borderbottomrightradius='0px'
      this.styleObj.bordertopleftradius='15px'
      this.styleObj.borderbottomleftradius='15px'
      this.styleObj.rightDis='50%'
      this.isShow = !this.isShow
    },
    changeToLogin(){
      this.styleObj.bordertoprightradius= '15px'
      this.styleObj.borderbottomrightradius='15px'
      this.styleObj.bordertopleftradius='0px'
      this.styleObj.borderbottomleftradius='0px'
      this.styleObj.rightDis='0px'
      this.isShow = !this.isShow
    },
    //验证码验证
    getCode(code){
      this.validCode = code; // 更新validCode的值
      console.log(this.validCode); // 打印验证码的code值
    },
    //用户登录
    UserLogin(){
      // request.post("/user/login",this.loginUser).then(res=>{
      //   if(res.code=="200"){
      //     localStorage.setItem("user",JSON.stringify(res.data))
      //     this.$message.success("登陆成功！")
      //     this.$router.push("/manage")
      //   }else if(res.code=="400"){
      //     this.$message.warning(res.msg)
      //   }else if(res.code=="401"){
      //     this.$message.error(res.msg)
      //   }
      //   else{
      //     this.$message.error("用户名或密码错误！")
      //   }
      // })
    },
    //加载管理员信息
    loadInfoOfAdmin(){
      this.request.get("/user/listOfAdmin").then(res=>{
        if(res.code=="200"){
          this.admins=res.data
          return true
        }
      })
    },
    //用户注册
    userRegister(){
      if(this.regUser.regUsername===""){
        this.$message.error("用户名不能为空！")
        return false
      }else if(this.regUser.regPwd!=this.regUser.regRePwd){
        this.$message.error("两次密码输入不同，请检查后重新注册！")
        return false
      }else{
        let user = {};
        user.name = this.regUser.regUsername
        user.password = this.regUser.regPwd
        user.auditor = this.regUser.selectValue
        this.request.post("/user/register",user).then(res=>{
          if(res.code==="200"){
            this.$message.success("申请成功，请耐心等待管理员审核！")
            this.regUser={  regUsername:'',
              regRePwd:'',
              regPwd:'',
              selectValue:""}
            this.changeToLogin()
          }
          if(res.code==="400"){
            this.$message.error(res.msg)
            return
          }
        })
      }

    }
  },

}
</script>

<style>

 .base{
   height: 100vh;
   display: flex;
   justify-content: center;
   align-items: center;
   background-image: url("../assets/background.png");
   background-size: 100%;
 }
.loginAndRegist{
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}
.loginArea{
  background-color: rgba(255,255,255,0.8);
  border-top-left-radius: 15px;
  border-bottom-left-radius: 15px;
  height: 400px;
  width: 350px;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
}
.registArea{
  border-top-right-radius: 15px;
  border-bottom-right-radius: 15px;
  height: 400px;
  width: 350px;
  background-color: rgba(255,255,255,0.8);
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content:center ;
  align-items: center;
}
.showInfo{
  border-top-right-radius: 15px;
  border-bottom-right-radius: 15px;
  position: absolute;
  height: 400px;
  width: 350px;
  z-index:2;
  top: 0;
  right: 0;
  background-size: 90%;
}
.chk_registBtn{
  border: 1px solid #ffffff !important;
  background: rgba(254,217,23,0.5) !important;
  color: #9D2933 !important;
}
.chk_registBtn:hover{
  background: rgba(254,217,23,0.9) !important;
}
.chk_loginBtn{
  background: rgba(254,217,23,0.5) !important;
  border: 1px solid #ffffff;
  color: #9D2933 !important;
}
.chk_loginBtn:hover{
  background: rgba(254,217,23,0.9) !important;
}
.login {
  background-image: url("../assets/login.png");
}

.register {
  background-image: url("../assets/register.png");
}
.showInfo:hover{
  background-size: 100%;
  background-position: -40px -40px;
}
.title{
  width: 70%;
  flex:1;
  border-bottom: 1px solid #041322;
  display: flex;
  align-items: center;
  color: #041322;
  font-weight: bold;
  font-size: 24px;
  justify-content: center;
}
#aaa{
  transition: 0.3s linear;
}
.pwdArea{
  width: 100%;
  flex:2;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.pwdArea input{
  outline: none;
  height: 30%;
  border-radius:13px ;
  padding-left: 10px;
  font-size: 12px;
  border: 1px solid gray;
}
.pwdArea input:focus{
  border: 2px solid #041322;
}
.btnArea{
  flex:1;
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.span_a{
  color: #808080;
 }
.span_a:hover{
  color: #eb7350;
}
.rigestTitle{
width: 70%;
flex: 1;
color: #041322;
font-weight: bold;
font-size: 24px;
display: flex;
justify-content: center;
align-items: center;
border-bottom: 1px solid #041322;
}
.registForm{
flex: 2;
display: flex;
flex-direction: column;
color: #041322;
font-size: 16px;
}

.registForm input{
outline: none;
height: 30%;
border-radius:13px ;
padding-left: 10px;
font-size: 12px;
border: 1px solid gray;
}
.registForm input:focus{
border: 2px solid #041322;
}
#elselect:focus{
border: 2px solid #041322;
}
.registBtn{
flex: 1;
display: flex;
justify-content: center;
align-items: center;
}
.el-input__icon.el-icon-user,
.el-input__icon.el-icon-lock,
.el-input__icon.el-icon-vaildcode{
   margin-top: 4px;
 }

</style>