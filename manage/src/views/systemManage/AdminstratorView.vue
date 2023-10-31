<template>
<div class="userSettingStyle">
  <el-tabs tab-position="left" v-model="activeName">
    <el-tab-pane label="我的信息" name="myInfo">
      <!-- 标头 -->
      <div class="settingStyleTitle">我的信息</div>
      <!-- 用户信息 -->
      <div style = "display: flex">
        <div class="userImageStyle">
          <div class="userImageSpan">用户头像</div>
          <div style="height: 160px; margin-bottom: 12px">
            <el-image
              :model="user"
              :src="user.avatar ||'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'"
              style="width: 160px; height: 160px; border-radius: 8px"
              ></el-image>
            <el-upload :action="$baseUrl + '/file/upload'"
                       :on-success="handleAvatarSuccess"
                       :headers="{token : user.token}"
                       :show-file-list="false"
                       list-type="picture"
                       style="line-height: 34px; height: 34px; margin-top: 30px">
              <el-button icon="el-icon-upload2" type="primary" plain>
                更换头像
              </el-button>
            </el-upload>
          </div>


        </div>
        <!-- 用户设置表单 -->
        <div class="userSettingForm">
        <el-form :model="user" label-width="80px" class="settingform" style="margin-top: 30px">
          <el-form-item label="用户名：" prop="username">
            <span  style="display: flex; justify-content: center; font-size: 18px">{{user.username}}</span>
          </el-form-item>
          <el-form-item label="姓名：" prop="name">
            <el-input v-model="user.name"  placeholder="姓名"></el-input>
          </el-form-item>
            <el-form-item label="电话：" prop="phone">
              <el-input v-model="user.phone"  placeholder="电话"></el-input>
            </el-form-item>
            <el-form-item label="邮箱：" prop="email">
              <el-input v-model="user.email" placeholder="邮箱"></el-input>
            </el-form-item>
            <el-form-item label="地址：" prop="address">
              <el-input type="textarea" v-model="user.address" placeholder="居住地址"></el-input>
            </el-form-item>
            <div style="display: flex; justify-content: center;margin-top: 30px">
              <el-button type="primary" icon="el-icon-check"
                         style="margin-right: 580px"
                         @click="update">保 存</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </el-tab-pane>

    <el-tab-pane label="修改密码" name="changePwd" >

      <!-- 标头 -->
      <div class="settingStyleTitle">修改密码</div>

      <!-- 修改密码  -->
      <div class="pwdChangeStyle">
        <el-form :model="user" label-width="80px" class="pwdSettingForm" :rules="rules" ref="changePwdRef">
          <el-form-item label="当前密码：" prop="password" style="margin-top:30px; width: 350px">
            <el-input show-password   placeholder="当前密码"></el-input>
          </el-form-item>
          <el-form-item label="新密码：" prop="newPwd" style="margin-top:10px; width: 350px">
            <el-input show-password v-model="user.newPwd"  placeholder="新密码"></el-input>
          </el-form-item>
          <el-form-item label="确认密码：" prop="confirmPwd" style="margin-top:10px; width: 350px">
            <el-input show-password v-model="user.confirmPwd"  placeholder="确认密码"></el-input>
          </el-form-item>
          <div style="margin-top: 30px;margin-bottom: 20px">
            <el-button type="primary" icon="el-icon-check"
                       style=""
                       @click="confirm">确 认</el-button>
          </div>
        </el-form>
      </div>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import {updateUser} from "@/api/systemManage/adminstrator";

export default {
  data() {
    const validatePwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if(value !== this.user.newPwd) {
        callback(new Error('两次密码输入不同,请检查后重试！'))
      } else if (value === this.user.password) {
        callback(new Error('新密码不能与当前密码一致'));
      }else {
        callback()
      }
    }
    const validateNewPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认新密码'))
      } else if (value === this.user.password) {
        callback(new Error('新密码不能与当前密码一致'));
      } else {
        callback();
      }
    }
    return{
      activeName:'myInfo',
      user:JSON.parse(localStorage.getItem('manage_config') || "{}"),

      rules: {
        password: [
          {
            required: true, message:'请输入当前密码', trigger: 'blur'
          },
          ],
        newPwd: [
          {
            validator: validateNewPwd, required:true, trigger: 'blur'
          },
          {
            min: 6, max: 20, message: '密码长度太短', trigger: 'blur'
          }
        ],
        confirmPwd: [
          {
            validator: validatePwd, required: true, trigger: 'blur'
          },
          {
            min: 6, max: 20, message: '密码长度太短', trigger: 'blur'
          }
        ],
      }
    }
  },

  methods: {
    handleAvatarSuccess(response, file, fileList){
      this.user.avatar = response.data
    },
    confirm(){
      this.$refs.changePwdRef.validate((valid) =>{
        if (valid){
          this.user.password = this.user.newPwd
          //保存当前的用户信息到数据库
          updateUser(this.user).then(res =>{
            if (res.code === '200') {
              //成功更新
              this.$message.success('密码已更改！')
              this.$router.push('/login')
            } else{
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    update(){
      //保存当前的用户信息到数据库
      updateUser(this.user).then(res =>{
        if (res.code === '200') {
          //成功更新
          this.$message.success('资料已更新！')
          //更新浏览器缓存里的用户信息
          localStorage.setItem('manage_config', JSON.stringify(this.user))

          //触发父级更新
          this.$emit('update:user', this.user)
        } else{
          this.$message.error(res.msg)
        }
      })
    },
  },
  created() {
    const tab = this.$route.query.tab;
    if (tab) {
      this.activeName = tab;
    }
  },

  watch: {
    '$route.query.tab'(tab) {
      if (tab) {
        this.activeName = tab;
      }
    }
  }
}


</script>

<style scoped>
.userSettingStyle {
  background-color:#fff;
  width: 100%;
  height: calc(100vh - 100px);
  border-radius: 12px;
  padding: 12px;
}
.el-tabs {
  border-left:0;
  height: 100%;
}
/deep/ .el-tabs__nav {
  width: 150px;
  height: 100%;
  overflow: hidden;
  position: relative;
}
/deep/ .el-tabs__nav .el-tabs__item {
  font-size: 16px;
  text-align: left !important;
  line-height: 40px;
  padding-left: 20px;
  transition: transform 0.3s ease, opacity 0.3s ease;
}

/deep/ .el-tabs__nav .el-tabs__item.is-active{
  font-weight: 500;
  background-color: #e6f7ff;
  padding-left: 20px !important;
}
/deep/ .el-tabs__nav .el-tabs__active-bar {
  height: 40px !important;
}
/deep/ .el-tabs__nav .el-tabs__item:nth-child(2) {
  padding-left: 20px !important;
}
.settingStyleTitle {
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  font-size: 20px;
  line-height: 28px;
  text-align: left;
}
.userSettingForm{
  text-align: left;
  margin-left: 10px;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 100%;
}
.pwdChangeStyle {
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 80%;
  margin: 0 auto;
}
.pwdSettingForm {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/deep/.el-form-item__label {
  white-space: nowrap;
}
.el-input--small .el-input__inner {
  height: 32px;
  line-height: 150px;
  width: 300px;
}
.el-form-item {
  margin-bottom: 80px;
}
.userSettingForm .el-form-item {
  line-height: 50px;
  max-width: 460px;
}
.userImageStyle {
  width: 300px !important;
  text-align: center;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  height: 650px;
}
.userImageSpan {
  line-height: 32px;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 500;
}

.el-image{
  margin-top: 70px;
}
.el-button{
  border-radius: 12px !important;
}
/deep/.el-input__inner,
/deep/.el-textarea__inner {
  border-radius: 8px !important;
  overflow: hidden;
}
</style>
