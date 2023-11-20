<template>
  <transition
      name="animate__animated animate__bounce"
      enter-active-class="animate__rollIn"
      leave-active-class="animate__rollOut"
      appear
  >
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
            <el-form :model="loginUser" :rules="rules" ref="UserLoginRef" class="pwdArea">
              <el-form-item prop="username">
            <div style="flex: 1;justify-content: center;display: flex;align-items: center; margin-top: 20px">
              <el-input prefix-icon="el-icon-user" class="username" v-model="loginUser.username"  style="width: 165px"
                        placeholder="用户名"></el-input>
            </div>
              </el-form-item>
              <el-form-item prop="password">
            <div style="flex: 1;justify-content: center;display: flex;align-items: center; margin-top: 20px">
              <el-input prefix-icon="el-icon-lock" placeholder="密码"  v-model="loginUser.password" style="width: 165px"
                        prop="password" show-password></el-input>
            </div>
              </el-form-item>

              <el-form-item prop="code">
            <div style="flex: 1;justify-content: center;display: flex;align-items: center; margin-top: 20px">
              <el-popover trigger="click" placement="top">
                <div slot="reference">
                  <el-input prefix-icon="el-icon-vaildcode" placeholder="验证码"  v-model="loginUser.code"
                            style="width: 165px;" >
                  </el-input>
                </div>
                <div>
                <valid-code @update:value="getCode" />
                </div>
              </el-popover>
            </div>
              </el-form-item>
            </el-form>
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
              <div style="margin-top: 10px;margin-right: -285px; cursor: pointer"  @click="handleForgetPwd">
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
            <el-form class="el-registForm" :model="regUser" :rules="rules" ref="RegistUserRef">
            <div style="flex: 1;display: flex;justify-content: center;align-items: center;margin-top: 18px">
              用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;名:
              <el-form-item prop="regUsername" style="margin-top: 20px">
              <el-input
                  placeholder="请输入用户名"
                  v-model="regUser.regUsername"
                  style="width: 165px;margin-left: 10px"
                  clearable
              >
              </el-input>
              </el-form-item>
            </div>
            <div style="flex: 1;display: flex;justify-content: center;align-items: center ">
              密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
              <el-form-item prop="regPwd" style="margin-top: 20px">
              <el-input placeholder="请输入密码" style="width: 165px;margin-left: 10px" v-model="regUser.regPwd" show-password></el-input>
              </el-form-item>
            </div>
            <div style="flex: 1;display: flex;justify-content: center;align-items: center ">
              确&nbsp;认&nbsp;密&nbsp;码:
              <el-form-item prop="regRePwd" style="margin-top:20px">
              <el-input placeholder="请确认密码" style="width: 165px;margin-left: 10px" v-model="regUser.regRePwd" show-password></el-input>
              </el-form-item>
            </div>
            <div style="flex: 1;display: flex;align-items: center">
              管理员审核:
              <template>
                <el-form-item  style="margin-top:20px">
                <el-select id="elselect"  v-model="regUser.selectValue" filterable style="width: 100px;margin-left: 10px"  placeholder="请选择">
                  <el-option
                      v-for="item in admins"
                      :key="item.id"
                      :label="item.username"
                      :value="item.id">
                  </el-option>
                </el-select>
                </el-form-item>
              </template>
            </div>
            </el-form>
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
    <el-dialog title="忘记密码"  :visible.sync="forgetPwdDialoVisible" class="reset-dialog">
      <el-form :model="forgetUserForm" label-width="80px" style="padding-right: 20px">
        <el-form-item label="邮箱">
          <el-input size="medium" style="width: 350px" v-model="forgetUserForm.email" autocomplete="off" placeholder="请输入电子邮箱"></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input size="medium"  style="width: 200px" v-model="forgetUserForm.code"  placeholder="请输入验证码"></el-input>
          <el-button type="primary" size="small" style="margin-left: 50px"
                     :disabled="isCountingDown"
                     @click="sendEmailCode(2)">{{ isCountingDown ? remainingTime + 's' : '获取验证码' }}</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-top: 25px">
        <el-button @click="forgetPwdDialoVisible = false" style="margin-right: 10px">取 消</el-button>
        <el-button type="primary" @click="resetPwd">重置密码</el-button>
      </div>
    </el-dialog>
  </div>
</transition>
</template>

<script>
import 'animate.css';
import ValidCode from "@/ValidCode/ValidCode";
import {loadOfAdmin, login, resetPwd, sendEmail, userRegister} from "@/api/login";

export default {
  name:'Login',
  components:{
    ValidCode
  },
  data() {


    //验证码校验
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else if(value.toLowerCase() !== this.code) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }

    //确认密码校验
    const validatePwd = (rule, regRePwd, callback) => {
      if (regRePwd === '') {
        callback(new Error('请确认密码'))
      } else if(regRePwd !== this.regUser.regPwd) {
        callback(new Error('两次密码输入不同,请检查后重试！'))
      } else {
        callback()
      }
    }


    return {
      forgetUserForm:{  //忘记密码表单数据

      },
      isCountingDown:false,
      remainingTime: 0,   // 剩余时间
      countdownTimer: null, // 倒计时计时器
      forgetPwdDialoVisible:false,
      code: '', //验证码组件传过来的code
      loginUser: {
        code: "", //表单上用户输入的code
        username: "",
        password: ""
      },

      admins:[],
      ////看看用不用转成用户对象
      regUser:{
        regUsername:'',
        regRePwd:'',
        regPwd:'',
        selectValue:"",
      },
      rules:{
        username: [
          {
            required: true, message:'请输入账号', trigger: 'blur'
          },
        ],
        password: [
          {
            required: true, message:'请输入密码', trigger: 'blur'
          },
        ],
        code: [
          {
            validator: validateCode, trigger:'blur'
          }
        ],
        regUsername: [
          {
            required: true, message:'请输入用户名', trigger:'blur'
          }
        ],
        regPwd: [
          {
            required:true, message:'请输入密码', trigger:'blur'
          },
          {
            min: 6, max: 20, message: '密码长度太短', trigger: 'blur'
          }
        ],
        regRePwd: [
          {
            validator:validatePwd,trigger:'blur'
          },
          {
            min: 6, max: 20, message: '密码长度太短', trigger: 'blur'
          }
        ],
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
    handleForgetPwd(){   //初始化表单的数据
      this.forgetUserForm = {}
      this.forgetPwdDialoVisible = true
    },
    //获取验证码
    sendEmailCode(type) {
      let email;
      if (type === 2) {
        email = this.forgetUserForm.email;
      } else {
        this.$message.warning("请输入邮箱账号");
        return;
      }
      if (!email || !/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(email)) {
        this.$message.warning("请输入正确的邮箱账号");
        return;
      }
      // 发送邮箱验证码
      sendEmail(email, type).then(res => {
        if (res.code === '200') {
          this.$message.success("发送成功")
          this.startTimer()   // 发送验证码成功后开始倒计时
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    startTimer() {
      this.isCountingDown = true  // 开始倒计时
      this.remainingTime = 60     // 设置倒计时时间为 60 秒
      this.countdownTimer = setInterval(() => {
        if (this.remainingTime > 0) {
          this.remainingTime--
        } else {
          clearInterval(this.countdownTimer)   // 停止倒计时器
          this.isCountingDown = false           // 将 isCountingDown 属性设置为 false
        }
      }, 1000)
    },

    //重置密码
    resetPwd(){
      resetPwd(this.forgetUserForm).then(res => {
        if (res.code === '200') {
          this.$message.success("重置密码成功，新密码为：123456，请尽快修改密码")
          this.forgetPwdDialoVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    //验证码验证
    getCode(code){
      this.code = code.toLowerCase() // 更新validCode的值
    },
    //用户登录
    UserLogin(){
      this.$refs['UserLoginRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          login(this.loginUser).then(res => {
            if (res.code == "200") {
              if (res.data.role === '管理员' || res.data.role === '超级管理员') {
                localStorage.setItem("manage_config", JSON.stringify(res.data)) //存储用户数据
                //根据不同时间段，插入不同的登录成功提示欢迎语
                const currentTime = new Date();
                const currentHour = currentTime.getHours();
                let welcomeMessage = '';
                let roleMessage = res.data.role.toString();
                if (currentHour >= 5 && currentHour < 12) { // 8点到12点为上午
                  welcomeMessage = '上午好！亲爱的'+ ' ' + roleMessage + '，' + '新的一天又开始了，请努力工作！';
                } else if (currentHour >= 12 && currentHour < 18) { // 12点到18点为下午
                  welcomeMessage = '下午好！亲爱的' + ' ' + roleMessage + '，' + '午休过后，请努力工作！';
                } else if (currentHour >= 18 && currentHour < 24) {  // 18点到次日凌晨0点为晚上
                  welcomeMessage = '晚上好！亲爱的' + ' ' + roleMessage + '，' + '工作完成后，请休息哦！';
                } else  { // 0点到次日凌晨5点深夜
                  welcomeMessage = '夜深了！亲爱的' + ' ' + roleMessage + '，' + '身体是革命的本钱，请注意休息哦！';
                }
                this.$message.success(welcomeMessage );
                this.$router.push("/");
              }
                else {
                this.$message.error("您没有管理员权限")
              }
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    //加载管理员信息
    loadInfoOfAdmin(){
      loadOfAdmin(this.admins).then(res=>{
        if(res.code=="200"){
          this.admins=res.data
          return true
        }
      })
    },
    //用户注册
    userRegister(){
      this.$refs['RegistUserRef'].validate((valid) =>{
        if (valid){
          let user = {};
          user.username = this.regUser.regUsername
          user.password = this.regUser.regPwd
          user.role = '用户'
            userRegister(user).then(res=>{
            if(res.code==="200"){
              this.$message.success("申请成功，请耐心等待管理员审核！")
              this.regUser={  regUsername:'',
                regRePwd:'',
                regPwd:'',
                selectValue:''}
              this.changeToLogin()
            }
            if(res.code==="500"){
              this.$message.error(res.msg)
              return
            }
          })
        }
      })

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
}
.el-registForm{
font-size: 14px;
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
   margin-top: 6px;
 }
.el-input--medium .el-input__inner {
   border-radius: 12px !important;
 }

.reset-dialog .el-dialog {
  width: 30% !important;
  height: 38% !important;
  border-radius: 12px !important;
}
</style>
